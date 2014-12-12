package ch.uzh.dyndco.algorithms.mgm

import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.problems.Constraints
import collection.mutable.Set
import collection.mutable.Map

 class MGMVertex (
    id: Any, 
    agentView: MGMMessage,
    timeslots: Int,
    constraints : Constraints,
    index : Map[Int, Int]
    ) extends DynamicVertex(id, agentView){
  
    /**
     * Control parameters
     */
    var initialized : Boolean = false
    
    /**
     * Values
     */
    var values : Map[Any,Int] = Map[Any,Int]()
    var lastGain : Double = 0.0
    var lastValue : Int = -1
//  
//  /• Send its current value ai
//to neighbors and receive values from neighbors.
//• Choose a value a
//∗
//i
//such that the local gain g
//∗
//i
//is maximised (assuming neighbors
//do not change value).
//• Send the gain g
//∗
//i
//to neighbors and receive gain from neighbors.
//• If the gain for the agent is the highest in the neighborhood, update the value
//of xi
//to a
//∗
//i
//.
    
    //  currentReward 5 u(s 5 currentState, sv) 1
//for k 5 1:K 2
//stateGain(k) 5 u(s 5 k, sv) – currentReward 3
//end for 4
//bestGainState 5 argmax
//k
//½stateGain 5
//bestGainValue 5 stateGain(bestStateGain) 6
//sendBestGainMessage[allNeighbours, bestGainValue] 7
//neighbourGainValues 5 getNeighbourGainValues[allNeighbours] 8
//if bestGainValue . max[neighbourGain] then 9
//newState 5 bestGainState 10
//sendStateMessage[allNeighbours, newState] 11
//end if
  
	def collect() = {
	  
	  if(!initialized){
	    
	    // send current preference for meeting
	    var preferences = constraints.preference
	    var meetingPref : Int = preferences.apply(id.toString().substring(3).toInt)
	    initialized = true
	    new MGMMessage(id, meetingPref)
	    
	  }
	  else{
	    
		  var messages : Set[MGMMessage] = Set[MGMMessage]()
		  for (signal <- signals.iterator) {
			  messages += signal.asInstanceOf[MGMMessage]
		  }
		  
		  var message : MGMMessage = messages.toVector(0)
		  var messageType : String = message.getType()
	    
	    if(messageType == "value"){
	      println("received values")
	      for(message <- messages){
	        values += (message.sender -> message.value)
	      }
	    }
	    else if(messageType == "gain"){
	      println("received gain")
	      var hasBestGain = true
	      for(message <- messages){
	        if(message.gain > lastGain){
	          hasBestGain = false
	        }
	      }
	      if(hasBestGain == true){
	        new MGMMessage(id, lastValue)
	      }
  	      
  	    // choose assignment with maximal local gain, send gain, store gain
	      chooseMaximalGain()
	      new MGMMessage(id, lastGain)
	    }
	  }
	  
	}
    
    def chooseMaximalGain() = {
      
      // FIXME how?
      
      lastGain = 1
      lastValue = 1
    }
  
  

}