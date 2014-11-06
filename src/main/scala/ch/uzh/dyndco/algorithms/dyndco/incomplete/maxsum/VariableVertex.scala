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
    val allAssignmentCosts = Map[Any, Map[Any, Map[Int, Double]]]()
    for (signal <- signals.iterator) {
      var proposal : Proposal = signal
      var sender : Any = proposal.sender
      var costAssignments = proposal.allCostAssignments
      allAssignmentCosts += (sender -> costAssignments)
    }
    println("unpacked assignment costs: " + allAssignmentCosts.size)
    
    // 1. all functions: build for all functions
    for(function <- allAssignmentCosts.keys){
      
      // build set of other functions
      var assignmentCostsSet = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
      for(currFunction : Any <- allAssignmentCosts.keys){
        if(currFunction != function){
          assignmentCostsSet += allAssignmentCosts.apply(currFunction) // FIXME fails
        }
      }
      
      // 2. all variables: take all variables except this one
      // 3. all assignments: take all available assignments and pick the minimal one 
      
      // Find minimal combination for target (FIXME for all meetings!)
      var minCost : Double = Double.MaxValue // FIXME: what is this value?
      var minAssignment : Int = -1
      
      for(assignment : Int <- 1 to timeslots){
        
        val curCost : Double = 0
        
        // Run every function
        for(assignmentCosts <- assignmentCostsSet){
          for(specificAssignment <- assignmentCosts.keys){
            if(specificAssignment != id){ // if the assignment is not from this variable            
            var assignmentMap : Map[Int,Double] = assignmentCosts.apply(assignment)
            curCost + assignmentMap.apply(assignment)
            }
          }
        }
            
        if(curCost < minCost){
          
          // Don't allow hard constraint breaches
          if(!hardConstraints.contains(assignment)){
            
            // FIXME: Don't allow assignments where other preferences have been set
            preferences += assignment
            
          }
        }
      
      }
     }
    
    // Adjust own preferences & build hard, soft and preferences
//   var newHardConstraints = hardConstraints // stays the same
//   var newSoftConstraints = softConstraints // FIXME adjust this
     new Proposal(id, hardConstraints, softConstraints, preferences)
    }
    else {
      initialized = true
      initialState
    }
  }
  
}
