package ch.uzh.dyndco.algorithms.mgm

import scala.collection.mutable.Map
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.stack.vertex.DynamicVertex

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
        var maxValue = findMaxValue(utilities)
        registerValue(maxValue)
        lastGain = utilities.apply(value) - lastGain
      
      }
  }

	def collect() = {
    
    newRound()
    
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
      
      // Determine biggest gain
      if(gains.size > 0){
        var hasBestGain = true
        for(gain <- gains.values){
          if(gain > lastGain){
            hasBestGain = false
          }
        }
        if(hasBestGain == true){
          
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
    
    // store curent utility
    storeAgentUtility()
    
   outgoing
  }
}
