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
	 * Config
	 */
	final var HARD_UTILITY : Double = -10
	final var SOFT_UTILITY : Double = 0
	final var PREF_UTILITY : Double = 10
  
    /**
     * Control parameters
     */
    var initialized : Boolean = false
    var finished : Boolean = false
    
    /**
     * Values
     */
    var values : Map[Any,Int] = Map[Any,Int]()
    var lastGain : Double = 0.0
    var lastValue : Int = -1
    
  /**
   * Score signal function
   */
  override def scoreSignal: Double = {
    if(this.finished) 0
    else 1
  }

	def collect() = {
    
    var outgoing : MGMMessage = null
	  
	  if(!initialized){
      
      initialized = true
	    
	    // send current preference for meeting
	    var preferences = constraints.preference
      var meetingID : Int = id.toString().substring(3).toInt
	    var meetingPref : Int = preferences.apply(meetingID)
	    
	    // build ordered list of best timeslots // FIXME too extensive
      var utilValueMap : Map[Int,Double] = Map[Int, Double]()
      for (value <- 1 to timeslots){
    		if(constraints.hard.contains(value)){
    			utilValueMap += (value -> HARD_UTILITY)
    		}
    		else if (constraints.soft.contains(value)){
    			utilValueMap += (value -> SOFT_UTILITY)
    		}
    		else if (constraints.preference.values.toList.contains(value)){
    			utilValueMap += (value -> PREF_UTILITY)
    		}
    	}
      
      println(utilValueMap)
      
      // initialize local value & gain
	    lastGain = utilValueMap.apply(meetingPref)
      lastValue = meetingPref
      println("initialGain: " + lastGain)
      println("initialValue: " + lastValue)
      
      // add pref to values
      values += id -> meetingPref
	    
	    outgoing = new MGMMessage(id, meetingPref)
	    
	  }
	  else{
      
      // determine if finished - needs to be before
      if(values.size > 1){
        var same = true
        var preferences = values.values.toList
        println(id + " - first v: " + preferences(0))
        println(id + " - values: " + values.values)
        for(pref <- values.values){
          if(pref != preferences(0)){
            same = false
          }
        }
        if(same){
          println(id + " - has finished")
          finished = true
        }
      }
	    
      // process messages
      var gains : Map[Any, Double] = Map[Any, Double]()
      for(signal <- signals.iterator){
        var message = signal.asInstanceOf[MGMMessage]
        
        println("received message: " + message.messageType)
        
  	    if(message.messageType == "value"){
  	        values += (message.sender -> message.value)
            println(id + " - Value received - " + values)
  	      }
    	  if(message.messageType == "gain"){
            gains += (message.sender -> message.value)
         }
      }
      
      var messageCreated : Boolean = false
      
      // Determine biggest gain
      if(gains.size > 0){
        var hasBestGain = true
        for(gain <- gains.values){
          if(gain > lastGain){
            hasBestGain = false
          }
        }
        if(hasBestGain == true){
          println(id + " - has best gain, sending value: " + lastValue)
          // Update values
          values += (id -> lastValue)
          outgoing = new MGMMessage(id, lastValue)
          messageCreated = true
        }
      }
      
      // choose assignment with maximal local gain, send gain, store gain
      if(messageCreated == false){
        chooseMaximalGain()
        outgoing = new MGMMessage(id, lastGain)
      }
	  }
    
    outgoing
	}
    
    def chooseMaximalGain() = {
      
      var maxGain : Double = 0.0
      
      // build ordered list of best timeslots
      var utilValueMap : Map[Int,Double] = Map[Int, Double]()
      for (value <- 1 to timeslots){
    		if(constraints.hard.contains(value)){
    			utilValueMap += (value -> HARD_UTILITY)
    		}
    		else if (constraints.soft.contains(value)){
    			utilValueMap += (value -> SOFT_UTILITY)
    		}
    		else if (constraints.preference.values.toList.contains(value)){
    			utilValueMap += (value -> PREF_UTILITY)
    		}
    	}
      
      // remove all which are currently used
      for(blocked <- index.values){
        println(blocked)
        utilValueMap.remove(blocked)
      }
      
      // add agents -> utility increase 10
      for(favorized <- values.values){
        var utility = utilValueMap.apply(favorized)
        utility += 10 // FIXME make constant
        utilValueMap += (favorized -> utility)
      }
      
      // Take value with highest utility
	  var highestUtility = 0.0
	  var optimalChoice : Int = -1
	  utilValueMap.foreach {
	    keyVal =>
	    if(keyVal._2 > highestUtility){
	      lastValue = keyVal._1
	      lastGain = keyVal._2 - lastGain
	    }
	  }
    
    println(id + " - lastGain" + lastGain)
    println(id + " - lastValue" + lastValue)
  }
    
}

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