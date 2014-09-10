package ch.uzh.dyndco.vertices

import scala.util.Random
import com.signalcollect.DataGraphVertex


class AgentVertex(id: Any, initialSchedule: Int, numTimeslots: Int) extends DataGraphVertex(id, initialSchedule) {
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
	var isFixed: Boolean = false
	
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
	
	
	//-------------------------- ADDITIONAL FUNCTIONS ----------------------------------------------
	
	/** Returns a random schedule */
	def getRandomSchedule: Int = Random.nextInt(numTimeslots) + 1
	
	/** Returns random blocked timeslots */ //TODO: Make set
	def getRandomBlocks : Int = Random.nextInt(numTimeslots) + 1
	
	/**
	 * Receive change notification
	 */
	
	
	/**
	 * The signal score is 1 if this vertex hasn't signaled before or if it has
	 *  changed its color (kept track of by informNeighbors). Else it's 0.
	 */
	override def scoreSignal = if (informNeighbors || lastSignalState == None) 1 else 0

	
	//-------------------------- COLLECT ----------------------------------------------

	/**
	 * Checks if one of the neighbors shares the same color. If so, the state is
	 * set to a random color and the neighbors are informed about this vertex'
	 * new color. If no neighbor shares the same color, we stay with the old color.
	 */
	def collect ()= {
		//if (signals.iterator.contains(Int)) {
			
			informNeighbors = true
		
			if (isFixed) {
				//isFixed = true
			//	println("communicating initial choice for meeting")
				initialSchedule
			} 
			else {
			  var equalityThreshold : Double = 0.6
			  val equalNeighborsCount = (signals filter (_ equals initialSchedule)).size
			  val totalNeighborsCount = signals.size
			  if (equalNeighborsCount.toFloat / totalNeighborsCount.toFloat >= equalityThreshold) {
			    println("Reached Goal for " + initialSchedule + "->" + equalNeighborsCount)
				  initialSchedule
			  } else {
				  getRandomSchedule
			  }
			}
		//} 
		//else {
		//	informNeighbors = false || (lastSignalState.isDefined && lastSignalState.get != state)
		//		state
		//}
	}
}