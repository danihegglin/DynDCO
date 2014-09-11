package ch.uzh.dyndco.vertices

import scala.util.Random
import com.signalcollect.DataGraphVertex


class AgentVertex(id: Any, schedule: Int, numTimeslots: Int) extends DataGraphVertex(id, schedule) {
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
	 * Initial fixed state
	 */
//	var hasRun: Boolean = false
	
	//-------------------------- RELEVANT VARIABLES ----------------------------------------------
	
	/** The set of available colors */
	val timeslots: Set[Int] = (1 to numTimeslots).toSet
	
	/**
	 * Initial schedule FIXME 
	 */
	//var initialSchedule : Int = getRandomSchedule
	
	/**
	 * Initial blocks
	 */
	var initialBlocks : Int = getRandomBlocks
	
	/**
	 * Finish boolean
	 */
	var finished : Boolean = false
	
	//-------------------------- ADDITIONAL FUNCTIONS ----------------------------------------------
	
	/** Returns a random schedule */
	def getRandomSchedule: Int = {
	  Random.nextInt(numTimeslots) + 1
	}
	
	/** Returns random blocked timeslots */ //TODO: Make set
	def getRandomBlocks : Int = Random.nextInt(numTimeslots)
	
	/**
	 * Receive change notification
	 */
	//TODO: Implement
	
	
	// -------------------------- TERMINATION CRITERION -----------------------------------------
	
	/**
	 * The signal score is 1 if this vertex hasn't signaled before or if it has
	 *  changed its color (kept track of by informNeighbors). Else it's 0.
	 */
	//override def scoreSignal = if (informNeighbors || lastSignalState == None) 1 else 0
	override def scoreSignal: Double = {
      println(id + ": running scoreSignal: " + lastSignalState)
//      if(this.finished == true){
//        0
//      }
//      else {
//        1
//      }
	 lastSignalState match {
      case Some(oldState) if oldState == state => 0
      case noStateOrStateChanged               => 1
      case None								   => 1
    }
        }

	
	//-------------------------- COLLECT ----------------------------------------------

	/**
	 * Checks if one of the neighbors shares the same color. If so, the state is
	 * set to a random color and the neighbors are informed about this vertex'
	 * new color. If no neighbor shares the same color, we stay with the old color.
	 */
	def collect() = {
    
//			informNeighbors = true
		
			// InitialChoice
//			if (hasRun == false) {
//				hasRun = true
//				println(id + ": initial choice: " + schedule)
//				schedule
//			} 
//			else {
	  
			//  var matching : Boolean = false
			  
			  while(signals.iterator.hasNext){
			    var proposal : Int = signals.iterator.next
			    
			    if(proposal == this.state){
			      println("match: " + proposal + "/" + this.state)
			      schedule
			    }
			    else {
			      println("nomatch")
//			      println(id + ": " + this.schedule + " ->" + proposal)
			   
			      
//			      println("new schedule:" + this.schedule)
			    }
//			    proposal match {
//			      case schedule => this.schedule
//			      case x if x > this.schedule => this.schedule = getRandomSchedule
//			      case x if x < this.schedule => this.schedule = getRandomSchedule
//			    }
//			    println(id + ": Signal -> " + proposal + " / " + schedule)
//			    if(proposal == schedule){
//			    	schedule
//			    }
//			   else {
//			     println("not matching")
//			    }
			  }
			  val newSchedule : Int = getRandomSchedule
			  this.setState(newSchedule)
			  newSchedule
			
//			  println("deciding")
//			  
////			  if(matching){
////			    println("match true")
////				this.setState(schedule)
////			    schedule
////			  }
////			  else {
//			    var newSchedule = getRandomSchedule
//			    println("sending new proposal")
//			    this.setState(newSchedule)
//			    newSchedule
//			  }
//			}
	}
}