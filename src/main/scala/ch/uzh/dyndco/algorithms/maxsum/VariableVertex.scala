package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import scala.collection.SortedMap
import scala.util.Random
import ch.uzh.dyndco.dynamic.DynamicVertex
import ch.uzh.dyndco.problems.Constraints
import ch.uzh.dyndco.util.Monitoring
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
//import spray.json._
//import DefaultJsonProtocol._

class VariableVertex (
		id: Any, 
		initialState: MaxSumMessage, 
		valueSpace: Int,
    constraints_ : Constraints,
    agentIndex : Map[Any, Int],
    meetingIndex : Map[Any, Int],
    meetingID : Int
		) extends DynamicVertex(id, initialState) {
  
  final var HARD_UTILITY_N : Double = 0
  final var SOFT_UTILITY_N : Double = 0.75
  final var PREF_UTILITY_N : Double = 1
  
  var MIN_VALUE : Double = 0
  var MAX_VALUE : Double = 0
  
  /**
   * Extended Config
   */
  final var CHANGE_ROUND : Int = 10
  var roundCount = 0
  var messages : Map[String, Double] = Map[String, Double]()
  var MESSAGES_MAX = 250
  var PUSH_ROUND = 250 // FIXME make distributed

  /**
   * Meeting Value
   */
  var bestValueAssignment : Int = -1 // Contains best combination of assignments for the greater good
	var bucketHistory : Set[Int] = Set[Int]()
  
	/**
	 * The Utility
	 */
  var constraints = constraints_
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
	def buildUtilities(allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]]): Map[Any,Map[Int, Double]] = {
	  
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
  		for(assignment : Int <- 1 to valueSpace){
  
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
  		marginalUtilities += (functionVertex -> assignmentMap)
	  }
	  marginalUtilities
	}
  
  /**
   * Normalize
   */
  def normalize(utility : Double) : Double = {
    var normalized : Double = 0.0
    
    MAX_VALUE = (meetingIndex.size - 1) * (meetingIndex.size)
    
    if(utility > 0){
      
      try {
        normalized = (utility - MIN_VALUE) / (MAX_VALUE - MIN_VALUE)
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

	    var orderedUtilities = allUtilities.toList sortBy {_._2}
	    orderedUtilities = orderedUtilities.reverse

      // build buckets
      var lastValue : Double = -1
      var bucketCount : Int = 0
      var buckets : Map[Int, MutableList[Int]] = Map[Int,MutableList[Int]]()
      var list : MutableList[Int] = MutableList[Int]()
      var count : Int = 0
      for(utility <- orderedUtilities){
        
        count += 1
        
        if(lastValue < 0){
          lastValue = utility._2
        }
        if(utility._2 != lastValue || count == orderedUtilities.size){
          val finalSet = list.sortWith(_ < _) // smallest timeslot first
          buckets += (bucketCount -> finalSet)
          bucketCount += 1
          list = MutableList[Int]()
          lastValue = utility._2
        }
        list += utility._1
      }
      
      // check
      var accepted : Boolean = false
      var position_top : Int = 0 // FIXME test 0
      var position_sub : Int = 0
     
      while(!accepted){
        
        // Retrieve List
        var curList : MutableList[Int] = MutableList[Int]()
        var assignment : Int = -1
  	    
        try {
          curList = buckets.apply(position_top)
          assignment = curList.apply(position_sub)
        } catch {
           case e : Exception => println("BUCKET ERROR: " + position_top + " | " + position_sub + " | " + buckets + " | " + curList.length)
        }
        
        var conflict : Boolean = false
        
        // index check
        for(meeting <- agentIndex.keys){
          if(meeting != meetingID){
             if(agentIndex.apply(meeting) == assignment){
               conflict = true
             }
           }
        }
        
        // history check FIXME test
        if(bucketHistory.contains(assignment)){
          conflict = true
        }
      
        if(!conflict){
            bestValueAssignment = assignment
            agentIndex += (meetingID -> assignment)
            bucketHistory.add(assignment)
            accepted = true
        }
        else {
          
           if(curList.length > (position_sub + 1)){
               position_sub += 1          
           }
           else {
              if(buckets.size > (position_top + 1)){
                position_top += 1
              }
              else {
                position_top = 0
              }
              position_sub = 0
              bucketHistory.clear()
           }
        }
      }

    meetingIndex += (id -> bestValueAssignment)
	  
	  bestValueAssignment
	}
	
  /**
   * Calculate Utilities for current Best Value Assignment
   */
	def calculateLocalUtility(bestValueAssignment : Int): Double = {
	  var utility : Double = PREF_UTILITY_N
		if(originalConstraints.hard.contains(bestValueAssignment)){
		  utility = HARD_UTILITY_N
		}
		else if(originalConstraints.soft.contains(bestValueAssignment)){
		  utility = SOFT_UTILITY_N
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
          utilValueMap += (value -> HARD_UTILITY_N)
        }
        else if (constraints.soft.contains(value)){
          utilValueMap += (value -> SOFT_UTILITY_N)
        }
        else if (constraints.preference.values.toList.contains(value)){
          utilValueMap += (value -> PREF_UTILITY_N)
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
//        println(meetingID + " finished -> " + refValue)
      }
//      else {
//        println(meetingID + " not finished: " + meetingIndex.values)
//      }
  }

  /**
   * Collect Signals
   */
	def collect() = {
    
    roundCount += 1
////    println("roundCount: " + roundCount)
//    if(roundCount >= 1000){
//      finished = true
//    }
//    
    
    if(roundCount >= CHANGE_ROUND){
      
//      println(id + " - CHANGE!!!!!!!!!!!!!!!!!!!!: " + roundCount)
      
//      var participations : Set[Int] = Set[Int](meetingID)
//      constraints = MeetingSchedulingFactory.buildSingleConstraints(id, participations)
      roundCount = 0
      
//      println(id + " - " + roundCount)
      
//      initialized = false
      
    }

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
//        println("v" + roundCount + "_1: " + receivedUtilities)
        
    		// prepare utilities
    		val allUtilities = buildUtilities(receivedUtilities)
    			
        if(!isNull){
    			
          // find best assignments for all requirements
          if(!finished){
      		  val bestValueAssignment = findBestValueAssignment(allUtilities)
          }
      
          // calculate local utility
      		agentUtility = calculateLocalUtility(bestValueAssignment)
      			
      	  // add current utility to messages
            val timestamp: Long = System.currentTimeMillis / 1000
            messages += timestamp.toString() -> agentUtility
            
            // Send if reached max
            if(messages.size >= MESSAGES_MAX){
              Monitoring.update(id, messages)
              messages.clear()
            }
//          println(id + ": " + agentUtility + " -> " + bestValueAssignment)
          
       
//       println("v" + roundCount + "_2: " + allUtilities)
        } 
    	 new MaxSumMessage(id, allUtilities)
      
    }
		else {
      
      // already added at initialization of vertex
//      meetingIndex += (id -> pref) // add value to index FIXME could already be too late
      
      // initialize
			initialized = true
      
      // add pref to index
			var pref = constraints.preference.apply(meetingID)
      bestValueAssignment = pref // assign best value
      
      new MaxSumMessage(id, calculateOriginUtilities())
		}
	}
}
