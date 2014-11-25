package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set

class FunctionVertex (
		id: Any, 
		initialState: Proposal,
		timeslots: Int
		) extends DynamicVertex(id, initialState) {

	/**
	 * Config
	 */
	final var HARD_COST : Double = 2
	final var SOFT_COST : Double = 1
	final var PREF_COST : Double = -0.5

	/**
	 * Control parameters
	 */
	var finished : Boolean = false
	var finishedCount : Int = 0
	var lastAssignmentCosts = Map[Any, Map[Int,Double]]()

	/**
		* Indicates that every signal this vertex receives is
		* an instance of Int. This avoids type-checks/-casts.
		*/
	type Signal = Proposal

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

	def collect() = {

	    // Process constraints
	    val hardConstraints = Map[Any, Set[Int]]() // Blocked timeslots (vertex, set of constraints)
		val softConstraints = Map[Any, Set[Int]]() // Free timeslots (vertex, set of constraints)
		val preference = Map[Any, Int]() // Proposed timeslot of any variable (vertex, set of constraints)

		// Unpack constraint pack
		for (signal <- signals.iterator) {
//			println("Function: Received Signal")
			var proposal : Proposal = signal

			// sender
			var sender = proposal.sender

			// work the blocked slots
			var h : Set[Int] = proposal.hard
			hardConstraints += (sender -> h)

						// work the free slots
						var s : Set[Int] = proposal.soft
						softConstraints += (sender -> s)

						// work the proposed slot, take the necessary one
						var p : Map[Any,Int] = proposal.preference
						println("preference: " + p)
						for(function <- p.keys){
						  var assgn = p.apply(function)
						  if(function == id){
						    preference += (sender -> assgn)
						  }
						  else {
						    // add to hardConstraints
						    var hc : Set[Int] = hardConstraints.apply(sender)
						    hc + assgn
						    hardConstraints += (sender -> hc)
						  }
						}
			}

	// create hard, soft and preference builds for every target with minimal costs
	val allAssignmentCosts = Map[Any, Map[Int,Double]]()
			for(target <- targetIds.iterator){

				// build assignment that excludes target
				val assignedHard = Map[Int,Int]()
				val assignedSoft = Map[Int,Int]()
				val assignedPreferences = Map[Int,Int]()

				// hc
				for(curTarget : Any <- hardConstraints.keys){
					if(curTarget != target){

					  // get hard constraints of target
						for(targetConstraint : Int <- hardConstraints.apply(curTarget)){
							if(assignedHard.contains(targetConstraint)){
								assignedHard += (targetConstraint -> (assignedHard.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
								assignedHard += (targetConstraint -> 1) // Initialize this timeslot value
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
								assignedSoft += (targetConstraint -> (assignedSoft.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
								assignedSoft += (targetConstraint -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// pref -> take function related
				for(curTarget : Any <- preference.keys){
					if(curTarget != target){
						// get hard constraints of target
					    var targetConstraint = preference.apply(curTarget)
					  
							if(assignedPreferences.contains(targetConstraint)){
//								println("Function: -> already inserted into map")
							  assignedPreferences += (targetConstraint -> (assignedPreferences.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
//								println("Function: -> initialize into map")
								assignedPreferences += (targetConstraint -> 1) // Initialize this timeslot value
							}
					}
				}

				// calculate all possible value assignments and their costs for this agent
				val assignmentCosts = Map[Int, Double]()
//				println("----------------------------")
				for(assignment <- 1 to timeslots){
				  
				  var hardCount = 0
				  var softCount = 0
				  var prefCount = 0
				  
				  if(assignedHard.contains(assignment)){
				    hardCount += assignedHard.apply(assignment)
				  }
				  if(assignedSoft.contains(assignment)){
				    softCount += assignedSoft.apply(assignment)
				  }
				  if(assignedPreferences.contains(assignment)){
				    prefCount += assignedPreferences.apply(assignment)
				  }
				        
				  var curCost = hardCount * HARD_COST + softCount * SOFT_COST + prefCount * PREF_COST
				  
				  assignmentCosts += (assignment -> curCost)
				 
				  println(
				      "assignment: " + assignment + 
				      " | hardC: " + hardCount + 
				      " | softC: " + softCount + 
				      " | prefC: " + prefCount + 
				      " | allC: " + curCost) 
				}
				
//				println("----------------------------")
				
				// build constraints object for the assignments
			  allAssignmentCosts += (target -> assignmentCosts) // Target -> Assignment : Cost
				      
			}
	
			// stop criteria
			if(allAssignmentCosts == lastAssignmentCosts && finishedCount > 1){
			  finished = true
//			  println("Costs equal")
			}
			else {
			  if(allAssignmentCosts == lastAssignmentCosts) finishedCount+=1
			  else{
			    finished = false
			    lastAssignmentCosts = allAssignmentCosts
//			    println("Cost not equal")
			  }
			}
	    
	    // Return builds in constraints object
//	    println("Sending message: " + id + " -> " + allAssignmentCosts.size)
	    var proposal : Proposal = new Proposal(id, null,null,null)
	    proposal.addCostAssignments(allAssignmentCosts)
	    proposal
			
//				  initialState
	  
  }
}
