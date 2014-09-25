package ch.uzh.dyndco.algorithms.dco.complete.dpop

import com.signalcollect.DataGraphVertex
import dispatch._, Defaults._
import scala.collection.mutable.MutableList
import scala.util.Random

class DPOPVertex (
    id: Any, 
    agentView: DPOPMessage,
    numTimeslots: Int
    ) extends DataGraphVertex(id, agentView){
  
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
	
	//-------------------------- VARIABLE ----------------------------------------------------
	var parent : DPOPVertex = null
	var children : MutableList[DPOPVertex] = MutableList()
//	var pparent : DPOPVertex = null
//	var pchildren : List[DPOPVertex] = null
	
	/**
	 * Local value and utility
	 */
	var localUtility : Double = 0.0
	var localValue : Double = Random.nextInt(numTimeslots)
	
	/**
	 * Full map of values and their utility at this node
	 */
	var utilValueMap : scala.collection.mutable.Map[Int,Double] = scala.collection.mutable.Map()
	
	/**
	 * Message containers
	 */
	var utilMessages : MutableList[DPOPMessage] = MutableList()
	var valueMessages : MutableList[DPOPMessage] = MutableList()

	//-------------------------- FUNCTIONS ---------------------------------------------------

	def addParent(_parent : DPOPVertex) = {parent = _parent}
	def addChild(_child : DPOPVertex) = {children += _child}
//	def addPParent(_pparent : DPOPVertex) = {pparent = _pparent}
//	def addPChildren(_pchildren : List[DPOPVertex]) = {pchildren = _pchildren}
	
	def computeUtils() : Double = {
	  
	  // Initialize map
	  if(utilMessages.size == 0){
	    for (value <- 1 to numTimeslots){
			  if(value == localValue){
				  utilValueMap += (value -> 1.0)
				  this.localUtility = 1.0
			  }
			  else{
				  utilValueMap += (value -> 0.0)
			  }
		  }
	  }
	  
	  // Merge map with util messages
	  else {
		  // Process all utilmessages
		  for(utilMessage <- utilMessages){
			  // Calculate usefulness of all values // FIXME völlig falsch
			  for (value <- 1 to numTimeslots){
			  
				  var localMapUtility = utilValueMap.get(value)
				  var messageMapUtility = utilMessage.getUtilValueMap.get(value)
			  
				  utilValueMap += (value -> localMapUtility + messageMapUtility)
			  
				  if(value == localValue){
					  this.localUtility = localMapUtility + messageMapUtility
				  }
			  }
	  	  }
	  }
	  
	  this.localUtility
	}
	
	def chooseOptimal() : Double = {
	  
	  // Take value with highest utility
	  var highestUtility = 0.0
	  utilValueMap.foreach {
	    keyVal =>
	    if(keyVal._2 > highestUtility){
	      this.localValue = keyVal._1
	      this.localUtility = keyVal._2
	      highestUtility = keyVal._2
	    }
	  }
	  
	  // Push current utility to monitoring
	  System.out.println(id + ": utility - " + localUtility)
	  val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + localUtility)
	  val result = Http(svc OK as.String)
	  
	  this.localValue
	}
	
	//-------------------------- COLLECT -----------------------------------------------------

	def collect() = {
	  
		// Message Type
		for (signal <- signals.iterator) {
		  var message : DPOPMessage = signal.asInstanceOf[DPOPMessage]  
		  if(message != null && message.getMessageType != null){
			  if(message.getMessageType == "Util"){
			    utilMessages += message
			    System.out.println(id + ": Util message received");
			  }
			  else if (message.getMessageType == "Value"){
			    valueMessages += message
			    System.out.println(id + ": Value message received");
			  }
		  }
		}
		
		// Leaf Node: Create Util
		if(children == null){
		  System.out.println(id + ": Leaf Node, computeUtils");
		  this.localUtility = computeUtils()
		  new DPOPMessage(this.utilValueMap, "Util")
		}
		else {
			// Check if util message from all children have arrived
			if(utilMessages.size == children.size){
			  
				// Node is root -> Value Message to all children
				if(parent == null){
				  System.out.println(id + ": Parent Node, Root, choosingOptimal")
				  this.localValue = chooseOptimal()
				  new DPOPMessage(this.localValue, "Value")
				}
				// Node is not root -> Util Message to parent
				else {
				  System.out.println(id + ": Parent Node, computeUtils")
				  localUtility = computeUtils()
				  new DPOPMessage(this.utilValueMap, "Util")
				}
			}
		}
			
		// Check if value messages have arrived FIXME not completely correct
		if(valueMessages.size > 0){
		  System.out.println(id + ": Process Value Messages, chooseOptimal")
		  var newValue = chooseOptimal()
		  if(newValue == localValue){
		    finished = true
		  }
		  new DPOPMessage(newValue, "Value")
		}
		else {
		   System.out.println(id + ": Send Value to children")
		  // FIXME not in the algorithm
		   new DPOPMessage(localValue, "Value")
		}
	}
}