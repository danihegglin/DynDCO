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
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.Problem
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.deployment.DeployableAlgorithm

/**
 * Based on: FIXME
 */

object MaxSum {

  def run(problem : MeetingSchedulingProblem) = {

    /**
     * Build graph
     */
    val graph = MaxSumGraph.build(problem)
    
    /**
     * Run the graph
     */ 
    Monitoring.start()
    val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
    val stats = graph.execute(execConfig)
    graph.shutdown
    Monitoring.sucess()
    
    /**
     * Results
     */
    println(stats)
            
    for(vertex <- MaxSumGraph.varVertices){
      println(vertex.id + " -> " + vertex.bestValueAssignment)
    }
    
  }
}
