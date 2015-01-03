package ch.uzh.dyndco.algorithms.mgm

import scala.collection.mutable.Map
import scala.collection.mutable.MutableList

 import ch.uzh.dyndco.stack.DynamicVertex

 class MGMVertex (id: Any, agentView: MGMMessage) 
   extends DynamicVertex(id, agentView){
  
  /**
   * This avoids type-checks/-casts.
   */
    type Signal = MGMMessage
  
    /**
     * Values
     */
    var values : Map[Any,Int] = Map[Any,Int]()
    var lastGain : Double = 0.0
    
    /**
     * Choose max gain
     */
   def chooseMaximalGain() = {
      
      if(!finished){
      
        var maxGain : Double = 0.0
        
        // build ordered list of best timeslots
        var utilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
        
        // add agents -> utility increase
        for(agent <- values.keys){
          if(agent != id){
            var favoriteValue = values.apply(agent)
            var utility = utilities.apply(favoriteValue)
            utility += 2.0
            utilities += (favoriteValue -> utility)
          }
        }
        
        // Take value with highest utility
        value = findMaxValue(utilities)
        lastGain = utilities.apply(value) - lastGain
      
      }
  }

	def collect() = {
    
    var outgoing : MGMMessage = null
	  
	  if(!initialized){
      
      initialized = true
	    
	    // send current preference for meeting
	    var preferences = CONSTRAINTS_CURRENT.preference
	    var meetingPref : Int = preferences.apply(MEETING_ID)
	    
	    // build ordered list of best timeslots
      var utilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
      
      // initialize local value & gain
	    lastGain = utilities.apply(meetingPref)
      value = meetingPref
      
      // add pref to values
      values += id -> meetingPref
	    
	    outgoing = new MGMMessage(id, meetingPref)
	    
	  }
	  else{
      
      finishedCheck()
	    
      // process messages
      var gains : Map[Any, Double] = Map[Any, Double]()
      for(signal <- signals.iterator){
        var message = signal.asInstanceOf[MGMMessage]
        
  	    if(message.messageType == "value"){
  	        values += (message.sender -> message.value)
  	      }
    	  if(message.messageType == "gain"){
            gains += (message.sender -> message.gain)
         }
      }
      
      var sendValueMessage : Boolean = false
      
//      println(id + ": last gain -> " + lastGain + " | GAINS: " + gains + " | VALUES: " + values)
      
      // Determine biggest gain
      if(gains.size > 0){
        var hasBestGain = true
        for(gain <- gains.values){
          if(gain > lastGain){
//            println(gain)
            hasBestGain = false
          }
        }
        if(hasBestGain == true){
          
//          println(id + " - has best gain, sending value: " + value)
          
          // Update values
          values += (id -> value)
          outgoing = new MGMMessage(id, value)
          sendValueMessage = true
                    
        }
      }

      // choose assignment with maximal local gain, send gain, store gain
      if(!sendValueMessage){
        chooseMaximalGain()
        outgoing = new MGMMessage(id, lastGain)
      }
    
	  }
    
    // calculate local utility
   agentUtility = calculateSingleUtility(CONSTRAINTS_ORIGINAL, value)
   storeUtility()
    
   outgoing
  }
}
