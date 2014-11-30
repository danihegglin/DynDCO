package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import dispatch._,Defaults._
import scala.collection.SortedMap
import scala.util.Random

class VariableVertex (
		id: Any, 
		initialState: Proposal, 
		valueSpace: Int
		) extends DynamicVertex(id, initialState) {

	/**
	 * This variable constraints
	 */
	var hardConstraints = initialState.hard
	var softConstraints = initialState.soft
	var preferences = initialState.preference
	
	/**
	 * The Utility
	 */
	var agentUtility : Double = 0
	var lastUtility : Double = 0
	var lastCount : Int = 0

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

			override def scoreSignal: Double = {
			      
				  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
				  
				  if(this.finished) 
				    0
				   else
				     1
			    
			     }

			def show() = {
		println("variable " + id)
		println("--------------")
		println("hc:" + hardConstraints.toString())
		println("sc:" + softConstraints.toString())
		println("pf:" + preferences.toString())
		println("--------------")
	}
	
	def buildMarginalUtilities(allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]], timeslots : Int) : Map[Any,Map[Int, Double]] = {
	  
	  val marginalUtilities = Map[Any,Map[Int, Double]]() // Keeps track which meeting has which assignment
		
	  for(functionVertex <- allMarginalUtilities.keys){
	    
		  // build set of other functionVertices for the particular functionVertex target
		var allUtilities = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
		for(currFunctionVertex <- allMarginalUtilities.keys){
			if(currFunctionVertex != functionVertex){ 
				var variableUtility = allMarginalUtilities.apply(currFunctionVertex)
				allUtilities += variableUtility
			}
		}
	    
		// build assignment -> costs for the particular functionVertex target
		var assignmentMap : Map[Int,Double] = Map[Int,Double]()
		for(assignment : Int <- 1 to timeslots){

			var fullAssignmentCost : Double = 0
				    
			// process every utility map
			for(functions <- allUtilities){
				for(currVariableVertexId <- functions.keys){
					// ignore if the assignment is from this particular VariableVertex
					if(currVariableVertexId != id){
					  var variableAssignments = functions.apply(currVariableVertexId)
					  // adjust assignment costs if they are set in the set
					  if(variableAssignments.contains(assignment)){
						  var costs = variableAssignments.apply(assignment)
						  fullAssignmentCost += costs
					  }
					}
				}
			}
			assignmentMap += (assignment -> fullAssignmentCost)
		}
		// Add assignment costs to map for all functionVertices
		marginalUtilities += (functionVertex -> assignmentMap)
	  }
	  
	  marginalUtilities
	}
	
	def findBestValueAssignment(marginalUtilities : Map[Any,Map[Int, Double]]) : Map[Int, Int] = {
		
	  val bestValueAssignment = Map[Int, Int]() // Contains best combination of assignments for the greater good

	  // Find optimal assignement
	  var combinedUtilities = Map[Int, Double]()
	  for(function <- marginalUtilities.keys){
		  var utilities = marginalUtilities.apply(function)
		  for(assignment <- utilities.keys){
		    var utility = utilities.apply(assignment)
		    var combinedUtility : Double = 0
		    if(combinedUtilities.contains(assignment)){
		      combinedUtility += combinedUtilities.apply(assignment)
		    }
		    combinedUtility += utility
		    combinedUtilities += (assignment -> combinedUtility)
		  }
	  }
	  
	  var orderedUtilities = combinedUtilities.toList sortBy {_._2}
	  
	  var meetings : Map[Int, Int] = initialState.preference
	  
	  var random : Random = new Random
	  var blocked = Set()
	  var counter = 0
	  for(meeting <- meetings.keys){
	    var randPick = random.nextInt(meetings.size)
//	    println("randPick" + randPick)
	    var assignment = orderedUtilities(randPick)
	    println("assignment" + assignment)
	    bestValueAssignment += (meeting -> assignment._1)
	    counter += 1
	  }
	  var argMax = orderedUtilities(0)
	  agentUtility = argMax._2 // how?
	  
	  bestValueAssignment
	}
	
	def findCurrentCost(bestValueAssignment: Map[Any,Int]) : Double = {
		  0.0 // FIXME
	}

	// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
	def collect() = {

		if(initialized){
		  
		  // allMarginalUtilities: function -> variable -> assignment -> utility
			val allMarginalUtilities = Map[Any, Map[Any, Map[Int, Double]]]()
			for (signal <- signals.iterator) {
				try {
					var proposal : Proposal = signal
					var costAssignments = proposal.allCostAssignments
					var sender : Any = proposal.sender
					allMarginalUtilities += (sender -> costAssignments)
				}
				catch {
				  case e : Exception => println("signal was null")
				}
			}

			// prepare utilities
			val marginalUtilities = buildMarginalUtilities(allMarginalUtilities, valueSpace)
//			println(id + " -> utilities: " + marginalUtilities)
			
			// find best assignments for all requirements
			val bestValueAssignment = findBestValueAssignment(marginalUtilities)
			preferences = bestValueAssignment
//			println(id + " -> best: " + bestValueAssignment)
			
			// adjust softConstraints // FIXME also hardconstraints
			var newSoftConstraints = Set[Int]()
			for(assignment <- 1 to valueSpace){
				if(bestValueAssignment.contains(assignment)){}
				else if(hardConstraints.contains(assignment)){}
				else {
				  newSoftConstraints += assignment
				}
			}
			
			// Push current utility
			val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + agentUtility)
			val result = Http(svc OK as.String)
			
			println(id + ": " + agentUtility)
			
			if(agentUtility == lastUtility){
			  if(lastCount > 100){
			    finished = true;
			  }
			  else {
			    lastCount += 1
			  }
			}
			lastUtility = agentUtility

			new Proposal(id, hardConstraints, newSoftConstraints, bestValueAssignment)
			initialState
		}
		else {
			initialized = true
			initialState
		}
	}
}
