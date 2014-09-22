package ch.uzh.dyndco.algorithms.dco.complete.dpop

import com.signalcollect.DataGraphVertex
import dispatch._, Defaults._

class DPOPVertex (
    id: Any, 
    value: Double, 
    numTimeslots: Int, 
    parent: DPOPVertex,
    children: List[DPOPVertex],
    pparent: List[DPOPVertex],
    pchildren: List[DPOPVertex]) extends DataGraphVertex(id, value){
  
  // -------------------------- TERMINATION CRITERION -----------------------------------------
  
  /**
   * Finish boolean
   */
	var finished : Boolean = false

	override def scoreSignal: Double = {
	//println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
		if(this.finished) 
			0
		else
			1
     }
	
	//-------------------------- VARIABLE ----------------------------------------------------
	var util : Double = 0.0
	var optimal : Double = 0.0

	//-------------------------- FUNCTIONS ---------------------------------------------------
	def computeUtils() : Double {
	  //????
	}
	
	def chooseOptimal() : Double {
	  //????
	}
	
	//-------------------------- COLLECT -----------------------------------------------------

	def collect() = {
	  
		if(value.)

		// Push current utility
		val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + utility)
		val result = Http(svc OK as.String)
  
		// Leaf Node: Create Util
		if(children.size == 0){
		  util = computeUtils()
		  util
		}
		// Parent Node: Collect Util form children
		else {
		  // Node is root
		  if(parent == null){
		    optimal = chooseOptimal()
		    optimal
		  }
		  // Node is not root
		  else {
		    util = computeUtils()
		    util
		  }
		}
  
  // Function Compute Utils
  
  // Send Message
  
  // FUNCTION Value Message propagation
  
  // FUNCTION Util Message Handler
  // FUNCTION Value Message Handler
  
  // FUNCTION choose optimal
  
  // Send Value to Children
  
  

}