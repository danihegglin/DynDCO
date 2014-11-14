package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import dispatch._,Defaults._
import scala.collection.SortedMap

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
	
	def buildPrefMap(allAssignmentCosts : Map[Any, Map[Any, Map[Int, Double]]], timeslots : Int) : Map[Any,Map[Int, Double]] = {
	  
	  val prefMap = Map[Any,Map[Int, Double]]() // Keeps track which meeting has which assignment
		
	  for(functionVertex <- allAssignmentCosts.keys){
	    
	    println("---------------------------------------")
	    println("running function " + functionVertex)
	    println("size of full set: " + allAssignmentCosts.size)

		  // build set of other functionVertices for the particular functionVertex target
		var assignmentCostsSet = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
		for(currFunctionVertex <- allAssignmentCosts.keys){
			if(currFunctionVertex != functionVertex || allAssignmentCosts.size < 2){ // FIXME question: what if only one function, how should I create a result when excluding this function
				var variableCosts = allAssignmentCosts.apply(currFunctionVertex)
				assignmentCostsSet += variableCosts
			}
		}
	    
	    println("prepared set: " + assignmentCostsSet)
	    println("size of prepared set: " + assignmentCostsSet.size)

		// build assignment -> costs for the particular functionVertex target
		var assignmentMap : Map[Int,Double] = Map[Int,Double]()
		for(assignment : Int <- 1 to timeslots){
//		  println("running assignment: " + assignment)

			var fullAssignmentCost : Double = 0
				    
			// process every assignment costs map
			for(assignmentCosts <- assignmentCostsSet){
//			  println("processing set " + assignmentCosts)
				// process every variable
				for(currVariableVertexId <- assignmentCosts.keys){
					// ignore if the assignment is from this particular VariableVertex
					if(currVariableVertexId != id){
					  println("running variable " + currVariableVertexId)
					  var variableAssignments = assignmentCosts.apply(currVariableVertexId)
					  // adjust assignment costs if they are set in the set
					  if(variableAssignments.contains(assignment)){
//						  println("updating costs: " + assignment)
						  var costs = variableAssignments.apply(assignment)
						  fullAssignmentCost += costs
					  }
					}
				}
			}
			assignmentMap += (assignment -> fullAssignmentCost)
		}
		// Add assignment costs to map for all functionVertices
		prefMap += (functionVertex -> assignmentMap)
		println("---------------------------------------")
	  }
	  prefMap
	}
	
	def buildFinalMap(prefMap : Map[Any,Map[Int, Double]]) : Map[Any, Int] = {
		
	  val tempPrefMap = Map[Any, Set[Int]]() // Contains best combination of assignments

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

			  	var minAss : Set[Int] = Set[Int]()
				var minCosts = Double.MaxValue
				for(assignment <- proposedAssignments.keys){
				  if(proposedAssignments.apply(assignment) < minCosts){
					  minAss = Set(assignment)
					  minCosts = proposedAssignments.apply(assignment)
				  }
				  else if (proposedAssignments.apply(assignment) == minCosts){
					  minAss += assignment
				  }
				}
				tempPrefMap += (function -> minAss)
			}
	  		
	  		// Process priority queue
	  		println("tempPrefMap: " + tempPrefMap)
			// order
	  		var orderedPrefMap = SortedMap[Int,Map[Any,Set[Int]]]()
			for(tempPref <- tempPrefMap.keys){
			  var prefSize = tempPrefMap.apply(tempPref).size
			  var prefs = tempPrefMap.apply(tempPref)
			  orderedPrefMap += (prefSize -> Map(tempPref -> prefs)) // FIXME
			}
	  		println("orderedPrefMap: " + orderedPrefMap)
	  		val finalPrefMap = Map[Any, Int]() // Contains best combination of assignments
	  		// FIXME won't work with similar sizes of solutions
	  		var blocked = Set[Any]()
	  		for(ordering <- orderedPrefMap.keys){
	  			println(ordering)
	  			var orderedMap = orderedPrefMap.apply(ordering)
	  			for(ordered <- orderedMap.keys){
		  			var prefs = orderedMap.apply(ordered) // set of preferences
		  			for(pref <- prefs){
		  			  if(!blocked.contains(pref)){
		  				  finalPrefMap += (ordered -> pref)
		  			  }
		  			}
	  			}
	  		}
	  		println("finalMap: " + finalPrefMap)
			
			finalPrefMap
	}
	
//<<<<<<< HEAD
//		// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
//  def collect() = {
//    
//    if(initialized){
//    
////    println("Variable: received signals");
//    
//     // unpack assignment costs
//    val allAssignmentCosts = Map[Any, Map[Any, Map[Int, Double]]]()
//    for (signal <- signals.iterator) {
//      var proposal : Proposal = signal
//      var sender : Any = proposal.sender
//      var costAssignments = proposal.allCostAssignments
////      println("ERROR HERE: " + sender + " -> " + costAssignments.size)
//      allAssignmentCosts += (sender -> costAssignments)
//    }
////    println("unpacked assignment costs: " + allAssignmentCosts.size)
//    
//    // 1. all functions: build for all functions
//    var minCost : Double = Double.MaxValue // FIXME: what is this value?
//    var minAssignment : Int = -1
//    
//    for(function <- allAssignmentCosts.keys){
//      
//      // build set of other functions
//      var assignmentCostsSet = Set[Map[Any,Map[Int,Double]]]() // holds functions, all variables and their costs
//      for(currFunction : Any <- allAssignmentCosts.keys){
////        println("processing function packets: " + currFunction)
//        if(currFunction != function || allAssignmentCosts.size < 2){ // FIXME question: what if only one function, how should I create a result when excluding this function
//          var variableCosts = allAssignmentCosts.apply(currFunction)
////          println("function is allowed to be included: " + variableCosts.size)
//          assignmentCostsSet += variableCosts
//        }
//      }
//      
//      // 2. all variables: take all variables except this one
//      // 3. all assignments: take all available assignments and pick the minimal one 
//      
//      // Find minimal combination for target (FIXME for all meetings!)
//      for(assignment : Int <- 1 to timeslots){
//        
////        println("testing assignment: " + assignment)
//        
//        var curCost : Double = 0
//        
//        // Run every function
//        for(assignmentCosts <- assignmentCostsSet){
////          println("going through set: " + assignmentCosts.size)
//          for(specificAssignment <- assignmentCosts.keys){
////            println("testing variable set: " + specificAssignment + "(id=" + id + ")")
//            if(specificAssignment != id){ // if the assignment is not from this variable 
//              if(assignmentCosts.contains(assignment)){
//                var assignmentMap : Map[Int,Double] = assignmentCosts.apply(assignment)
////                println("assignmentMap of variable: " + assignmentMap.size)
//                var cost = assignmentMap.apply(assignment)
////                println("cost: " + cost)
//                curCost = curCost + cost
//              }
//            }
//          }
//        }
//        
////        println("assignment: " + assignment + " -> cost: " + curCost)
//        
//        if(curCost < minCost){
//          
////          println("considering assignment")
//          
//          // Don't allow hard constraint breaches
////          if(!hardConstraints.contains(assignment)){
//            
//            // FIXME: Don't allow assignments where other preferences have been set
////            println("adding assignment");
//            preferences = Set(assignment)
//            
//            minCost = curCost
//            
////          }
//        }
//      }
//     }
//    
//    // Push current utility
//    println("Sending mincost (" + minCost + ") to monitoring")
//    val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + minCost)
//    val result = Http(svc OK as.String)
//    
//    // Adjust own preferences & build hard, soft and preferences
////   var newHardConstraints = hardConstraints // FIXME does it stay the same
////   var newSoftConstraints = softConstraints // FIXME adjust this
//    
//     new Proposal(id, hardConstraints, softConstraints, preferences)
//    }
//    else {
//      initialized = true
//      initialState
//    }
//  }
//  
//=======
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
			val prefMap = buildPrefMap(allAssignmentCosts, timeslots)
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

//>>>>>>> e57d4a9612d00e4fa12f28c05091a4a743a2f692
}

/**
 * FIXME Store the last value chosen on equal solutions, so the other value/values are tried in next run
 */
/**
 * FIXME If variable/function only receives one other signal takes local value into consideration
 */
