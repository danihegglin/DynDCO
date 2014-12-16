package ch.uzh.dyndco.algorithms.maxsum

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.Constraints
import dispatch._
import dispatch.Defaults._
import ch.uzh.dyndco.util.Monitoring

/**
 * Based on: FIXME
 */

object MaxSum extends App {

	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 10 // args(0)
	var AGENTS : Int = 5 // args(1)
	var MEETINGS : Int = 3 // args(2)
  
  /**
   * Build problem
   */
  val problem = MeetingSchedulingFactory.build(TIMESLOTS,MEETINGS,AGENTS)
  
  /**
   * Build graph
   */
  val graph = MaxSumGraph.build(problem)
  
  /**
   * Run the graph
   */ 
  Monitoring.start()
  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
  val stats = graph.execute(execConfig)
  graph.shutdown
  Monitoring.stop()
  
  /**
   * Results
   */
    // show run info
  println(stats)
  
  //    // show run info
//    println(stats)
////    graph.foreachVertex(println(_))
//          
//    // show results
//    for(variableVertex <- variableVertices){
//      println("----------" + variableVertex.id + "---------------")
//      variableVertex.show()
//    }
//    for(agent <- agentIndices.keys){
//      println(agent + " -> " + agentIndices.apply(agent))
//    }
          
}
