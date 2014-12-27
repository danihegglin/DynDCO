package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Constraints
import collection.mutable.Map
import collection.mutable.Set

abstract class MeetingSchedulingVertex (id: Any, initialState: Any) 
  extends MonitoredVertex(id, initialState) {
  
  /**
   * Configuration
   */
    final var HARD_UTILITY_N : Double = 0
    final var SOFT_UTILITY_N : Double = 0.75
    final var PREF_UTILITY_N : Double = 1
    
    var TIMESLOTS : Int = -1
    var CONSTRAINTS_ORIGINAL : Constraints = null
    var CONSTRAINTS_CURRENT : Constraints = null
    var MEETING_INDEX : Map[Any, Int] = null // monitors values for specific meetings -> needs to be the same
    var AGENT_INDEX : Map[Any, Int] = null // monitors values chosen by user -> needs to be different
    var MEETING_ID : Int = -1
    
   /**
    * Constraints
    */
//    var constraints = constraints_
//    var originalConstraints = constraints
    
  /**
     * Adjusted Finish Control: 
     * Finish when all participants have same timeslot for meeting
     */
    override def finishedCheck() = {
    
    // Check Index
      var same : Boolean = true
      var refValue : Int  = MEETING_INDEX.values.toList(0)
      for(value <- MEETING_INDEX.values){
        if(value != refValue)
          same = false
      }
      if(same){
        finished = true
      }
      
    // Check Max Round      
    if(roundCount >= MAX_ROUND){
      finished = true
    }
          
  }  
    
  /**
   * Calculate Utilities for current Best Value Assignment
   */
  def calculateLocalUtility(bestValueAssignment : Int): Double = {
    var utility : Double = PREF_UTILITY_N
    if(CONSTRAINTS_ORIGINAL.hard.contains(bestValueAssignment)){
      utility = HARD_UTILITY_N
    }
    else if(CONSTRAINTS_ORIGINAL.soft.contains(bestValueAssignment)){
      utility = SOFT_UTILITY_N
    }
    utility
  }
  
  /**
   * Calculate Utilities from Constraints
   */
  def calculateOriginUtilities() : Map[Any, Map[Int, Double]] = {
     var utilValueMap = Map[Int, Double]()
      for (value <- 1 to TIMESLOTS){
        if(CONSTRAINTS_CURRENT.hard.contains(value)){
          utilValueMap += (value -> HARD_UTILITY_N)
        }
        else if (CONSTRAINTS_CURRENT.soft.contains(value)){
          utilValueMap += (value -> SOFT_UTILITY_N)
        }
        else if (CONSTRAINTS_CURRENT.preference.values.toList.contains(value)){
          utilValueMap += (value -> PREF_UTILITY_N)
        }
      }
     
     var finalUtilities = Map[Any, Map[Int, Double]]()
     finalUtilities += (id -> utilValueMap)
     finalUtilities
  }

}