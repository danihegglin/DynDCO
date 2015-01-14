package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.DataGraphVertex
import dispatch._, Defaults._
import scala.collection.mutable.MutableList
import scala.util.Random
import ch.uzh.dyndco.stack.vertex.DynamicVertex
import ch.uzh.dyndco.problems.MeetingConstraints
import collection.mutable.Map
import collection.mutable.Set

class DPOPVertex (id: Any, agentView: DPOPMessage) 
  extends DynamicVertex(id, agentView){
  
	/**
	 * Parent / Child relationships
	 */
	var parent : DPOPVertex = null
	var children : MutableList[DPOPVertex] = MutableList()
	
	/**
	 * Current utilities and values
	 */
	var utilities = Map[Int, Map[Int, Double]]()
  var values = Map[Int,Int]()
	
	/**
	 * Message containers
	 */
	var utilMessages : MutableList[DPOPMessage] = MutableList()
	var valueMessages : MutableList[DPOPMessage] = MutableList()

  /**
   * Relationships
   */
	def addParent(_parent : DPOPVertex) = {parent = _parent}
	def addChild(_child : DPOPVertex) = {children += _child}
  def removeChild(_child : DPOPVertex) = {
    for(childNum <- 0 to children.size){
      var child = children.get(childNum)
      if(child == _child)
        children.drop(childNum)
    }
  }
	
  /**
   * Calculates all utilities
   */
	def computeUtils() = {
    
	  // Create local utilities
    var localUtilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
    utilities += (MEETING_ID -> localUtilities)
       
    // Merge map with util messages
    for(utilMessage <- utilMessages){
      
      for(singleUtility <- utilMessage.utilities.keys){
        
        var combinedUtilities : Map[Int, Double] = Map[Int, Double]()
    	  for (value <- 1 to TIMESLOTS){
    		
          var localUtility : Double = 0
          if(singleUtility == MEETING_ID){
            var localUtilities = utilities.apply(singleUtility)
        		if(localUtilities.contains(value)){
        			localUtility = localUtilities.get(value).get
        		}
          }
    	  
          var messageValueUtility : Double = 0
          var messageUtilities : Map[Int, Double] = utilMessage.getUtilities.get(singleUtility).get
      		if(messageUtilities.contains(value)){
    			  messageValueUtility = messageUtilities.get(value).get
      		}
        
        combinedUtilities += (value -> (localUtility + messageValueUtility))
    	 }
       utilities += (singleUtility -> combinedUtilities)
	   }
    }
    
	}
	
  /**
   * Choose values with highest utility
   */
	def chooseOptimal() = {
    
    if(!converged){
    
      /**
       *  Root Node
       */
      if(isRoot){
        
        computeUtils()
        var localValue = findMaxValue(utilities.get(MEETING_ID).get)
        registerValue(localValue)
        values.put(MEETING_ID, localValue)
        
      }
      /**
       * Middle, Leaf Nodes
       */
      else {
        
        var maxValue = -1
        if(values.contains(MEETING_ID)){
          maxValue = values.get(MEETING_ID).get
          registerValue(maxValue)
          values.put(MEETING_ID, maxValue)
        }
        else {
          maxValue = findMaxValue(utilities.get(MEETING_ID).get)
          computeUtils()
        }
        registerValue(maxValue)
        values.put(MEETING_ID, maxValue)
      }
   }
	}
  
  /**
   * Helper functions
   */
  def isLeaf() : Boolean = children.size == 0 && id.toString().contains("a")
  def isRoot() : Boolean = parent == null
  
  /**
   * Collect signals
   */
	def collect() = {
    
    newRound()
    
    // Check if finished
    if(initialized && !converged){
      var isFinished = true
      for(child <- children){
        if(!child.converged) isFinished = false
      }
      if(isFinished){
        convergenceCheck()
      }
    } 
    
    // Initialize
    if(!initialized){
      try {
        value = CONSTRAINTS_ORIGINAL.preferred.apply(MEETING_ID)
        AGENT_INDEX += (MEETING_ID -> value)
        MEETING_INDEX += (AGENT_ID -> value)
      } catch {
        case e : Exception => "Initialization fail"
      }
      initialized = true
    }
	  
		/**
     * Process messages 
     */
     for (signal <- signals.iterator) {
				      
       var message : DPOPMessage = signal.asInstanceOf[DPOPMessage]
              
       if(message != null){
         
         // Util messages
         if(!isLeaf){
				   if(children.contains(message.sender)){
					   utilMessages += message
					 }
         }
				
         // Value messages
         if(!isRoot){
           if(parent == message.sender){
					   valueMessages += message
				   }
         }
       }
     }
		
    /**
     * UTIL message propagation
     */
		
    // Leaf Node: Create Util
		if(isLeaf){
		  computeUtils()
		}
		else {
      
      // Check if util message from all children have arrived
  		if(utilMessages.size == children.size){
        
  			 // Node is root: value message to all children
  			 if(parent == null){
  			   chooseOptimal()
  			 }
  
         // Node is not root: Meeting Node, Util Message to parent
  		   else {
  			   computeUtils()
  			 }
          
			}
    }
    utilMessages.clear()
    
    /**
     * VALUE message propagation
     */

			// Check if value messages have arrived
			if(valueMessages.size > 0){
        
        // Add to agent view
        for(valueMessage <- valueMessages){
            values = valueMessage.values
        }

        // Choose the best value
        chooseOptimal()
          
         // store curent utility
         storeAgentUtility()
    
         valueMessages.clear()
		  }
    
     /**
      * MESSAGE: contains both value and utilities; parent reads util, children read values
      */
     new DPOPMessage(this, utilities, values)
     
	}
}