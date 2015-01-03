package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import scala.collection.SortedMap
import scala.util.Random
import ch.uzh.dyndco.stack.DynamicVertex
import ch.uzh.dyndco.problems.MeetingConstraints
import ch.uzh.dyndco.util.Monitoring
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
//import spray.json._
//import DefaultJsonProtocol._

class VariableVertex (id: Any, initialState: MaxSumMessage) 
  extends DynamicVertex(id, initialState) {
  
  /**
   * This avoids type-checks/-casts.
   */
  type Signal = MaxSumMessage
	
  /**
   * Build Utilities for all Function Vertices
   */
	def buildUtilities(allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]]): Map[Any,Map[Int, Double]] = {
	  
	  val utilities = Map[Any,Map[Int, Double]]() // Keeps track which meeting has which assignment
		
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
  		for(assignment : Int <- 1 to TIMESLOTS){
  
  			var utility : Double = 0
  				    
  			// process every utility map
  			for(functions <- allUtilities){
  				
  			  for(currVariableVertexId <- functions.keys){
  					  var variableAssignments = functions.apply(currVariableVertexId)
  					  if(variableAssignments.contains(assignment)){
  						  utility += variableAssignments.apply(assignment)
  					  }
  				}
  			}
        
        // normalize utility
        utility = normalize(utility)
        
  			assignmentMap += (assignment -> utility)
  		}
  		// Add assignment costs to map for all functionVertices
  		utilities += (functionVertex -> assignmentMap)
	  }
	  utilities
	}
  
  /**
   * Normalize
   */
  def normalize(utility : Double) : Double = {
    var normalized : Double = 0.0
    
    var max = (MEETING_INDEX.size - 1) * (MEETING_INDEX.size) // FIXME
    
    if(utility > 0){
      
      try {
        normalized = (utility - 0) / (max - 0)
      } 
      catch {
        case e : Exception => println("normalization error")
      }
    }
    normalized
  } 
	
  /**
   * Find Best Value
   */
	def findBestValueAssignment(marginalUtilities : Map[Any,Map[Int, Double]]) : Int = {
    
      // Sum of all utilities
      var allUtilities = Map[Int,Double]()
      for(utilities <- marginalUtilities.values){
        for(timeslot <- utilities.keys){
          var utility = utilities.apply(timeslot)
          if(allUtilities.contains(timeslot)){
            utility += allUtilities.apply(timeslot)
          }
          allUtilities += (timeslot -> utility)
        }
      }
      
      // Find max value
      findMaxValue(allUtilities)
	}
	
  /**
   * Collect Signals
   */
	def collect() = {
    
    newRound()
    
		if(initialized){
      
       // check if finished
       finishedCheck()
      
  		  // build all Utilities: function -> variable -> timeslot -> utility
        var isNull : Boolean = true
  			val receivedUtilities = Map[Any, Map[Any, Map[Int, Double]]]()
  			for (signal <- signals.iterator) {
  				try {
  					var message : MaxSumMessage = signal
            for(utilities <- message.utilities.values){
              for(utility <- utilities.values){
                if(utility < 0 || utility > 0){
                  isNull = false
                }
              }
            }
  					receivedUtilities += (message.sender -> message.utilities)
  				}
  				catch {
  				  case e : Exception => 
              //println("signal was null")
  				}
  			}
        
    		// prepare utilities
    		val allUtilities = buildUtilities(receivedUtilities)
    			
        if(!isNull){
    			
          // find best assignments for all requirements
          if(!finished){
      		  value = findBestValueAssignment(allUtilities)
          }
          
          // store values of current round
          storeMessage(value)
        }
        
    	  new MaxSumMessage(id, allUtilities)
        
    }
		else {
      
      // initialize
			initialized = true
      
      // add pref to index
			value = CONSTRAINTS_CURRENT.preference.apply(MEETING_ID)
      
      var currentUtilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
      
      var utilities = Map[Any, Map[Int, Double]]()
      utilities += (id -> currentUtilities)
      
      new MaxSumMessage(id, utilities)
      
		}
	}
}
