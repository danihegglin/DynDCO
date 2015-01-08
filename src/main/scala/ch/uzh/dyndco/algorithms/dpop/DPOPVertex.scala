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
	var utilities = Map[Int, Double]()
  var values = Map[Any,Int]()
	
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
	
  /**
   * Calculates all utilities
   */
	def computeUtils() = {
    
	  // Create local utilities
    utilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
    
    // Merge map with util messages
    for(utilMessage <- utilMessages){
    	for (value <- 1 to TIMESLOTS){
    		
        var localUtility : Double = 0
    		if(utilities.contains(value)){
    			localUtility = utilities.get(value).get
    		}
    	  
        var messageValueUtility : Double = 0
    		if(utilMessage.getUtilities.contains(value)){
    			messageValueUtility = utilMessage.getUtilities.get(value).get
    		}
        
    		utilities += (value -> (localUtility + messageValueUtility))
    	}
	 }
    
	}
	
  /**
   * Choose values with highest utility
   */
	def chooseOptimal() = {
    
    if(!finished){
    
      if(isRoot){
        
        for(utilMessage <- utilMessages){
          
          var localUtilities = utilMessage.getUtilities
          var vertex : DPOPVertex = utilMessage.sender
          if(localUtilities != null){
            try {
              var localValue = findMaxValue(localUtilities)
              AGENT_INDEX.put(vertex.MEETING_ID, localValue)
              values.put(vertex, localValue)
            } catch {
              case e : Exception => 
                //e.printStackTrace()
            }
          }
          else {
            println("ERROR")
          }
        } 
        
        
      }
      else {
        
        for(valueMessage <- valueMessages){
            values = valueMessage.values
        }
        
        if(isLeaf){
          if(values.contains(this.parent)){
            var maxValue = values.get(parent).get
            registerValue(maxValue)
          }
        }
        
    }
   }
	}
  
  /**
   * Helper functions
   */
  def isLeaf() : Boolean = children.size == 0 && id.toString().size > 2
  def isRoot() : Boolean = parent == null
  
  /**
   * Collect signals
   */
	def collect() = {
    
    // Check if finished
    if(isLeaf && initialized && !finished){
      finishedCheck()
    } else {
      if(!isRoot && children.size > 0){
        var isFinished = true
        for(child <- children){
          if(!child.finished)
            isFinished = false
        }
        if(isFinished){
          finished = true
        }
      }
    }
    
    // Initialize
    if(!initialized){
      if(isLeaf){
        value = CONSTRAINTS_ORIGINAL.preference.apply(MEETING_ID)
        AGENT_INDEX += (MEETING_ID -> value)
        MEETING_INDEX += (AGENT_ID -> value)
      }
      initialized = true
    }
    
    newRound()
	  
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

        // Choose the best value
        chooseOptimal()
          
         // store curent utility
         if(isLeaf){
            storeAgentUtility()
         }
    
         valueMessages.clear()
		  }
     
      
     /**
      * FINAL message: contains both; parent reads util, children read values
      */
     new DPOPMessage(this, utilities, values)
     
	}
}