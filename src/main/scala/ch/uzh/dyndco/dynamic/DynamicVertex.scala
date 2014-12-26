package ch.uzh.dyndco.dynamic

import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.problems.Constraints
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.util.Monitoring

abstract class DynamicVertex (
		id: Any, 
		initialState: Any,
    valueSpace: Int,
    constraints_ : Constraints,
    meetingIndex : Map[Any, Int]
		) extends MeetingSchedulingVertex(id, initialState) {
  
  
  
//  /**
//   * Configuration
//   */
//    final var HARD_UTILITY_N : Double = 0
//    final var SOFT_UTILITY_N : Double = 0.75
//    final var PREF_UTILITY_N : Double = 1
    
    /**
     * Control Parameters
     */
    var finished : Boolean = false
    
    /**
     * Finish method
     */
    override def scoreSignal: Double = {
      if(this.finished) 0
      else 1
    }
    
//    /**
//     * Constraints
//     */
//    var constraints = constraints_
//    final var originalConstraints = constraints
  
//    /**
//   * Calculate Utilities for current Best Value Assignment
//   */
//  def calculateLocalUtility(bestValueAssignment : Int): Double = {
//    var utility : Double = PREF_UTILITY_N
//    if(originalConstraints.hard.contains(bestValueAssignment)){
//      utility = HARD_UTILITY_N
//    }
//    else if(originalConstraints.soft.contains(bestValueAssignment)){
//      utility = SOFT_UTILITY_N
//    }
//    utility
//  }
//  
//  /**
//   * Calculate Utilities from Constraints
//   */
//  def calculateOriginUtilities() : Map[Any, Map[Int, Double]] = {
//     var utilValueMap = Map[Int, Double]()
//      for (value <- 1 to valueSpace){
//        if(constraints.hard.contains(value)){
//          utilValueMap += (value -> HARD_UTILITY_N)
//        }
//        else if (constraints.soft.contains(value)){
//          utilValueMap += (value -> SOFT_UTILITY_N)
//        }
//        else if (constraints.preference.values.toList.contains(value)){
//          utilValueMap += (value -> PREF_UTILITY_N)
//        }
//      }
//     
//     var finalUtilities = Map[Any, Map[Int, Double]]()
//     finalUtilities += (id -> utilValueMap)
//     finalUtilities
//  }
  
    /**
   * Check if Meeting Scheduling is finished
   */
  final var MAX_ROUND : Int = 10000
  
  def finishedCheck() = {
    
    // Check Index
      var same : Boolean = true
      var refValue : Int  = meetingIndex.values.toList(0)
      for(value <- meetingIndex.values){
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
   * Round control
   */
  final var CHANGE_ROUND : Int = 10
    
  var roundCount = 0
  
  def newRound(){
    roundCount += 1 // FIXME move
  }
  
  
  var messages : Map[String, Double] = Map[String, Double]()
  
  def isChangeRound() : Boolean = {

    // Check Change
    if(roundCount >= CHANGE_ROUND){
        changeConstraints()
        true
    }
    else{
      false
    }
    
  }
  
  def changeConstraints(){
      //      var participations : Set[Int] = Set[Int](meetingID)
//      constraints = MeetingSchedulingFactory.buildSingleConstraints(id, participations)
   }
    
    /**
     * Utility Storage
     */
    final var PUSH_ROUND = 10
  
    var agentUtility : Double = 0
    def storeUtility(){
            
          // add current utility to messages
            val timestamp: Long = System.currentTimeMillis / 1000
            messages += timestamp.toString() -> agentUtility
            
            // Send if reached max
            if(roundCount % PUSH_ROUND == 0){
              println("push")
              Monitoring.update(id, messages)
              messages.clear()
            }
    }
  
}
