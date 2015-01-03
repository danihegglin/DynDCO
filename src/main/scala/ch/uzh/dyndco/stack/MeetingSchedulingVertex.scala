package ch.uzh.dyndco.stack

import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.problems.MeetingConstraints
import scala.collection.mutable.MutableList
import scala.util.Random
import ch.uzh.dyndco.util.Monitoring

abstract class MeetingSchedulingVertex (id: Any, initialState: Any) 
  extends MonitoredVertex(id, initialState) {
  
  /**
   * Configuration
   */
    final var HARD_UTILITY_N : Double = 0
    final var SOFT_UTILITY_N : Double = 0.75
    final var PREF_UTILITY_N : Double = 1
    
    var TIMESLOTS : Int = -1
    var CONSTRAINTS_ORIGINAL : MeetingConstraints = null
    var CONSTRAINTS_CURRENT : MeetingConstraints = null
    var MEETING_INDEX : Map[Any, Int] = null // monitors values for specific meetings -> needs to be the same
    var AGENT_INDEX : Map[Any, Int] = null // monitors values chosen by user -> needs to be different
    var MEETING_ID : Int = -1
    var AGENT_ID : Int = -1
    
  /**
   * Meeting Value
   */
  var value : Int = -1 // The currently choosen value for the meeting
  
  /**
   * Control parameters
   */
  var initialized : Boolean = false
    
  /**
     * Adjusted Finish Control: 
     * Finish when all participants have same timeslot for meeting
     */
    override def finishedCheck() = {
    
    // Check Meeting Index
    if(MEETING_INDEX.size > 0){
      var same : Boolean = true
      var refValue : Int  = MEETING_INDEX.values.toList(0)
      for(value <- MEETING_INDEX.values){
        if(value != refValue)
          same = false
      }
      
      // Check Agent Index
      if(AGENT_INDEX.size > 0){
        
        var different : Boolean = true
        refValue = -1
        for(value <- AGENT_INDEX.values){
          if(value == refValue)
            different = false
            
          refValue = value
        }
        
        if(same && different){
          finished = true
        }
//        else 
//          println(id + " -> " + MEETING_INDEX + " -> " + AGENT_INDEX)
      }
    }
      
    // Check Max Round      
    if(roundCount >= MAX_ROUND){
      finished = true
    }
          
  }  
    
  /**
   * Calculate Utility for current Best Value Assignment for original constraints
   */
  def calculateSingleUtility(constraints : MeetingConstraints, value : Int): Double = {
    
    var utility : Double = 0
        
    // timeslot utility
    utility += (TIMESLOTS - (value - 1)) / TIMESLOTS  
        
    // calendar utility
    if(CONSTRAINTS_CURRENT.hard.contains(value)){
      utility += HARD_UTILITY_N
    }
    else if (CONSTRAINTS_CURRENT.soft.contains(value)){
      utility += SOFT_UTILITY_N
    }
    else if (CONSTRAINTS_CURRENT.preference.values.toList.contains(value)){
      utility += PREF_UTILITY_N
    }
        
    // agent index utility
    for(meeting <- AGENT_INDEX.keys){
      if(meeting != MEETING_ID){
        if(AGENT_INDEX.contains(meeting)){
           if(AGENT_INDEX.apply(meeting) == value){
              utility = 0
           }
        }
      }
    }
    
    utility
  }
  
  /**
   * Calculate Utilities from Constraints
   */
  def calculateAllUtilities(constraints : MeetingConstraints): Map[Int, Double] = {
     
    var utilities = Map[Int, Double]()
    
    if(constraints != null){
      for (value <- 1 to TIMESLOTS){ 
        utilities += (value -> calculateSingleUtility(constraints, value))
      }
    }
     
    utilities
  }
  
  /**
   * Find best value
   */
  var history = Map[Int,Int]()
  
  def findMaxValue(utilities : Map[Int, Double]) : Int = {
      
      var orderedUtilities = utilities.toList sortBy {_._2}
      orderedUtilities = orderedUtilities.reverse
      
    

      // build buckets
//      var lastValue : Double = -1
//      var bucketCount : Int = 0
//      var buckets : Map[Int, MutableList[Int]] = Map[Int,MutableList[Int]]()
//      var list : MutableList[Int] = MutableList[Int]()
//      var count : Int = 0
      
//      for(utility <- orderedUtilities){
        
//        count += 1
//        
//        if(lastValue < 0){
//          lastValue = utility._2
//        }
//        if(utility._2 != lastValue || count == orderedUtilities.size){
//          val finalSet = list.sortWith(_ < _) // smallest timeslot first
//          buckets += (bucketCount -> finalSet)
//          bucketCount += 1
//          list = MutableList[Int]()
//          lastValue = utility._2
//        }
//        list += utility._1
//      }
      
      // check validity
      var accepted : Boolean = false
//      var position_top : Int = 0
//      var position_sub : Int = 0
      
      var position = 0
      
      var maxRounds = 10
      var roundCount = 0
      
      while(!accepted && roundCount < maxRounds){
        
        roundCount += 1
        
        // Retrieve List
//        var curList : MutableList[Int] = MutableList[Int]()
//        var assignment : Int = -1
        
//        try {
//          curList = buckets.apply(position_top)
          var assignment = orderedUtilities(position)
//        } catch {
//           case e : Exception => 
             //println("BUCKET ERROR: " + position_top + " | " + position_sub + " | " + buckets + " | " + curList.length)
//        }
        
        var conflict : Boolean = false
        
        // index check
        for(meeting <- AGENT_INDEX.keys){
          if(meeting != MEETING_ID){
             if(AGENT_INDEX.apply(meeting) == assignment._1){               
//               if(Random.nextDouble() > 0.25)
               conflict = true
             }
           }
        }
        
        // history check FIXME test
        if(history.contains(assignment._1)){
          if(history.apply(assignment._1) > 10){
            conflict = true
            history += (assignment._1 -> 0)
          }
        }
      
        if(!conflict){
            AGENT_INDEX += (MEETING_ID -> assignment._1)
            MEETING_INDEX += (AGENT_ID -> assignment._1)
            value = assignment._1
            CONSTRAINTS_CURRENT.update(MEETING_ID, assignment._1)
            accepted = true
            
            if(history.contains(assignment._1))
            history += (assignment._1 -> (history.apply(assignment._1) + 1))
            else
              history += assignment._1 -> 1
        }
        else {
          
          if((position + 1) > (orderedUtilities.size - 1)){
            position = 0
          }
          else {
            if(Random.nextDouble() > 0.25)
            position += 1
          }
          
//           if(curList.length > (position_sub + 1)){
//               position_sub += 1          
//           }
//           else {
//              if(buckets.size > (position_top + 1)){
//                position_top += 1
//              }
//              else {
//                position_top = 0
//              }
//              position_sub = 0
//              bucketHistory.clear()
//           }
        }
      }
    
    value
  }
  
//  def findBestValueTwo(utilities : Map[Int, Double]) : Int = {
//    
//      // Take value with highest utility
//      var highestUtility = 0.0
//      var optimalChoice : Int = -1
//      var best = MutableList[Int]()
//      var bestValue : Int = -1
//      
//      utilities.foreach {
//        keyVal =>
//          
//          var isBlocked = false
//          
//          var key = keyVal._1
//          var value = keyVal._2
//          
//          for(blocked <- AGENT_INDEX.values) {
//             if(key == blocked) {
//                value -= 1
//               isBlocked = true
//             }
//          }
//////          println(key + " -> " + isBlocked)
////          
////          if(!isBlocked){
//            if(value == highestUtility){
//              best += key
//            }
//            if(value > highestUtility){
//              best.clear
//              best += key
//              highestUtility = value
//            }
////          }
//     }
//      
////     if(best.size > 0){
//     
//       bestValue = best.sorted.get(0).get
////       lastGain = utilities.apply(lastValue) - lastGain
//       
//       // Update indices
//       MEETING_INDEX += (AGENT_ID -> bestValue)
//       AGENT_INDEX += (MEETING_ID -> bestValue)
//       
//       bestValue
////     }
//    
//  }
  
  /**
    * Utility Message Control
    */
   var messages = Map[String, String]()
   
   def storeMessage(utility : Double){
     
      var utility = calculateSingleUtility(CONSTRAINTS_ORIGINAL, value)
      var message = utility+ ";" + AGENT_INDEX + ";" + MEETING_INDEX
              
      // add current utility to messages
      val timestamp: Long = System.currentTimeMillis
      messages += timestamp.toString() -> message
              
      // Send if reached max
      if(cycleCount == PUSH_ROUND){
  //     println("push: " + cycleCount + " | " + id + " | " + PUSH_ROUND + " | " + messages.size)
         Monitoring.update(id, messages)
         messages.clear()
         cycleCount = 0
      }
   }

}