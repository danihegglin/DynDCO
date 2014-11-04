package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex

class VariableVertex (
      id: Any, 
      initialState: Constraints,
      timeslots: Int
    ) extends DataGraphVertex(id, initialState) {
  
  /**
   * This variable constraints
   */
  var hardConstraints = initialState.hard
  var softConstraints = initialState.soft
  var preferences = initialState.preference
  
   /**
	 * Finish boolean
	 */
	var finished : Boolean = false
  
  	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = Constraints
	
		override def scoreSignal: Double = {
      
	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
	  
	  if(this.finished) 
	    0
	   else
	     1
     }
	
		// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
  def collect() = {
    
     // unpack assignment costs
    var allAssignmentCosts = Map[Any, Map[Int,Double]]()
    for (signal <- signals.iterator) {
      var constraints : Constraints = signal  
      allAssignmentCosts + (constraints.sender -> constraints.allCostAssignments)
    }
    
    // find assignment combination with min costs
    var newPreferences = Set[Int]()
    for(target <- targetIds.iterator){
      
      // process all assignment costs of all targets except current target
      var assignmentCostsSet = Set[Map[Int,Double]]()
      for(currTarget <- allAssignmentCosts.keys){
        if(currTarget != target){
          assignmentCostsSet + allAssignmentCosts.apply(currTarget)
        }
      }
      
      // Find minimal combination for target (FIXME for all meetings!)
      var minCost : Double = Double.MaxValue // FIXME: what is this value?
      var minAssignment : Int = -1
      
      for(assignment : Int <- 1 to timeslots){
        
        var curCost : Double = 0
        for(assignmentCosts <- assignmentCostsSet){
          curCost += assignmentCosts.apply(assignment)
        }
            
        if(curCost < minCost){
          
          // Don't allow hard constraint breaches
          if(!hardConstraints.contains(assignment)){
            
            // FIXME: Don't allow assignments where other preferences have been set
            newPreferences += assignment
            
          }
          
//          minCost = curCost
//          minAssignment = assignment
        }
      }
    }
    
    // Adjust own preferences & build hard, soft and preferences
   var newHardConstraints = hardConstraints // stays the same
   var newSoftConstraints = softConstraints // FIXME adjust this
    
    var constraints = new Constraints(id, newHardConstraints, newSoftConstraints, newPreferences)
    
    // Send out new preferences
    constraints
  }
  
}
