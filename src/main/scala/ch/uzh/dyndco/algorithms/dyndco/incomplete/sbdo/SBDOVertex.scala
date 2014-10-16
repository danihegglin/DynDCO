package ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo

import scala.util.Random
import dispatch._
import scala.collection.mutable.MutableList
import scala.collection.mutable.HashMap
import ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo.SBDOConstraint
import ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo.SBDOMessage
import ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo.SBDOGood
import ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo.SBDOObjective
import com.signalcollect.DataGraphVertex

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
	
	// OBJECTIVES
	var objectives : MutableList[SBDOObjective] = MutableList()
	
	// CONSTRAINTS
	var constraints : MutableList[SBDOConstraint] = MutableList()
	
	// ISGOODS
	var isGoods : MutableList[SBDOGood] = MutableList()
	
	// NOGOODS
	var noGoods : MutableList[SBDOGood] = MutableList()
	
	// VIEW	
	var view : HashMap[Int, Int] = new HashMap[Int,Int]
	
	// RECV
	var recv : HashMap[SBDOVertex,Map[Int,Int]] = new HashMap[SBDOVertex,Map[Int,Int]]
	
	// SENT
	var sent : HashMap[SBDOVertex,Map[Int,Int]] = new HashMap[SBDOVertex,Map[Int,Int]]
	
	// NEIGHBOURS
	var neighbours : MutableList[SBDOVertex] = MutableList()
	
	// SUPPORT
	var support : SBDOVertex = null
	
	// ------------------------ SBDO FUNCTIONS ----------------------------
	
	/** receive-isgood */
	def receiveIsGood(isGood : Int) = {
	  
	  // Check if received isGood Value is consistent with the
	  // domain of our variable -> if timeslot is not blocked
	  for(block <- blocks){
		  if(blocks == isGood){
//		    sendNoGood(sender) FIXME
		  }
	  }
	  
	}
	
	/** receive-nogood */
	def receiveNoGood(newNoGood : SBDOGood) : Boolean = {
	  
	  // Check if already inserted in noGoods
	  for(previousNoGood <- noGoods){
	    if(newNoGood == previousNoGood){
	      return false
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
	    return false
	  }
	 
	  // Run all isGoods and invalidate
	  for(isGood <- isGoods){
	    if(isGood == newNoGood){
//	      sendNoGood(sender) FIXME
	    }
	  }
	  
	  return true
	  
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
	def sendIsGood(receiver : SBDOVertex) = {
	  
	  // Check if recv and sent is consistent with current value
	  // FIXME implement
	  
	  // Lock communication channel with receiver
	  // FIXME implement
	  
	  // Check if no unprocessed isgoods from receiver are available
	  // then L -> min
	  // I -> isGood (I = |L|)
	  // Calculate utility U of this isgood
	  // Set U as local utility -> send this utility
	  // Remove reference to receiver from I
	  // send I to receiver
	  // set sent to I
	  
	}
	
	/** receive-add-constraint */
	def receiveAddConstraint(constraint : SBDOConstraint) = {
	  
	  // Add constraint to list
	  constraints += constraint
	  
	  // FIXME
	  // Process all agents in Constraint
//	  for(agent <- constraint.getAgents()){
//	    if(agent != this){
//	      neighbours += agent
//	    }
//	  }
	  
	  // Process all isGoods
//	  for(agent <- recv){
//	    // FIXME satisfy function needed
//	    if(recv.get(agent) != constraint){
//	      sendNoGood(agent)
//	    }
//	  }
	  
	}
	
	/** receive-remove-constraint */
	def receiveRemoveConstraint(constraint : SBDOConstraint) = {
	  
	  // Send remove constraint to all neighbors
	  // FIXME implement
	  
	  // Check if constraint is in constraints
	  // Remove from constraints
	  
	  // Process all agents
	  // If no constraints reference agent & no objectives reference a
		// -> remove a from neighbors
		// -> delete recv(a)
		// -> delete sent(a)
	  
	  // For all nogoods 
		// if constraint is in nogood.justification
			// -> remove constraint from justification
			// if nogood justification is empty
				// -> remove n from nogoods
	  
	  
	}
	
	/** receive-add-objective */
	def receiveAddObjective(objective : SBDOObjective) = {
	  
	  // add objective to objectives
	  objectives += objective
	  
	  // process all agents in objective
			  // if agent is not this
			  	// add agent to neighbors
	  
	  // update utility of view
	  // FIXME implement
	  
	}
	
	/** receive-remove-objective */
	def receiveRemoveObjective(objective : SBDOObjective) = {
	  
	  // if objective is in objectives
		// -> remove objective
	  
	  // process all agents in objective
	  
	  	  // If no constraints reference agent & no objectives reference a
		// -> remove a from neighbors
		// -> delete recv(a)
		// -> delete sent(a)
	  
	  // update utility of view
	  // FIXME implement
	}
	
	// --------------------------- HELPER METHODS -------------------------------------------
	
	def checkReferences(agents : List[SBDOVertex]) = {
	  
	}
	
	def updateUtilityOfView() = {
	  // FIXME implement
	}
	
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
	  
	  // local variables
	  var noGoods : MutableList[SBDOGood] = MutableList()
	  var isGoods : MutableList[SBDOGood] = MutableList()
	  var removeConstraints : MutableList[SBDOConstraint] = MutableList()
	  var addConstraints : MutableList[SBDOConstraint] = MutableList()
	  var removeObjectives : MutableList[SBDOObjective] = MutableList()
	  var addObjectives : MutableList[SBDOObjective] = MutableList()
	  
	  // Categorize Signal Types
		for (signal <- signals.iterator) {
		  var message : SBDOMessage = signal.asInstanceOf[SBDOMessage]  
		  if(message != null && message.getMessageType != null){
			  if(message.getMessageType == "NoGood"){
			    noGoods += message.getContent.asInstanceOf[SBDOGood]
			  }
			  else if (message.getMessageType == "isGood"){
			    isGoods += message.getContent.asInstanceOf[SBDOGood]
			  }
			  else if (message.getMessageType == "removeConstraint"){
			    removeConstraints += message.getContent.asInstanceOf[SBDOConstraint]
			  }
			  else if (message.getMessageType == "addConstraint"){
			    addConstraints += message.getContent.asInstanceOf[SBDOConstraint]
			  }
			  else if (message.getMessageType == "removeObjective"){
			    removeObjectives += message.getContent.asInstanceOf[SBDOObjective]
			  }
			  else if (message.getMessageType == "addObjective"){
			    addObjectives += message.getContent.asInstanceOf[SBDOObjective]
			  }
		  }
		}
	  
	  // Process No-Goods (FIFO Order)
	  for(noGood <- noGoods)
	    receiveNoGood(noGood)
	  
	  // Process Remove-Constraint (FIFO Order)
	  for(removeConstraint <- removeConstraints)
	    receiveRemoveConstraint(removeConstraint)
	    
	  // Process Add-Constraint (FIFO Order)
	  for(addConstraint <- addConstraints)
	    receiveAddConstraint(addConstraint)
	    
	  // Process Remove-Objectives (FIFO Order)
	  for(removeObjective <- removeObjectives)
	    receiveRemoveObjective(removeObjective)
	  
	  // Process Add-Objectives (FIFO Order)
	  for(addObjective <- addObjectives)
	    receiveAddObjective(addObjective)
	  
	  // Process Is-Goods (FIFO Order)
//	  for(isGood <- isGoods) FIXME
//	    receiveIsGood(isGood) FIXME
	  
	  // Select Support
//	  selectSupport() FIXME
	  
	  // Send Is-Good to all neighbors
//	  for(neighbour <- neighbours)
//	    view = sendIsGood(neighbour) //FIXME
	    
	    1 // FIXME

	}
}