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
    constraints : Constraints
    ) extends DynamicVertex(id, agentView){
  
  // -------------------------- TERMINATION CRITERION -----------------------------------------
  /**
   * Finish boolean
   */
	var finished : Boolean = false

	override def scoreSignal: Double = {
		if(this.finished) 
			0
		else
			1
     }
	
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
	  
	  // Initialize map
	  if(utilMessages.size == 0){
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
	  else {
		  // Process all utilmessages
		  for(utilMessage <- utilMessages){
			  // Calculate usefulness of all values
			  for (value <- 1 to timeslots){
			  
				  var localValueUtility : Double = utilValueMap.get(value).get
				  var messageValueUtility : Double = utilMessage.getUtilValueMap.get(value).get
			  
				  utilValueMap += (value -> (localValueUtility + messageValueUtility)) 
			  
//				  if(value == localValue){
//					  this.localUtility = localMapUtility + messageMapUtility FIXME
//				  }
			 }
	   }
	 }
	}
	
	def chooseOptimal() = {
	  
	  // Take value with highest utility
	  var highestUtility = 0.0
	  utilValueMap.foreach {
	    keyVal =>
	    if(keyVal._2 > highestUtility){
	      optimalChoice = keyVal._1
//	      this.localUtility = keyVal._2
	      highestUtility = keyVal._2
	    }
	  }
	  
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
	  
		// Message Type
		for (signal <- signals.iterator) {
		  var message : DPOPMessage = signal.asInstanceOf[DPOPMessage]  
		  if(message != null && message.getMessageType != null){
			  if(message.getMessageType == "Util"){
			    if(message.sender != parent.id){ // FIXME
			      utilMessages += message
			      System.out.println(id + ": Util message received");
			    }
			  }
			  else if (message.getMessageType == "Value"){
			    if(!children.contains(message.sender)){ // FIXME
			      valueMessages += message
			      System.out.println(id + ": Value message received");
			    }
			  }
		  }
		}
		
		// Leaf Node: Create Util
		if(children == null){
		  System.out.println(id + ": Leaf Node, computeUtils");
		  computeUtils()
		  new DPOPMessage(id, 0, utilValueMap, "Util")
		}
		else {
			// Check if util message from all children have arrived
			if(utilMessages.size == children.size){
			  
				// Node is root -> Value Message to all children
				if(parent == null){
				  System.out.println(id + ": Parent Node, Root, choosingOptimal")
				  chooseOptimal()
				  new DPOPMessage(id, 0, utilValueMap, "Value") // FIXME correct?
				}
				// Node is not root -> Util Message to parent
				else {
				  System.out.println(id + ": Parent Node, computeUtils")
				  computeUtils()
				  new DPOPMessage(id, 0, utilValueMap, "Util")
				}
			}
		}
			
		// Check if value messages have arrived FIXME not completely correct
		if(valueMessages.size > 0){
//		  System.out.println(id + ": Process Value Messages, chooseOptimal")
//		  var newValue : Int = 
		    chooseOptimal()
//		  if(newValue == optimalChoice){
//		    finished = true
//		  }
		  new DPOPMessage(id, 0, utilValueMap, "Value") // FIXME correct?
		}
//		else {
//		   System.out.println(id + ": Send Value to children")
//		  // FIXME not in the algorithm
//		   new DPOPMessage(localValue, "Value")
//		}
	}
}