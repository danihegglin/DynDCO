package ch.uzh.dyndco.app

import com.signalcollect._
import ch.uzh.dyndco.vertices.AgentVertex
import com.signalcollect.configuration.ExecutionMode
import scala.util.Random
import akka.event.Logging.LogLevel

///**
// * 	This algorithm attempts to find a vertex coloring.
// * A valid vertex coloring is defined as an assignment of labels (colors)
// * 	to vertices such that no two vertices that share an edge have the same label.
// *
// * Usage restriction: this implementation *ONLY* works on *UNDIRECTED* graphs.
// * In Signal/Collect this means that there is either no edge between 2 vertices
// * or one in each direction.
// *
// * @param id: the vertex id
// * @param numColors: the number of colors (labels) used to color the graph
// */
//class ColoredVertex(id: Any, numColors: Int, initialColor: Int, isFixed: Boolean = false) extends DataGraphVertex(id, initialColor) {
//
//	/**
//	 * Indicates that every signal this vertex receives is
//	 * an instance of Int. This avoids type-checks/-casts.
//	 */
//	type Signal = Int
//
//			/** The set of available colors */
//			val colors: Set[Int] = (1 to numColors).toSet
//
//			/** Returns a random color */
//			def getRandomColor: Int = Random.nextInt(numColors) + 1
//
//			/**
//			 * Variable that indicates if the neighbors of this vertex should be informed
//			 * about its color choice. This is the case if the color has changed or if the color is the same but a conflict persists.
//			 */
//			var informNeighbors: Boolean = false
//
//			/**
//			 * Checks if one of the neighbors shares the same color. If so, the state is
//			 * set to a random color and the neighbors are informed about this vertex'
//			 * new color. If no neighbor shares the same color, we stay with the old color.
//			 */
//			def collect = {
//				if (signals.iterator.contains(state)) {
//					informNeighbors = true
//							if (isFixed) {
//								initialColor
//							} else {
//								val r = Random.nextDouble
//										if (r > 0.8) {
//											val freeColors = colors -- signals
//													val numberOfFreeColors = freeColors.size
//													if (numberOfFreeColors > 0) {
//														freeColors.toSeq(Random.nextInt(numberOfFreeColors))
//													} else {
//														getRandomColor
//													}
//										} else {
//											getRandomColor
//										}
//							}
//				} else {
//					informNeighbors = false || (lastSignalState.isDefined && lastSignalState.get != state)
//							state
//				}
//			}
//
//			/**
//			 * The signal score is 1 if this vertex hasn't signaled before or if it has
//			 *  changed its color (kept track of by informNeighbors). Else it's 0.
//			 */
//			override def scoreSignal = if (informNeighbors || lastSignalState == None) 1 else 0
//
//}

/**
 * Builds a Vertex Coloring compute graph and executes the computation
 *
 * StateForwarderEdge is a built-in edge type that simply sends the state
 * of the source vertex as the signal, which means that this algorithm does
 * not require a custom edge type.
 */
object DynDCO extends App {
  
	// configuration
	println("configuration");
	val numberOfAgents : Integer = 2;
	val numberOfTimeslots : Integer = 2;

	// initialize graph
	println("initialize graph");
	val graph = GraphBuilder.withConsole(true,8090).build
	
	// build vertices
	println("starting to create agents");
	for( vertexID <- 1 to numberOfAgents){
		println("creating agent:" + vertexID);
		graph.addVertex(new AgentVertex(vertexID, Random.nextInt(numberOfTimeslots) + 1, numberOfTimeslots))
	}
	
	// add edges
	graph.addEdge(1, new StateForwarderEdge(2))
	graph.addEdge(2, new StateForwarderEdge(1))
	
//	println("adding edges");
//	val switch : Boolean = false
//	for( sender <- 1 to numberOfAgents){
//		for( target <- 1 to numberOfAgents){
//			if(target != sender){
//				if(sender % 2 == 0){
//					if(sender-1 > 0)
//						graph.addEdge(sender, new StateForwarderEdge(sender-1))
//						if(sender+1 <= numberOfAgents)	
//							graph.addEdge(sender, new StateForwarderEdge(sender+1))
//				}
//				else {
//					if(switch){
//						if(sender-1 > 0)
//							graph.addEdge(sender, new StateForwarderEdge(sender-1))
//							switch == false
//					}
//					else { 	
//						if(sender+1 <= numberOfAgents)
//							graph.addEdge(sender, new StateForwarderEdge(sender+1))
//							switch == true
//					}
//				}
//			}
//		}
//	}
	
	// execute
	println("starting the graph");
	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = graph.execute(execConfig)
	
	// insert change & evaluate resilience
	
	
	// show run info
	println(stats)
	graph.foreachVertex(println(_))
	
	// shutdown graph
	graph.shutdown
}