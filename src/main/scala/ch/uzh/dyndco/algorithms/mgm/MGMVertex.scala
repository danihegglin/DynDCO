package ch.uzh.dyndco.algorithms.mgm

import ch.uzh.dyndco.stack.DynamicVertex
import ch.uzh.dyndco.problems.Constraints
import collection.mutable.Set
import collection.mutable.Map
import scala.collection.mutable.SortedSet
import scala.collection.mutable.MutableList

 class MGMVertex (id: Any, agentView: MGMMessage) 
   extends DynamicVertex(id, agentView){
  
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
    
   def chooseMaximalGain() = {
      
      var maxGain : Double = 0.0
      
      // build ordered list of best timeslots
      var utilities = calculateCurrentUtilities()
      
      // add agents -> utility increase 10
      for(agent <- values.keys){
        if(agent != id){
          var favoriteValue = values.apply(agent)
          var utility = utilities.apply(favoriteValue)
          utility += 1.0 // FIXME make constant
          utilities += (favoriteValue -> utility)
        }
      }
      
      // Take value with highest utility
      var highestUtility = 0.0
      var optimalChoice : Int = -1
      var best = MutableList[Int]()
      
      utilities.foreach {
        keyVal =>
          
          var isBlocked = false
          
          var key = keyVal._1
          var value = keyVal._2
          
//          for(blocked <- AGENT_INDEX.values) {
//             if(key == blocked) {
//                value -= 10
//               isBlocked = true
//             }
//          }
////          println(key + " -> " + isBlocked)
//          
//          if(!isBlocked){
            if(value == highestUtility){
              best += key
            }
            if(value > highestUtility){
              best.clear
              best += key
              highestUtility = value
            }
//          }
     }
      
//     if(best.size > 0){
     
       lastValue = best.sorted.get(0).get
       lastGain = utilities.apply(lastValue) - lastGain
       
       // Update indices
       MEETING_INDEX += (id -> lastValue)
       AGENT_INDEX += (MEETING_ID -> lastValue)
//     }
          
  }

	def collect() = {
    
    var outgoing : MGMMessage = null
	  
	  if(!initialized){
      
      initialized = true
	    
	    // send current preference for meeting
	    var preferences = CONSTRAINTS_CURRENT.preference
	    var meetingPref : Int = preferences.apply(MEETING_ID)
	    
	    // build ordered list of best timeslots
      var utilities = calculateCurrentUtilities
      
      // initialize local value & gain
	    lastGain = utilities.apply(meetingPref)
      lastValue = meetingPref
      
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
          
//          println(id + " - has best gain, sending value: " + lastValue)
          
          // Update values
          values += (id -> lastValue)
          outgoing = new MGMMessage(id, lastValue)
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
   agentUtility = calculateOriginalUtility(lastValue)
   storeUtility()
    
   outgoing
  }
}
