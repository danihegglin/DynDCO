package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.algorithms.maxsum.Meeting
import collection.mutable.Map
import collection.mutable.Set
import dispatch._
import dispatch.Defaults._
import ch.uzh.dyndco.util.Monitoring

///**
// * based on: A Scalable Method for Multiagent Constraint Optimization
// */
//

object DPOP extends App {
	
	/**
	 * Configuration
	 */
  val TIMESLOTS : Int = 5
	val AGENTS : Int = 2
	val MEETINGS : Int = 1
  
  /**
   * Build problem
   */
  val problem = MeetingSchedulingFactory.build(TIMESLOTS,MEETINGS,AGENTS)
  
	/**
   * Build graph
   */
  val graph = DPOPGraph.build(problem)
  
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
	println(stats)
	graph.foreachVertex(println(_))
	
//	for(agentVertex <- agentVertices){
//	  println("----------" + agentVertex.id + "---------------")
//		agentVertex.show()
//	}
	
}
