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
	
	def buildPrefMap(allAssignmentCosts : Map[Any, Map[Any, Map[Int, Double]]]) : Map[Any,Map[Int, Double]] = {
	  
	  val prefMap = Map[Any,Map[Int, Double]]() // Keeps track which meeting has which assignment
		
	  for(functionVertex <- allAssignmentCosts.keys){

					// build set of other functionVertices for the particular functionVertex target
					var assignmentCostsSet = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
					for(currFunctionVertex : Any <- allAssignmentCosts.keys){
						if(currFunctionVertex != functionVertex || allAssignmentCosts.size < 2){ // FIXME question: what if only one function, how should I create a result when excluding this function
							var variableCosts = allAssignmentCosts.apply(currFunctionVertex)
							assignmentCostsSet += variableCosts
						}
					}

			    // build assignment -> costs for the particular functionVertex target
			    for(assignment : Int <- 1 to timeslots){

				    var curCost : Double = 0
				    
				    // process every assignment costs map
						for(assignmentCosts <- assignmentCostsSet){
						  // process every variable
						  for(currVariableVertexId <- assignmentCosts.keys){
						    // ignore if the assignment is from this particular VariableVertex
							  if(currVariableVertexId != id){
							    // adjust assignment costs if they are set
								  if(assignmentCosts.contains(assignment)){
									  var assignmentMap : Map[Int,Double] = assignmentCosts.apply(assignment)
									  var cost = assignmentMap.apply(assignment)
										curCost += cost
									}
								}
							}
						}

				    // Add assignment costs to map for all functionVertices
			      var allAssignments : Map[Int, Double] = Map[Int, Double]()
			      if(prefMap.contains(functionVertex)){
				      allAssignments = prefMap.apply(functionVertex)
			      }
						allAssignments += (assignment -> curCost)
						prefMap += (functionVertex -> allAssignments)
			   }
			}
			prefMap
	}
	
	def buildFinalMap(prefMap : Map[Any,Map[Int, Double]]) : Map[Any, Int] = {
	  val finalPrefMap = Map[Any, Int]() // Contains best combination of assignments

		  // Find optimal assignment
			for(function <- prefMap.keys){
			  var proposedAssignments = prefMap.apply(function)
				for(currFunction <- prefMap.keys){
				  if(function != currFunction){
				    var currPropAssignments = prefMap.apply(currFunction)
						for(assignment <- proposedAssignments.keys){
						  if(currPropAssignments.contains(assignment)){
							  var newAssCosts = currPropAssignments.apply(assignment)
							  var oldAssCosts = proposedAssignments.apply(assignment)
							  proposedAssignments += (assignment -> (oldAssCosts + newAssCosts))
							}
					  }
				  }
				}

			  var minAss : Int = -1
				var minCosts = Double.MaxValue
				for(assignment <- proposedAssignments.keys){
				  if(proposedAssignments.apply(assignment) < minCosts){
					  minAss = assignment
					  minCosts = proposedAssignments.apply(assignment)
					} 
				}
				finalPrefMap += (function -> minAss)
			}
			finalPrefMap
	}
	
	def findCurrentCost(finalPrefMap: Map[Any,Int]) : Double = {
		  0.0 // FIXME
	}

	// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
	def collect() = {

		if(initialized){
		  
		  // unpack assignment costs
		  // allAssignmentCosts: function -> variable -> assignment -> cost
			val allAssignmentCosts = Map[Any, Map[Any, Map[Int, Double]]]()
			for (signal <- signals.iterator) {
				var proposal : Proposal = signal
				var costAssignments = proposal.allCostAssignments
				allAssignmentCosts += (proposal.sender -> costAssignments)
			}

			// prepare preferences map for rebuild
			// prefMap: function -> assignment -> cost
			val prefMap = buildPrefMap(allAssignmentCosts)
			println(id + " -> prebuild: " + prefMap)
			
			// find best assignments for all requirements
			// finalPrefMap: function -> chosen assignment
			val finalPrefMap = buildFinalMap(prefMap)
			println(id + " -> final: " + finalPrefMap)
			
			// find costs of the assignments
			val currentCosts = findCurrentCost(finalPrefMap)
			    
			// Push current utility
			val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + currentCosts)
			val result = Http(svc OK as.String)

			new Proposal(id, hardConstraints, softConstraints, finalPrefMap)
		}
		else {
			initialized = true
			initialState
		}
	}

}
