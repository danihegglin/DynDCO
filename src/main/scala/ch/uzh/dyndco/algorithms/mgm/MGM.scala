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
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

object MGM extends App {
  
  def run(problem : MeetingSchedulingProblem) = {
      
    /**
     * Build graph
     */
    val mgmGraph = MGMGraphFactory.build(problem)
    
  	/**
  	 * Run the graph
  	 */	
  	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
  	val stats = mgmGraph.graph.execute(execConfig)
    mgmGraph.graph.shutdown
    
    /**
     * Send remaining utilities
     */
    for(vertex <- mgmGraph.getAgents()){
      Monitoring.update(vertex.id, vertex.messages)
      Thread sleep 20 // Otherwise too many messages at once
    }
    
    /**
     * Results
     */
//    println(stats)
//    mgmGraph.show
  }
}