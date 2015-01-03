package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.DataGraphVertex
import dispatch._, Defaults._
import scala.collection.mutable.MutableList
import scala.util.Random
import ch.uzh.dyndco.stack.DynamicVertex
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
	 * The Utility
	 */
//	var localUtility : Double = 0.0
	
	/**
	 * Full map of values and their utility at this node
	 */
	var utilities : Map[Int,Double] = Map[Int, Double]()
	
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
    
	  // Initialize map
    utilities = calculateAllUtilities(CONSTRAINTS_CURRENT)

    // Merge map with util messages
    for(utilMessage <- utilMessages){
    	for (value <- 1 to TIMESLOTS){
    		var localValueUtility : Double = 0
    		if(utilities.contains(value)){
    			localValueUtility = utilities.get(value).get
    		}
    	  var messageValueUtility : Double = 0
    		if(utilMessage.getUtilValueMap.contains(value)){
    			messageValueUtility = utilMessage.getUtilValueMap.get(value).get
    		}
    		utilities += (value -> (localValueUtility + messageValueUtility)) 
    	}
	 }
    
	}
	
  /**
   * Choose value with highest utility
   */
	def chooseOptimal() = {
	  
	  // Take value with highest utility
//	  var highestUtility = 0.0
//	  utilities.foreach {
//	    keyVal =>
//	    if(keyVal._2 > highestUtility){
//	      value = keyVal._1
//	      highestUtility = keyVal._2
//	    }
//	  }
	  
//	  println("optimal choice: " + value + " / " + highestUtility)
    
    findMaxValue(utilities)
	  
	}
  
  /**
   * Collect signals
   */
	def collect() = {
    
    newRound()
	  
	  var finalMessage : DPOPMessage = null
	  
		// Message Type
		for (signal <- signals.iterator) {
			try {
				var message : DPOPMessage = signal.asInstanceOf[DPOPMessage]
						if(message.getMessageType == "Util"){
							if(message.sender != parent.id){
								utilMessages += message
//										System.out.println(id + ": Util message received");
							}
						}
						else if (message.getMessageType == "Value"){
							if(message.sender == parent.id){ // FIXME
								valueMessages += message
//										System.out.println(id + ": Value message received");
							}
						}
			}
			catch {
			  case e : Exception => 
//          println(id + ": signal was null")
			}
		}
		
		// Leaf Node: Create Util
		if(children.size == 0){
		  computeUtils()
		  finalMessage = new DPOPMessage(id, 0, utilities, "Util")
		}
		else {
      
		  // Check if util message from all children have arrived
			if(utilMessages.size == children.size){
        
				// Node is root -> Value Message to all children
				if(parent == null){
				  chooseOptimal()
				  finalMessage = new DPOPMessage(id, 0, utilities, "Value")
				}

        // Node is not root -> Meeting Node, Util Message to parent
				else {
				  computeUtils()
				  finalMessage = new DPOPMessage(id, 0, utilities, "Util")
				}
        
        utilMessages.clear()
			}

			// Check if value messages have arrived
			if(valueMessages.size > 0){
				chooseOptimal()
		    finalMessage = new DPOPMessage(id, 0, utilities, "Value")
        valueMessages.clear()
		  }
		}
    
    // store values of current round
    storeMessage(value)
		
    // Send final message
    finalMessage
	}
}