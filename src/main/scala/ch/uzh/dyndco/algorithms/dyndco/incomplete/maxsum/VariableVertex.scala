package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import dispatch._, Defaults._

class VariableVertex (
      id: Any, 
      initialState: Proposal,
      timeslots: Int
    ) extends DynamicVertex(id, initialState) {
  
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
	
	def show() = {
    println("variable " + id)
    println("--------------")
    println("hc:" + hardConstraints.toString())
    println("sc:" + softConstraints.toString())
    println("pf:" + preferences.toString())
    println("--------------")
  }
	
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
      println("ERROR HERE: " + sender + " -> " + costAssignments.size)
      allAssignmentCosts += (sender -> costAssignments)
    }
    println("unpacked assignment costs: " + allAssignmentCosts.size)
    
    // 1. all functions: build for all functions
    var minCost : Double = Double.MaxValue // FIXME: what is this value?
    var minAssignment : Int = -1
    
    for(function <- allAssignmentCosts.keys){
      
      // build set of other functions
      var assignmentCostsSet = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
      for(currFunction : Any <- allAssignmentCosts.keys){
        println("processing function packets: " + currFunction)
        if(currFunction != function || allAssignmentCosts.size < 2){ // FIXME question: what if only one function, how should I create a result when excluding this function
          var variableCosts = allAssignmentCosts.apply(currFunction)
          println("function is allowed to be included: " + variableCosts.size)
          assignmentCostsSet += variableCosts
        }
      }
      
      // 2. all variables: take all variables except this one
      // 3. all assignments: take all available assignments and pick the minimal one 
      
      // Find minimal combination for target (FIXME for all meetings!)
      for(assignment : Int <- 1 to timeslots){
        
        println("testing assignment: " + assignment)
        
        var curCost : Double = 0
        
        // Run every function
        for(assignmentCosts <- assignmentCostsSet){
          println("going through set: " + assignmentCosts.size)
          for(specificAssignment <- assignmentCosts.keys){
            println("testing variable set: " + specificAssignment + "(id=" + id + ")")
            if(specificAssignment != id){ // if the assignment is not from this variable 
              if(assignmentCosts.contains(assignment)){
                var assignmentMap : Map[Int,Double] = assignmentCosts.apply(assignment)
                println("assignmentMap of variable: " + assignmentMap.size)
                var cost = assignmentMap.apply(assignment)
                println("cost: " + cost)
                curCost = curCost + cost
              }
            }
          }
        }
        
        println("assignment: " + assignment + " -> cost: " + curCost)
        
        if(curCost < minCost){
          
          println("considering assignment")
          
          // Don't allow hard constraint breaches
//          if(!hardConstraints.contains(assignment)){
            
            // FIXME: Don't allow assignments where other preferences have been set
            println("adding assignment");
            preferences = Set(assignment)
            
            minCost = curCost
            
//          }
        }
      }
     }
    
    // Push current utility
    println("Sending mincost (" + minCost + ") to monitoring")
    val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + minCost)
    val result = Http(svc OK as.String)
    
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
