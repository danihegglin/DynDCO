package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.DataGraphVertex
import dispatch._, Defaults._
import scala.collection.mutable.MutableList
import scala.util.Random
import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.problems.Constraints
import collection.mutable.Map
import collection.mutable.Set

class DPOPVertex (
    id: Any, 
    agentView: DPOPMessage,
    timeslots: Int,
    constraints : Constraints,
    index : Map[Int, Map[Any, Int]]
    ) extends DynamicVertex(id, agentView, timeslots, constraints, null){ // FIXME
  
  // -------------------------- TERMINATION CRITERION -----------------------------------------
  /**
   * Finish boolean
   */
//	var finished : Boolean = false
//
//	override def scoreSignal: Double = {
//		if(this.finished){ 0}
//		else {1}
//	}
	
	/**
	 * Parent / Child relationships
	 */
	var parent : DPOPVertex = null
	var children : MutableList[DPOPVertex] = MutableList()
	
	/**
	 * Config
	 */
	final var HARD_UTILITY : Double = -10
	final var SOFT_UTILITY : Double = 0
	final var PREF_UTILITY : Double = 10
	
	/**
	 * The Utility
	 */
//	var localUtility : Double = 0.0
	
	/**
	 * Full map of values and their utility at this node
	 */
	var utilValueMap : Map[Int,Double] = Map[Int, Double]()
	var optimalChoice : Int = 0
	
	/**
	 * Message containers
	 */
	var utilMessages : MutableList[DPOPMessage] = MutableList()
	var valueMessages : MutableList[DPOPMessage] = MutableList()

	//-------------------------- FUNCTIONS ---------------------------------------------------

	def addParent(_parent : DPOPVertex) = {parent = _parent}
	def addChild(_child : DPOPVertex) = {children += _child}
	
	def computeUtils() = {
    
    utilValueMap.clear()
	  
	  // Initialize map
    if(constraints != null){
    	for (value <- 1 to timeslots){
    		if(constraints.hard.contains(value)){
    			utilValueMap += (value -> HARD_UTILITY)
    		}
    		else if (constraints.soft.contains(value)){
    			utilValueMap += (value -> SOFT_UTILITY)
    		}
    		else if (constraints.preference.keys.toList.contains(value)){
    			utilValueMap += (value -> PREF_UTILITY)
    		}
    	}
    }

    // Merge map with util messages
    for(utilMessage <- utilMessages){
    	for (value <- 1 to timeslots){
    		var localValueUtility : Double = 0
    		if(utilValueMap.contains(value)){
    			localValueUtility = utilValueMap.get(value).get
    		}
    	  var messageValueUtility : Double = 0
    		if(utilMessage.getUtilValueMap.contains(value)){
    			messageValueUtility = utilMessage.getUtilValueMap.get(value).get
    		}
    		utilValueMap += (value -> (localValueUtility + messageValueUtility)) 
    	}
	 }
//	  println(utilValueMap)
	}
	
	def chooseOptimal() = {
	  
	  // Take value with highest utility
	  var highestUtility = 0.0
	  utilValueMap.foreach {
	    keyVal =>
	    if(keyVal._2 > highestUtility){
	      optimalChoice = keyVal._1
	      highestUtility = keyVal._2
	    }
	  }
	  
	  println("optimal choice: " + optimalChoice + " / " + highestUtility)
	  
	  // Push current utility to monitoring
//	  System.out.println(id + ": utility - " + localUtility)
//	  val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + localUtility)
//	  val result = Http(svc OK as.String)
	  
	}
	
				def show() = {
		println("variable " + id)
		println("--------------")
//		println("hc:" + hardConstraints.toString())
//		println("sc:" + softConstraints.toString())
//		println("pf:" + preferences.toString())
		println("--------------")
				}
	
	def collect() = {
	  
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
		  finalMessage = new DPOPMessage(id, 0, utilValueMap, "Util")
		}
		else {
      
//      println("utilMessage: " + utilMessages.size)
//      println("children: " + children.size)
		  
		  // Check if util message from all children have arrived
			if(utilMessages.size == children.size){
        
//        println("boom")
        
				// Node is root -> Value Message to all children
				if(parent == null){
//				  System.out.println(id + ": Parent Node, Root, choosingOptimal")
				  chooseOptimal()
				  finalMessage = new DPOPMessage(id, 0, utilValueMap, "Value")
				}
				// Node is not root -> Meeting Node, Util Message to parent
				else {
				  computeUtils()
				  finalMessage = new DPOPMessage(id, 0, utilValueMap, "Util")
				}
        
        utilMessages.clear()
			}

			// Check if value messages have arrived
			if(valueMessages.size > 0){
//		    System.out.println(id + ": Process Value Messages, chooseOptimal")
				chooseOptimal()
		    finalMessage = new DPOPMessage(id, 0, utilValueMap, "Value")
        valueMessages.clear()
		  }
		}
		
    // Send final message
    finalMessage
	}
}