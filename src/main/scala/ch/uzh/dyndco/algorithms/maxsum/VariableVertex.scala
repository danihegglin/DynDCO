package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import dispatch._
import dispatch.Defaults._
import scala.collection.SortedMap
import scala.util.Random
import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.problems.Constraints

class VariableVertex (
		id: Any, 
		initialState: MaxSumMessage, 
		valueSpace: Int,
    constraints : Constraints,
    index : Map[Int, Int]
		) extends DynamicVertex(id, initialState) {
  
  /**
   * Config
   */
  final var HARD_UTILITY : Double = -10
  final var SOFT_UTILITY : Double = 0
  final var PREF_UTILITY : Double = 10

//	/**
//	 * This variable constraints
//	 */
//	var hardConstraints = initialState.hard
//	var softConstraints = initialState.soft
//	var preferences = initialState.preference
	
	/**
	 * The Utility
	 */
	final var originalConstraints = constraints
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
			type Signal = MaxSumMessage

			override def scoreSignal: Double = {
			      
				  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
				  
				  if(this.finished) 
				    0
				   else
				     1
			    
			     }

//			def show() = {
//		println("variable " + id)
//		println("--------------")
//		println("hc:" + hardConstraints.toString())
//		println("sc:" + softConstraints.toString())
//		println("pf:" + preferences.toString())
//		println("--------------")
//	}
	
	def buildMarginalUtilities(allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]], timeslots : Int) : Map[Any,Map[Int, Double]] = {
	  
	  val marginalUtilities = Map[Any,Map[Int, Double]]() // Keeps track which meeting has which assignment
		
	  for(functionVertex <- allMarginalUtilities.keys){
	    
  		// build set of other functionVertices for the particular functionVertex target
  		var allUtilities = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
  		for(currFunctionVertex <- allMarginalUtilities.keys){
  			if(currFunctionVertex != functionVertex || allMarginalUtilities.size == 1){ 
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
  					  var variableAssignments = functions.apply(currVariableVertexId)
  					  if(variableAssignments.contains(assignment)){
  						  var costs = variableAssignments.apply(assignment)
  						  fullAssignmentCost += costs
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

	  for(function <- marginalUtilities.keys){
	    var meeting : Int = function.toString().substring(1).toInt
	    var unorderedUtilities = marginalUtilities.apply(function)
	    var orderedUtilities = unorderedUtilities.toList sortBy {_._2}
	    orderedUtilities = orderedUtilities.reverse
	    println(this.id + " / " + meeting + " :" + orderedUtilities)

      var accepted : Boolean = false
      var position : Int = 0 // FIXME test 0
     
      while(!accepted){
  	    
        var assignment = orderedUtilities(position)
        var conflict : Boolean = false
        
        for(indexedMeeting <- index.keys){
          if(indexedMeeting != meeting){
             if(index.apply(indexedMeeting) == assignment._1){
               conflict = true
             }
           }
        }
        
        if(!conflict){
            println(id + " position: " + position + " | assignment: " + assignment._1)
            bestValueAssignment += (meeting -> assignment._1)
            index += meeting -> assignment._1
            accepted = true
        }
        else {
            position = Random.nextInt(orderedUtilities.size) // FIXME test 1
        }
      }
	  }
	  
	  bestValueAssignment
	}
	
	def calculateUtility(bestValueAssignments : Map[Int,Int]) : Double = {
	  var utility : Double = 10 * bestValueAssignments.size
	  for(chosenTimeslot <- bestValueAssignments.values){
		  if(originalConstraints.hard.contains(chosenTimeslot)){
		    utility -= 10
		  }
		  else if(originalConstraints.soft.contains(chosenTimeslot)){
		    utility -= 5
		  }
	  }
	  utility
	}
  
  def calculateOriginUtilities() : Map[Any, Map[Int, Double]] = {
     var utilValueMap = Map[Int, Double]()
      for (value <- 1 to valueSpace){
        if(constraints.hard.contains(value)){
          utilValueMap += (value -> HARD_UTILITY)
        }
        else if (constraints.soft.contains(value)){
          utilValueMap += (value -> SOFT_UTILITY)
        }
        else if (constraints.preference.values.toList.contains(value)){
          utilValueMap += (value -> PREF_UTILITY)
        }
      }
     
     var finalUtilities = Map[Any, Map[Int, Double]]()
     finalUtilities += (id -> utilValueMap)
     finalUtilities
  }

	// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
	def collect() = {

		if(initialized){
		  
//		   allUtilities: function -> variable -> timeslot -> utility
			val receivedUtilities = Map[Any, Map[Any, Map[Int, Double]]]()
			for (signal <- signals.iterator) {
			  
				try {
					var message : MaxSumMessage = signal
//					var costAssignments = message.allUtilities
//					var sender : Any = message.sender
					receivedUtilities += (message.sender -> message.utilities)
				}
				catch {
				  case e : Exception => println("signal was null")
				}
			}

//			// prepare utilities
			val allUtilities = buildMarginalUtilities(receivedUtilities, valueSpace)
//			
//			// find best assignments for all requirements
			val bestValueAssignment = findBestValueAssignment(allUtilities)
//			constraints.preference = bestValueAssignment
//			
//			// adjust softConstraints // FIXME also hardconstraints
//			var newSoftConstraints = Set[Int]()
//			for(assignment <- 1 to valueSpace){
//				if(bestValueAssignment.contains(assignment)){}
//				else if(constraints.hard.contains(assignment)){}
//				else {
//				  newSoftConstraints += assignment
//				}
//			}
//			softConstraints = newSoftConstraints
			
			agentUtility = calculateUtility(bestValueAssignment)
//			
//			// Push current utility
//			val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + agentUtility)
//			val result = Http(svc OK as.String)
//			
//			lastUtility = agentUtility
//      
//      println(id + ": " + bestValueAssignment)

			new MaxSumMessage(id, allUtilities) //id, hardConstraints, newSoftConstraints, bestValueAssignment
		}
		else {
			initialized = true
      new MaxSumMessage(id, calculateOriginUtilities())
//			initialState
		}
	}
}
