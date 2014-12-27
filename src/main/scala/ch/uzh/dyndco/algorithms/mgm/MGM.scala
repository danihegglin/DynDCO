package ch.uzh.dyndco.algorithms.mgm

/**
 * Chapman (2011): A unifying framework for iterative approximate
 * best-response algorithms for distributed constraint
 * optimization problems. P.440.
 */

import dispatch._
import dispatch.Defaults._
import collection.mutable.Set
import collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import scala.collection.mutable.MutableList
import com.signalcollect.GraphBuilder
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.Meeting
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.util.Monitoring

object MGM extends App {
  
	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 20 // args(0)
  var MEETINGS : Int = 1 // args(2)
	var AGENTS : Int = 3 // args(1)
  
  /**
   * Build problem
   */
  val problem = MeetingSchedulingFactory.build(TIMESLOTS,MEETINGS,AGENTS)
    
  /**
   * Build graph
   */
  val mgmGraph = MGMGraphFactory.build(problem)
  
	/**
	 * Run the graph
	 */	
	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = mgmGraph.graph.execute(execConfig)
  mgmGraph.graph.shutdown
  
  /**
   * Results
   */
  // show run info
  println(stats)
          
  // agents
  for(vertex <- mgmGraph.vertices){
    println(vertex.id + " -> " + vertex.values)
  }
}