package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.VariableVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.Constraints
import collection.mutable.Map

class FunctionVertex (
      id: Any, 
      initialState: Constraints
    ) extends DataGraphVertex(id, initialState) {
  
  /**
   * Config
   */
  final var HARD_COST : Double = 2
  final var SOFT_COST : Double = 1
  final var PREF_COST : Double = -0.5
  
  	/**
	 * Finish boolean
	 */
	var finished : Boolean = false
  
  	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = Constraints
	
	/**
	 * Score signal function
	 */
		override def scoreSignal: Double = {
      
	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
	  
	  if(this.finished) 
	    0
	   else
	     1
     }
	
	// react: variable message -> create message using messages from Neighbours except the receiver node
	// product of all messages!
  def collect() = {
    
    // Process constraints
    var hardConstraints = Map[Any, Set[Int]]() // Blocked timeslots (vertex, set of constraints)
    var softConstraints = Map[Any, Set[Int]]() // Free timeslots (vertex, set of constraints)
    var preference = Map[Any, Set[Int]]() // Proposed timeslots (vertex, set of constraints)
    
    // Unpack constraint pack
    for (signal <- signals.iterator) {
     
      var constraints : Constraints = signal
      
      // work the blocked slots
        hardConstraints + (constraints.sender.id -> constraints.hard)
      
      // work the free slots
        softConstraints + (constraints.sender.id -> constraints.soft)
      
      // work the proposed slot
        preference + (constraints.sender.id -> constraints.preference)
    }
    
    // create hard, soft and preference builds for every target with minimal costs
    var targetBuilds = Map[Object, Constraints]()
    for(target <- targetIds.iterator){
      
      // build assignment that excludes target
      var assignedHard = Map[Int,Int]()
      var assignedSoft = Map[Int,Int]()
      var assignedPreferences = Map[Int,Int]()
      
      // hc
      for(curTarget : Any <- hardConstraints.keys){
        if(curTarget != target){
          // get hard constraints of target
          for(targetConstraint : Int <- hardConstraints.apply(curTarget)){
            if(assignedHard.apply(targetConstraint) == null){
              assignedHard + (targetConstraint -> 1) // Initialize this timeslot value
            }
            else {
              assignedHard + (targetConstraint -> (assignedHard.apply(targetConstraint) + 1)) // Add up existing value
            }
          }
        }
      }
      
       // sc
      for(curTarget : Any <- softConstraints.keys){
        if(curTarget != target){
          // get hard constraints of target
          for(targetConstraint : Int <- softConstraints.apply(curTarget)){
            if(assignedSoft.apply(targetConstraint) == null){
              assignedSoft + (targetConstraint -> 1) // Initialize this timeslot value
            }
            else {
              assignedSoft + (targetConstraint -> (assignedSoft.apply(targetConstraint) + 1)) // Add up existing value
            }
          }
        }
      }
      
       // pref
      for(curTarget : Any <- preference.keys){
        if(curTarget != target){
          // get hard constraints of target
          for(targetConstraint : Int <- preference.apply(curTarget)){
            if(assignedPreferences.apply(targetConstraint) == null){
              assignedPreferences + (targetConstraint -> 1) // Initialize this timeslot value
            }
            else {
              assignedPreferences + (targetConstraint -> (assignedPreferences.apply(targetConstraint) + 1)) // Add up existing value
            }
          }
        }
      }
      
      // calculate all possible value assignments and choose the one with the smallest cost (1 date now)
      var minCost : Double = Double.MaxValue // FIXME does this work?
      var minAssignment : Int = -1
      for(assignment <- 1 to 24){
        
        var hardCount = assignedHard.apply(assignment)
        var softCount = assignedSoft.apply(assignment)
        var prefCount = assignedPreferences.apply(assignment)
        
        var curCost = hardCount * HARD_COST + softCount * SOFT_COST + prefCount * PREF_COST
        
        if(curCost < minCost){
          minCost = curCost
          minAssignment = assignment
        }
      }
      
      // build constraints object for the assignments
      
      targetBuilds + (target -> new Constraints(target,hardFinal,softFinal,prefFinal))
      
    }
    
    // Return builds in constraints object
    var constraints : Constraints = new Constraints(null,null,null,null)
    constraints.addBuilds(targetBuilds)
    constraints
  }
	
	// needs a function that calculates the product of all utilities of all variable assignments
	// this function represents the constraints of one agent
}
