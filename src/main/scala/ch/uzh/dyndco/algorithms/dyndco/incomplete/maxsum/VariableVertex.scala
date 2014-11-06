package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map

class VariableVertex (
      id: Any, 
      initialState: Proposal,
      timeslots: Int
    ) extends DataGraphVertex(id, initialState) {
  
  /**
   * Logger
   */

  
  /**
   * This variable constraints
   */
  var hardConstraints = initialState.hard
  var softConstraints = initialState.soft
  var preferences = initialState.preference
  
   /**
	 * Control parameters
	 */
	var finished : Boolean = false
	var initialized : Boolean = false
  
  	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = Proposal
	
//		override def scoreSignal: Double = {
//      
//	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
//	  
//	  if(this.finished) 
//	    0
//	   else
//	     1
//    
//     }
	
		// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
  def collect() = {
    
    if(initialized){
    
    println("Variable: received signals");
    
     // unpack assignment costs
    var allAssignmentCosts = Map[Any, Map[Any, Map[Int, Double]]]()
    for (signal <- signals.iterator) {
      var proposal : Proposal = signal
      var sender : Any = proposal.sender
      var costAssignments = proposal.allCostAssignments
      allAssignmentCosts += (sender -> costAssignments)
    }
    println("unpacked assignment costs: " + allAssignmentCosts.size)
    
    // 1. all functions: build for all functions
    // 2. all variables: take all variables except this one
    // 3. all assignments: take all available assignments and pick the minimal one
    
    // Pick specific packs for this variable
    println("starting to pick correct costs from all functions: " + id)
    var filteredCostMaps = Map[Any,[Map[Int,Double]]]()
    for(targetId <- allAssignmentCosts.keys){
      if(targetId == id){
        filteredCostMaps += allAssignmentCosts.apply(targetId)
      }
    }
//    var functionCostMap = allAssignmentCosts.apply(id)
//    println("functionCostMap: " + functionCostMap.size)
    
    // find assignment combination with min costs
//    var newPreferences = Set[Int]()
//    for(function <- targetIds.iterator){
      
      // process all assignment costs of all targets except current target
//      var assignmentCostsSet = Set[Map[Int,Double]]()
//      for(currFunction <- functionCostMap.keys){
//        if(currFunction != function){
//          assignmentCostsSet + functionCostMap.apply(currFunction) // FIXME fails
//        }
//      }
      
//      // Find minimal combination for target (FIXME for all meetings!)
//      var minCost : Double = Double.MaxValue // FIXME: what is this value?
//      var minAssignment : Int = -1
//      
//      for(assignment : Int <- 1 to timeslots){
//        
//        var curCost : Double = 0
//        for(assignmentCosts <- assignmentCostsSet){
//          curCost += assignmentCosts.apply(assignment)
//        }
//            
//        if(curCost < minCost){
//          
//          // Don't allow hard constraint breaches
//          if(!hardConstraints.contains(assignment)){
//            
//            // FIXME: Don't allow assignments where other preferences have been set
//            newPreferences += assignment
//            
//          }
//          
////          minCost = curCost
////          minAssignment = assignment
//        }
//      }
//    }
    
//    // Adjust own preferences & build hard, soft and preferences
//   var newHardConstraints = hardConstraints // stays the same
//   var newSoftConstraints = softConstraints // FIXME adjust this
//    
//    var constraints = new Constraints(id, newHardConstraints, newSoftConstraints, newPreferences)
//    
//    // Send out new preferences
//    constraints
      initialState
    }
    else {
      initialized = true
      initialState
    }
  }
  
}
