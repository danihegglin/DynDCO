package ch.uzh.dyndco.algorithms.dyndco.complete.sbdo

import scala.util.Random
import com.signalcollect.DataGraphVertex
import dispatch._ Defaults._
import scala.collection.mutable.MutableList

class SBDOVertex(id: Any, schedule: Int, numTimeslots: Int) extends DataGraphVertex(id, schedule) {
	//this(id, numColors, initialColor, isFixedfalse)super(id, initialColor)()

	//-------------------------- SYSTEM VARIABLES ----------------------------------------------
  
	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = Int

	/**
	 * Variable that indicates if the neighbors of this vertex should be informed
	 * about its color choice. This is the case if the color has changed or if the color is the same but a conflict persists.
	 */
	var informNeighbors: Boolean = false
	
	/**
	 * Initial random
	 */
	var random: Random = new Random
	
	//-------------------------- SCHEDULE & UTILITY VARIABLES ----------------------------------------------
	
	/** The set of available colors */
	val timeslots: Set[Int] = (1 to numTimeslots).toSet
	
	/**
	 * Initial schedule FIXME 
	 */
	//var initialSchedule : Int = getRandomSchedule
	
	/**
	 * The blocked timeslots
	 */
	var blocks : List[Int] = getRandomBlocks
	
	/**
	 * Finish boolean
	 */
	var finished : Boolean = false
	
	/**
	 * Current Agent Utility
	 */
	var utility : Double = 0.0;
	
	//-------------------------- SCHEDULE & UTILITY FUNCTIONS ----------------------------------------------
	
	/** Returns a random schedule */
	def getRandomSchedule: Int = {
	  random.nextInt(numTimeslots) + 1
	}
	
	/** Returns random blocked timeslots */ //TODO: Make multiple values
	def getRandomBlocks : List[Int] = {
	  var blocks : List[Int] = List(random.nextInt(numTimeslots))
	  blocks
	}
	
	/**
	 * Receive change notification
	 */
	//TODO: Implement
	
	/**
	 * Calculate utility
	 */
	def calculateUtility: Double = {
	   random.nextDouble();
	}
	
	// ------------------------ SBDO VARIABLES ----------------------------
	
	// ADD OBJECTIVES
	var addObjectives : List[Objective] = List()
	
	// REMOVE OBJECTIVES
	var removeObjectives : List[Objective] = List()
	
	// ADD CONSTRAINTS
	var addConstraints : List[Constraint] = List()
	
	// REMOVE CONSTRAINTS
	var removeConstraints : List[Constraint] = List()
	
	// ISGOODS
	var isGoods : List[Int] = List()
	
	// NOGOODS
	var noGoods : MutableList[Int] = MutableList()
	
	// VIEW	
	var view : Map[Int, Double] = Map[Int,Double]
	
	// RECV
	var recv : Map[SBDOVertex,List[Int]] = Map[SBDOVertex,List[Int]]
	
	// SUPPORT NEIGHBOR
	var support : SBDOVertex = null
	
	// ------------------------ SBDO FUNCTIONS ----------------------------
	
	/** receive-isgood */
	def receiveIsGood(sender: SBDOVertex, isGood : Int) = {
	  
	  // Check if received isGood Value is consistent with the
	  // domain of our variable -> if timeslot is not blocked
	  for(block <- blocks){
		  if(blocks == isGood){
		    sendNoGood(sender)
		  }
	  }
	  
	}
	
	/** receive-nogood */
	def receiveNoGood(sender : SBDOVertex, newNoGood : Int) = {
	  
	  // Check if already inserted in noGoods
	  for(previousNoGood <- noGoods){
	    if(newNoGood == previousNoGood){
	      return
	    }
	  }
	  
	  // Add it to noGoods
	 noGoods += newNoGood
	 
	 // Check if received isGood Value is consistent with the
	  // domain of our variable -> if timeslot is not blocked
	 var consistent : Boolean = true
	  for(block <- blocks){
		  if(blocks == newNoGood){
		    consistent == false
		  }
	  }
	  if(consistent == false){
	    return
	  }
	 
	  // Run all isGoods and invalidate
	  for(isGood <- isGoods){
	    if(isGood = newNoGood){
	      sendNoGood(sender)
	    }
	  }
	  
	}
	
	/** select-support */
	def selectSupport() = {
	  
	  // Check if on value from the recv is better than view
	  // FIXME implement
	  
	  // Change support neighbor that has this value
	  // FIXME implement
	  
	  // Update own view to the superior view
	  updateView()
	  
	  // Needs to prevent: different variables, out of date, propagate cycle, better according to ordering
	  
	}
	
	/** update-view */
	def updateView() = {
	  
	}
	
	/** send-nogood */
	def sendNoGood(receiver : SBDOVertex) = {
	  
	  // Create subset of inconsistent values from recv of user (as minimal as possible)
	  // FIXME implement
	  
	  // Send this list to receiver
	  
	  // Remove receiver as support
	  if(receiver == support){
	    support = this
	  }
	}
	
	/** send-isgood*/
	def sendIsGood(receiver : SBDOVertex) = {}
	
	/** receive-add-constraint */
	def receiveAddConstraint(constraint : Constraint) = {}
	
	/** receive-remove-constraint */
	def receiveRemoveConstraint(constraint : Constraint) = {}
	
	/** receive-add-objective */
	def receiveAddObjective(objective : Objective) = {}
	
	/** receive-remove-objective */
	def receiveRemoveObjective(objective : Objective) = {}
	
	// -------------------------- TERMINATION CRITERION -----------------------------------------
	
	override def scoreSignal: Double = {
      
	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
	  
	  if(this.finished) 
	    0
	   else
	     1
     }

	
	//-------------------------- COLLECT ----------------------------------------------

	def collect() = {
	  
	  // Categorize Signal Types
	  
	  
	  // Process No-Goods (FIFO Order)
	  
	  // Process Remove-Constraint (FIFO Order)
	  
	  // Process Add-Constraint (FIFO Order)
	  
	  // Process Remove-Objectives (FIFO Order)
	  
	  // Process Add-Objectives (FIFO Order)
	  
	  // Process Is-Goods (FIFO Order)
	  
	  // Select Support
	  
	  // Send Is-Good
	  
	  
	}
}