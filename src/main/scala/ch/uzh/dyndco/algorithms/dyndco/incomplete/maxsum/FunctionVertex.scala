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

	// react: variable message -> create message using messages from Neighbours except the receiver node
	// product of all messages!
	def collect() = {

//	println("Function: received signals");

	// Process constraints
	val hardConstraints = Map[Any, Set[Int]]() // Blocked timeslots (vertex, set of constraints)
			val softConstraints = Map[Any, Set[Int]]() // Free timeslots (vertex, set of constraints)
			val preference = Map[Any, Set[Int]]() // Proposed timeslots (vertex, set of constraints)

			// Unpack constraint pack
			for (signal <- signals.iterator) {

//				println("Function: processing signal");

				var proposal : Proposal = signal

						// sender
						var sender = proposal.sender
//						println("sender: " + sender)

						// work the blocked slots
						var h : Set[Int] = proposal.hard
//						println("hard: " + h)
						hardConstraints += (sender -> h)

						// work the free slots
						var s : Set[Int] = proposal.soft
//						println("soft: " + s)
						softConstraints += (sender -> s)

						// work the proposed slot
						var p : Set[Int] = proposal.preference
//						println("preference: " + p)
						preference += (sender -> p)
			}

	// create hard, soft and preference builds for every target with minimal costs
	val allAssignmentCosts = Map[Any, Map[Int,Double]]()
			for(target <- targetIds.iterator){

//				println("Function: processing target -> " + target)

				// build assignment that excludes target
				val assignedHard = Map[Int,Int]()
				val assignedSoft = Map[Int,Int]()
				val assignedPreferences = Map[Int,Int]()

				// hc
				for(curTarget : Any <- hardConstraints.keys){
					if(curTarget != target){
//						println("Function: processing hc (" + curTarget + ")")
						// get hard constraints of target
						for(targetConstraint : Int <- hardConstraints.apply(curTarget)){
//							println("Function: hc -- " + targetConstraint)
							if(assignedHard.contains(targetConstraint)){
//								println("Function: -> already inserted into map")
								assignedHard += (targetConstraint -> (assignedHard.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
//								println("Function: -> initialize into map")
								assignedHard += (targetConstraint -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// sc
				for(curTarget : Any <- softConstraints.keys){
					if(curTarget != target){
						println("Function: processing sc (" + curTarget + ")")
						// get hard constraints of target
						for(targetConstraint : Int <- softConstraints.apply(curTarget)){
							println("Function: sc -- " + targetConstraint)
							if(assignedSoft.contains(targetConstraint)){
								println("Function: -> already inserted into map")
								assignedSoft += (targetConstraint -> (assignedSoft.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
								println("Function: -> initialize into map")
								assignedSoft += (targetConstraint -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// pref
				for(curTarget : Any <- preference.keys){
					if(curTarget != target){
						println("Function: processing preference (" + curTarget + ")")
						// get hard constraints of target
						for(targetConstraint : Int <- preference.apply(curTarget)){
							println("Function: pref -- " + targetConstraint)
							if(assignedPreferences.contains(targetConstraint)){
								println("Function: -> already inserted into map")
							  assignedPreferences += (targetConstraint -> (assignedPreferences.apply(targetConstraint) + 1)) // Add up existing value
							}
							else {
								println("Function: -> initialize into map")
								assignedPreferences += (targetConstraint -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// calculate all possible value assignments and their costs for this agent
				val assignmentCosts  = Map[Int, Double]()
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
				
				// build constraints object for the assignments
				println("adding assignment costs: " + target + "->" + assignmentCosts.size)
			  allAssignmentCosts += (target -> assignmentCosts) // Target -> Assignment : Cost
			  
				      
			}
	
			// stop criteria
			if(allAssignmentCosts == lastAssignmentCosts && finishedCount > 1){
			  finished = true
			  println("Costs equal")
			}
			else {
			  if(allAssignmentCosts == lastAssignmentCosts) finishedCount+=1
			  else{
			    finished = false
			    lastAssignmentCosts = allAssignmentCosts
			    println("Cost not equal")
			  }
			}
	    
	    // Return builds in constraints object
	    println("Sending message: " + id + " -> " + allAssignmentCosts.size)
	    var proposal : Proposal = new Proposal(id, null,null,null)
	    proposal.addCostAssignments(allAssignmentCosts)
	    proposal
  }
}
