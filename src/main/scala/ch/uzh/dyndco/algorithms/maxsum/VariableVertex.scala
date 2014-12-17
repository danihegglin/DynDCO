package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import scala.collection.SortedMap
import scala.util.Random
import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.problems.Constraints
import ch.uzh.dyndco.util.Monitoring

class VariableVertex (
		id: Any, 
		initialState: MaxSumMessage, 
		valueSpace: Int,
    constraints : Constraints,
    agentIndex : Map[Any, Int],
    meetingIndex : Map[Any, Int],
    meetingID : Int
		) extends DynamicVertex(id, initialState) {
  
  /**
   * Config
   */
  final var HARD_UTILITY : Double = -10
  final var SOFT_UTILITY : Double = 0
  final var PREF_UTILITY : Double = 10

  /**
   * Meeting Value
   */
  var bestValueAssignment : Int = -1 // Contains best combination of assignments for the greater good
	
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
	  if(this.finished) 0
		else 1
  }
	
  /**
   * Build Utilities
   */
	def buildUtilities(allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]], timeslots : Int): Map[Any,Map[Int, Double]] = {
	  
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
	
  /**
   * Find Best Value
   */
	def findBestValueAssignment(marginalUtilities : Map[Any,Map[Int, Double]]) : Int = {

	  for(function <- marginalUtilities.keys){
	    var unorderedUtilities = marginalUtilities.apply(function)
	    var orderedUtilities = unorderedUtilities.toList sortBy {_._2}
	    orderedUtilities = orderedUtilities.reverse
//
      var accepted : Boolean = false
      var position : Int = 0 // FIXME test 0
     
      while(!accepted){
  	    
        var assignment = orderedUtilities(position)
        var conflict : Boolean = false
        
        for(meeting <- agentIndex.keys){
          if(meeting != meetingID){
             if(agentIndex.apply(meeting) == assignment._1){
               conflict = true
             }
           }
        }
      
        if(!conflict){
            bestValueAssignment = assignment._1
            agentIndex += (meetingID -> assignment._1)
            accepted = true
        }
        else {
//            position = Random.nextInt(orderedUtilities.size) // FIXME test 1
              position += 1 // FIXME test 2
        }
      }
	  }
    
    meetingIndex += (id -> bestValueAssignment)
    println(id + ": " + bestValueAssignment + " | " + agentIndex)
	  
	  bestValueAssignment
	}
	
  /**
   * Calculate Utilities for current Best Value Assignment
   */
	def calculateLocalUtility(bestValueAssignment : Int): Double = {
	  var utility : Double = 10
		if(originalConstraints.hard.contains(bestValueAssignment)){
		  utility -= 20
		}
		else if(originalConstraints.soft.contains(bestValueAssignment)){
		  utility -= 10
		}
	  utility
	}
  
  /**
   * Calculate Utilities from Constraints
   */
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
  
  /**
   * Check if Meeting Scheduling is finished
   */
  def finishedCheck() = {
      var same : Boolean = true
      var refValue : Int  = meetingIndex.values.toList(0)
      for(value <- meetingIndex.values){
        if(value != refValue)
          same = false
      }
      if(same){
        finished = true
      }
  }

  /**
   * Collect Signals
   */
	def collect() = {

		if(initialized){
      
      // check if finished
      finishedCheck()
		  
		  // build all Utilities: function -> variable -> timeslot -> utility
			val receivedUtilities = Map[Any, Map[Any, Map[Int, Double]]]()
			for (signal <- signals.iterator) {
				try {
					var message : MaxSumMessage = signal
					receivedUtilities += (message.sender -> message.utilities)
				}
				catch {
				  case e : Exception => println("signal was null")
				}
			}
      
			// prepare utilities
			val allUtilities = buildUtilities(receivedUtilities, valueSpace)
			
			// find best assignments for all requirements
			val bestValueAssignment = findBestValueAssignment(allUtilities)

      // calculate local utility
			agentUtility = calculateLocalUtility(bestValueAssignment)
			
			// Push current utility to monitoring
      Monitoring.update(id, agentUtility)
			
			new MaxSumMessage(id, allUtilities)
    }
		else {
      
      // initialize
			initialized = true
      
      // add pref to index
      meetingIndex += (id -> constraints.preference.apply(meetingID)) // add value to index
      
      new MaxSumMessage(id, calculateOriginUtilities())
		}
	}
}
