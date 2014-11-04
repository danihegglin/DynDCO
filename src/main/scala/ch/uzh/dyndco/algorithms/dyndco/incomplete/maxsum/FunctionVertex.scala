package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.VariableVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.Constraints
import collection.mutable.Map

class FunctionVertex (
      id: Any, 
      initialState: Constraints,
      timeslots: Int
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
      hardConstraints + (constraints.sender -> constraints.hard)
      
      // work the free slots
      softConstraints + (constraints.sender -> constraints.soft)
      
      // work the proposed slot
      preference + (constraints.sender -> constraints.preference)
    }
    
    // create hard, soft and preference builds for every target with minimal costs
    var allAssignmentCosts = Map[Any, Map[Int,Double]]()
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
            if(assignedHard.contains(targetConstraint)){
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
            if(assignedSoft.contains(targetConstraint)){
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
            if(assignedPreferences.contains(targetConstraint)){
              assignedPreferences + (targetConstraint -> 1) // Initialize this timeslot value
            }
            else {
              assignedPreferences + (targetConstraint -> (assignedPreferences.apply(targetConstraint) + 1)) // Add up existing value
            }
          }
        }
      }
      
      // calculate all possible value assignments and their costs for this agent
      var assignmentCosts  = Map[Int, Double]()
      for(assignment <- 1 to timeslots){
        
        var hardCount = assignedHard.apply(assignment)
        var softCount = assignedSoft.apply(assignment)
        var prefCount = assignedPreferences.apply(assignment)
        
        var curCost = hardCount * HARD_COST + softCount * SOFT_COST + prefCount * PREF_COST
        assignmentCosts + (assignment -> curCost)
 
      }
      
      // build constraints object for the assignments
      allAssignmentCosts + (target -> assignmentCosts) // Target -> Assignment : Cost
      
    }
    
    // Return builds in constraints object
    var constraints : Constraints = new Constraints(id, null,null,null)
    constraints.addCostAssignments(allAssignmentCosts)
    constraints
  }
}
