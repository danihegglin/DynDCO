//package ch.uzh.dyndco.algorithms.dyndco.complete.sbdo
//
//import scala.util.Random
//import com.signalcollect.DataGraphVertex
//import dispatch._, Defaults._
//
//class SBDOVertex(id: Any, schedule: Int, numTimeslots: Int) extends DataGraphVertex(id, schedule) {
//	//this(id, numColors, initialColor, isFixedfalse)super(id, initialColor)()
//
//	//-------------------------- SYSTEM VARIABLES ----------------------------------------------
//  
//	/**
//	 * Indicates that every signal this vertex receives is
//	 * an instance of Int. This avoids type-checks/-casts.
//	 */
//	type Signal = Int
//
//	/**
//	 * Variable that indicates if the neighbors of this vertex should be informed
//	 * about its color choice. This is the case if the color has changed or if the color is the same but a conflict persists.
//	 */
//	var informNeighbors: Boolean = false
//	
//	/**
//	 * Initial random
//	 */
//	var random: Random = new Random
//	
//	//-------------------------- SCHEDULE & UTILITY VARIABLES ----------------------------------------------
//	
//	/** The set of available colors */
//	val timeslots: Set[Int] = (1 to numTimeslots).toSet
//	
//	/**
//	 * Initial schedule FIXME 
//	 */
//	//var initialSchedule : Int = getRandomSchedule
//	
//	/**
//	 * Initial blocks
//	 */
//	var initialBlocks : Int = getRandomBlocks
//	
//	/**
//	 * Finish boolean
//	 */
//	var finished : Boolean = false
//	
//	/**
//	 * Current Agent Utility
//	 */
//	var utility : Double = 0.0;
//	
//	//-------------------------- SCHEDULE & UTILITY FUNCTIONS ----------------------------------------------
//	
//	/** Returns a random schedule */
//	def getRandomSchedule: Int = {
//	  random.nextInt(numTimeslots) + 1
//	}
//	
//	/** Returns random blocked timeslots */ //TODO: Make set
//	def getRandomBlocks : Int = random.nextInt(numTimeslots)
//	
//	/**
//	 * Receive change notification
//	 */
//	//TODO: Implement
//	
//	/**
//	 * Calculate utility
//	 */
//	def calculateUtility: Double = {
//	   random.nextDouble();
//	}
//	
//	// ------------------------ SBDO VARIABLES & FUNCTIONS ----------------------------
//	
//	// OBJECTIVES
//	
//	// VIEW
//	
//	// ADD CONSTRAINTS
//	
//	// REMOVE CONSTRAINTS
//	
//	// ADD OBJECTIVES
//	
//	// REMOVE OBJECTIVES
//	
//	// ISGOODS
//	
//	
//	// NOGOODS
//	Stack
//	
//	/** receive-isgood */
//	/** receive-nogood */
//	/** select-support */
//	/** update-view */
//	/** send-nogood */
//	/** send-isgood*/
//	/** receive-add-constraint */
//	/** receive-remove-constraint */
//	/** receive-add-objective */
//	/** receive-remove-objective */
//	
//	// -------------------------- TERMINATION CRITERION -----------------------------------------
//	
//	override def scoreSignal: Double = {
//      
//	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
//	  
//	  if(this.finished) 
//	    0
//	   else
//	     1
//     }
//
//	
//	//-------------------------- COLLECT ----------------------------------------------
//
//	def collect() = {
//	  
//		// Calculate utility
//		this.utility = calculateUtility;
//	  
//		// Push current utility
//		val svc = url("http://localhost:9000/utility/agent/" + id + "?utility=" + utility)
//		val result = Http(svc OK as.String)
//	  
//		if(!finished){
//			 
//		// Determine number of signals & values
//		val numberOfNeighbors : Int = signals.size
//		val values = collection.mutable.Map[Int, Int]()
//		
//		// Determine if signal matches the chosen schedule
//		var matches : Int = 1
//		for (signal <- signals.iterator) {
//		  var numOfValues : Int = 0
//		  if(values.contains(signal)){
//		    numOfValues = values(signal)
//		  }
//		  values.put(signal, numOfValues + 1)
//		  if(signal == schedule){
//		    println(id + ": value matched")
//		    matches = matches + 1
//		  }
//		}
//		
//		// If own value matches with other agent's value(s)
//		if(matches > 0){
//			println(id + ": have matches for " + state + "/" + matches)
//		  // If own value is in a majority with other agent's values -> finish
//		 // if(matches.toFloat / numberOfNeighbors > 0.5){
//		  if(matches >= 6){
//			  println(id + ": I finish")
//			  finished = true
//			  state
//		  }
//		  // Get other schedule if not
//		  else {
//		    println(id + ": creating new schedule")
//		    val newSchedule : Int = getRandomSchedule
//			 this.setState(newSchedule)
//			 newSchedule
//		  }
//		}
//		else{
//		  
//		  // Find value with most matches
//		  var highestRankedValue : Int = 0
//		  var highestNumberOfMatches : Int = 0
//		  
//		  for(value <- values.keysIterator){
//		    if(values(value) > highestNumberOfMatches){
//		      highestRankedValue = value
//		      highestNumberOfMatches = values(value)
//		    }
//		  }
//		  println(id + ": value:" + highestRankedValue + " / matches: " + highestNumberOfMatches)
//		    
//		  // Converge if level is over threshold
//		 // if(highestNumberOfMatches.toFloat / numberOfNeighbors > 0.5){
//		  if(highestNumberOfMatches > 4){
//		    println(id + ": I converge to " + highestRankedValue + " !")
//		    this.setState(highestRankedValue)
//		    finished = true
//		    highestRankedValue
//		  }
//		  else {
//			 println(id + ": creating new schedule")
//			 val newSchedule : Int = getRandomSchedule
//			 this.setState(newSchedule)
//			 newSchedule
//		  }
//		}
//		}
//		else {
//		  schedule
//		}
//	}
//}