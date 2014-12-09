package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.dynamic.DynamicVertex

class FunctionVertex (
		id : Any, 
		initialState: MaxSumMessage,
		valueSpace: Int
		) extends DynamicVertex(id, initialState) {
  
	/**
	 * Config
	 */
	final var HARD_UTILITY : Double = -10
	final var SOFT_UTILITY : Double = 0
	final var PREF_UTILITY : Double = 10

	/**
	 * Control parameters
	 */
	var finished : Boolean = false
	var finishedCount : Int = 0
	var marginalUtilityHistory = Map[Any, Map[Int,Double]]()

	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = MaxSumMessage

	/**
	 * Score signal function
	 */
	override def scoreSignal: Double = {
		if(this.finished) 0
		else 1
	}
	
	def buildMarginalUtilities(){
	  // FIXME move code here
	}

	def collect() = {

	    // Process constraints
	    val hardConstraints = Map[Any, Set[Int]]() // Blocked timeslots (vertex, set of constraints)
		  val softConstraints = Map[Any, Set[Int]]() // Free timeslots (vertex, set of constraints)
		  val preferences = Map[Any, Map[Int,Int]]() // Proposed timeslot of any variable (vertex, set of constraints)

		// Unpack constraint pack
		for (signal <- signals.iterator) {

		  var proposal : MaxSumMessage = signal

			// sender
			var sender = proposal.sender

			// work the blocked slots
			var h : Set[Int] = proposal.hard
			hardConstraints += (sender -> h)

			// work the free slots
			var s : Set[Int] = proposal.soft
			softConstraints += (sender -> s)

			// work the proposed slot, take the necessary one
			var p : Map[Int,Int] = proposal.preference
			preferences += (sender -> p)

		}

	    // create hard, soft and preference builds for every target with minimal costs
	    val allMarginalUtilities = Map[Any, Map[Int,Double]]()
			for(messageReceiver <- targetIds.iterator){

				// build an assignment that excludes the target
				val assignedHard = Map[Int,Int]()
				val assignedSoft = Map[Int,Int]()
				val assignedPreferences = Map[Int,Int]()

				// hc
				for(variable : Any <- hardConstraints.keys){
					if(variable != messageReceiver){

					  // get hard constraints of target
						for(targetConstraint : Int <- hardConstraints.apply(variable)){
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
				for(variable : Any <- softConstraints.keys){
					if(variable != messageReceiver){
						// get hard constraints of target
						for(variableConstraint : Int <- softConstraints.apply(variable)){
							if(assignedSoft.contains(variableConstraint)){
								assignedSoft += (variableConstraint -> (assignedSoft.apply(variableConstraint) + 1)) // Add up existing value
							}
							else {
								assignedSoft += (variableConstraint -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// pref
				for(variable <- preferences.keys){
					if(variable != messageReceiver){
						
					  // get hard constraints of target
					    var meetings = preferences.apply(variable)
					    
					    for(meeting <- meetings.keys){
					    	
					      var timeslot = meetings.apply(meeting)
					      
							if(assignedPreferences.contains(timeslot)){
							  assignedPreferences += (timeslot -> (assignedPreferences.apply(timeslot) + 1)) // Add up existing value
							}
							else {
								assignedPreferences += (timeslot -> 1) // Initialize this timeslot value
							}
						}
					}
				}

				// calculate all possible value assignments and their utility for the message receiving agent
				val marginalUtility = Map[Int, Double]()
				for(valueAssignment <- 1 to valueSpace){
				  
				  var hardCount = 0
				  var softCount = 0
				  var prefCount = 0
				  
				  if(assignedHard.contains(valueAssignment)){
				    hardCount += assignedHard.apply(valueAssignment)
				  }
				  if(assignedSoft.contains(valueAssignment)){
				    softCount += assignedSoft.apply(valueAssignment)
				  }
				  if(assignedPreferences.contains(valueAssignment)){
				    prefCount += assignedPreferences.apply(valueAssignment)
				  }
				        
				  var utility = hardCount * HARD_UTILITY + softCount * SOFT_UTILITY + prefCount * PREF_UTILITY
				  
				  marginalUtility += (valueAssignment -> utility)
				 
//				  println(
//				      "id: " + id + 
//				      " | target: " + messageReceiver +
//				      " | assignment: " + valueAssignment +
//				      " | utility: " + utility) 
				}
				
				// build constraints object for the assignments
			  allMarginalUtilities += (messageReceiver -> marginalUtility) // Target -> Assignment : Cost
				      
			}
	
	    // Return builds in constraints object
	    var proposal : MaxSumMessage = new MaxSumMessage(id, null,null,null)
	    proposal.addCostAssignments(allMarginalUtilities)
	    proposal.addParticipationIndex(preferences)
	    proposal
			
  }
}
