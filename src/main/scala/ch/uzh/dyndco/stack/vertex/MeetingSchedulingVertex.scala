package ch.uzh.dyndco.stack.vertex

import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.problems.MeetingConstraints
import scala.collection.mutable.MutableList
import scala.util.Random
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.util.Tabulator

abstract class MeetingSchedulingVertex (id: Any, initialState: Any) 
  extends BasicVertex(id, initialState) {
  
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
        else 
          showState()
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
    utility += (TIMESLOTS - (value - 1)).asInstanceOf[Double] / TIMESLOTS.asInstanceOf[Double]  
        
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
      
      // find max value
      var accepted : Boolean = false
      var position = 0
      var maxRounds = 10
      var roundCount = 0
      
      while(!accepted && roundCount < maxRounds){
        
        roundCount += 1
        
        // get value
        var assignment = orderedUtilities(position)
        var candidateValue = assignment._1
        
        // checks: agent different, history of used values
        var conflict = isValid(candidateValue)
      
        // process results
        if(!conflict){
            
            // register new value preference
            registerValue(candidateValue)
              
            accepted = true
        }
        else {
          
          if((position + 1) > (orderedUtilities.size - 1)){
            position = 0
            history.clear
          }
          else {
            if(Random.nextDouble() > 0.75)
              position += 1
          }
          
        }
      }
    
    value
  }
  
  private def isValid(candidateValue : Int) : Boolean = {
    
    var conflict : Boolean = false
    
    // agent index check
    if(AGENT_INDEX != null) {
      for(meeting <- AGENT_INDEX.keys){
      	if(meeting != MEETING_ID){
     			if(AGENT_INDEX.apply(meeting) == candidateValue){               
     				conflict = true
     			}
     		}
      }
    }

    // history check
    if(history.contains(candidateValue)){
    	if(history.apply(candidateValue) > 20){
    		conflict = true
    				history += (candidateValue -> 0)
    	}
    }
    
    conflict
  }
  
  private def registerValue(candidateValue : Int) {
    
      // update local value
      value = candidateValue
    
      // update constraints
      if(CONSTRAINTS_CURRENT != null)
        CONSTRAINTS_CURRENT.update(MEETING_ID, candidateValue)
    
      // add to indices
      if(AGENT_INDEX != null)
        AGENT_INDEX += (MEETING_ID -> candidateValue)
      if(MEETING_INDEX != null)
      MEETING_INDEX += (AGENT_ID -> candidateValue)
      
      // add to history
      if(history.contains(candidateValue))
        history += (candidateValue -> (history.apply(candidateValue) + 1))
      else
        history += candidateValue -> 1
      
  }
  
  /**
    * Utility Message Control
    */
   var messages = Map[String, String]()
   
   def storeAgentUtility(){
     
      // build message
      var utility = calculateSingleUtility(CONSTRAINTS_ORIGINAL, value)
      var message = utility + ";" + AGENT_INDEX + ";" + MEETING_INDEX
              
      // add current utility to messages
      messages += (System.currentTimeMillis.toString() -> message)
              
      // send if reached max rounds
      if(cycleCount == PUSH_ROUND){
         Monitoring.update(id, messages)
         messages.clear()
         cycleCount = 0
      }
   }
   
   /**
    * Show state
    */
   def showState(){
       printf("%-10.8s  %-40.60s  %-30.30s%n", id, MEETING_INDEX, AGENT_INDEX);
   }

}