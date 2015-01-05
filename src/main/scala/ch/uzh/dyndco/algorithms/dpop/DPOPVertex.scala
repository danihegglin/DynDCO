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
//  var values = Map[Int,Int]()
	
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
    
    println("leaf: PRECALC -> " + utilities)

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
    
    /**
     * FIXME needs to handle multiple meetings in root
     */
    findMaxValue(utilities)
    
    // update values FIXME 
    
	}
  
  /**
   * Helper functions
   */
  def isLeaf() : Boolean = children.size == 0
  
  /**
   * Collect signals
   */
	def collect() = {
    
    newRound()
	  
		/**
     * Process messages 
     */
		for (signal <- signals.iterator) {
			try {
				var message : DPOPMessage = signal.asInstanceOf[DPOPMessage]
						if(children.contains(message.sender)){
								utilMessages += message
						}
						else if(parent == message.sender){
								valueMessages += message
						}
			}
			catch {
			  case e : Exception => 
          //println(id + ": signal was null")
			}
		}
		
    /**
     * UTIL message propagation
     */
		
    // Leaf Node: Create Util
		if(isLeaf){
		  computeUtils()
      println("leaf:" + utilities)
		}
		else {
      
      // Check if util message from all children have arrived
  		if(utilMessages.size == children.size){
        
  			 // Node is root: value message to all children
  			 if(parent == null){
           computeUtils() // not in the paper but should be here
  			   chooseOptimal()
           println("root: " + utilities)
  			 }
  
         // Node is not root: Meeting Node, Util Message to parent
  		   else {
  			   computeUtils()
           println("middle: " + utilities)
  			 }
          
			}
    }
    utilMessages.clear()
//    
//    /**
//     * VALUE message propagation
//     */
//
//			// Check if value messages have arrived
			if(valueMessages.size > 0){
//				chooseOptimal()
//        
//         if(isLeaf){
//           
//           println("leaf: " + value)
//            // take value for this meeting id
//            
//            // store curent utility
//            storeAgentUtility()
//         }
//         else {
//           println("middle: " + value)
//         }
    
         valueMessages.clear()
		  }
     
      
     /**
      * FINAL message: contains both; parent reads util, children read values
      */
     new DPOPMessage(this, utilities, value)
     
	}
}