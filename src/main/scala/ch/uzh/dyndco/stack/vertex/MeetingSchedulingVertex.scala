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
    final var BLOCKED_UTILITY_N : Double = 0
    final var FREE_UTILITY_N : Double = 0.75
    final var PREF_UTILITY_N : Double = 1
    final var SAME_UTILITY : Double = 0.5
    final var DIFFERENT_UTILITY : Double = 0.5
    
    var TIMESLOTS : Int = -1
    var CONSTRAINTS_ORIGINAL : MeetingConstraints = null
    var CONSTRAINTS_CURRENT : MeetingConstraints = null
    var MEETING_INDEX : Map[Any, Int] = null // monitors values for specific meetings -> needs to be the same
    var AGENT_INDEX : Map[Any, Int] = null // monitors values chosen by user -> needs to be different
    var MEETING_ID : Int = -1
    var AGENT_ID : Int = -1
    
    var MAX_REPETITIONS = 20
    var MAX_ATTEMPTS = 20
    var REPEAT_PROB = 0.90
    
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
   override def convergenceCheck() = {
      
    // Check Meeting Index
    if(MEETING_INDEX != null && MEETING_INDEX.size > 0){
      var same : Boolean = true
      var refValue : Int  = MEETING_INDEX.values.toList(0)
      for(value <- MEETING_INDEX.values){
        if(value != refValue)
          same = false
      }
      
      // Check Agent Index
      if(AGENT_INDEX == null){
        if(same){
          converged = true
        }
      }
      else if(AGENT_INDEX.size > 0){
        
        var different : Boolean = true
        refValue = -1
        for(value <- AGENT_INDEX.values){
          if(value == refValue)
            different = false
            
          refValue = value
        }
        
        if(same && different){
          converged = true
        }
        else 
          showState()
      }
    }
      
    // Check Max Round      
    if(roundCount >= MAX_ROUND){
      converged = true
    }
    
  }  
    
  /**
   * Calculate Utility for current Best Value Assignment for meeting constraints
   */
  def calculateSingleUtility(constraints : MeetingConstraints, value : Int): Double = {
    
    var utility : Double = 0
        
    // timeslot utility
    utility += (TIMESLOTS - (value - 1)).asInstanceOf[Double] / TIMESLOTS.asInstanceOf[Double]  
        
    // calendar utility
    if(CONSTRAINTS_CURRENT.blocked.contains(value)){
      utility += BLOCKED_UTILITY_N
    }
    else if (CONSTRAINTS_CURRENT.free.contains(value)){
      utility += FREE_UTILITY_N
    }
    else if (CONSTRAINTS_CURRENT.preferred.values.toList.contains(value)){
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
      
      // add hard constraint utilities
      val extendedUtilities = addHardConstraints(utilities)
    
      // order utilities by value
      var orderedUtilities = extendedUtilities.toList sortBy {_._2}
      orderedUtilities = orderedUtilities.reverse
      
      // find max value
      var accepted : Boolean = false
      var position = 0
      var roundCount = 0
      var maxValue = 0
      
      while(!accepted && roundCount < MAX_ATTEMPTS){
        
        roundCount += 1
        
        // get value
        var assignment = orderedUtilities(position)
        var candidateValue = assignment._1
        
        // checks: agent different, history of used values
        var conflict = isValid(candidateValue)
      
        // process results
        if(!conflict){
          
            // add to history
            if(history.contains(candidateValue))
              history += (candidateValue -> (history.apply(candidateValue) + 1))
            else
              history += candidateValue -> 1
              
            maxValue = candidateValue
            accepted = true
        }
        else {

          if((position + 1) > (orderedUtilities.size - 1)){
            position = 0
            history.clear
          }
          else {
            if(Random.nextDouble() > REPEAT_PROB)
              position += 1
          }
          
        }
      }
      
     history.clear()

     maxValue
  }
  
  def addHardConstraints(utilities : Map[Int, Double]) : Map[Int, Double] = {
    
    for(candidateValue <- utilities.keys){
    
      var isDifferent = true
      var isSame = true
        
      // agent index check
      if(AGENT_INDEX != null) {
        for(meeting <- AGENT_INDEX.keys){
          if(meeting != MEETING_ID){
            if(AGENT_INDEX.apply(meeting) == candidateValue){               
              isDifferent = false
            }
          }
        }
      }
      
      // meeting index check
      if(MEETING_INDEX != null && MEETING_INDEX.size > 0){
        var same : Boolean = true
        var refValue : Int  = MEETING_INDEX.values.toList(0)
        for(value <- MEETING_INDEX.values){
          if(value != refValue)
            isSame = false
        }
      }
      
      if(isDifferent){
        utilities += (candidateValue -> (utilities.apply(candidateValue) + DIFFERENT_UTILITY))
      }
      if(isSame){
        utilities += (candidateValue -> (utilities.apply(candidateValue) + SAME_UTILITY))
      }
      
    }
    
    utilities

  }
  
  def isValid(candidateValue : Int) : Boolean = {
    
    var conflict : Boolean = false
    
    // basic check
    if(candidateValue <= 0){
      conflict = true
    }
      
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
    	if(history.apply(candidateValue) > MAX_REPETITIONS){
    		conflict = true
    	  history += (candidateValue -> 0)
    	}
    }
    
    if(conflict)
      storeAgentConflicts()
    
    conflict
  }
  
  /**
   * Update all indexes
   */
  def registerValue(candidateValue : Int) {
    
    if(candidateValue > 0 && candidateValue <= TIMESLOTS){
    
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
        
    }
      
  }
  
  /**
   * Normalize
   */
  def normalize(utility : Double) : Double = {
    var normalized : Double = 0.0
    
    var max = (MEETING_INDEX.size - 1) * (MEETING_INDEX.size)
    
    if(utility > 0){
      
      try {
        normalized = (utility - 0) / (max - 0)
      } 
      catch {
        case e : Exception => println("normalization error")
      }
    }
    normalized
  } 
  
  /**
    * Message Control
    */
   var updates = Map[String, String]()
   var conflicts = Map[String, String]()
   
   def storeAgentUtility(normalizeValue : Boolean){
     
      // build message
      var utility = calculateSingleUtility(CONSTRAINTS_ORIGINAL, value)
      
      if(normalizeValue)
        utility = normalize(utility)
      
      var message = utility + ";" + AGENT_INDEX + ";" + MEETING_INDEX
              
      // add current utility to update messages
      updates += (System.currentTimeMillis.toString() -> message)
              
      // send if reached max rounds
      if(cycleCount == PUSH_ROUND){
         Monitoring.update(id, updates)
         updates.clear()
         cycleCount = 0
      }
   }
   
   def storeAgentConflicts() {
     
     conflicts += (System.currentTimeMillis.toString()  -> "conflict")
     
     // send if reached max rounds
     if(cycleCount == PUSH_ROUND){
         Monitoring.conflict(id, conflicts)
         conflicts.clear()
         cycleCount = 0
      }
         
   }
   
   /**
    * Show state
    */
   def showState(){
      printf("%-10.8s  %-40.80s  %-30.60s%n", id, "m " + MEETING_INDEX, "a " + AGENT_INDEX);
   }

}