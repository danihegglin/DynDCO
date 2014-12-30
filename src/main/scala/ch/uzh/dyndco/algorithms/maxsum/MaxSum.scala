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
import ch.uzh.dyndco.stack.DynamicController

/**
 * Based on: FIXME
 */

object MaxSum {

  def run(problem : MeetingSchedulingProblem) = {

    /**
     * Build graph
     */
    val maxSumGraph = MaxSumGraphFactory.build(problem)
    
    /**
     * Add dynamic controller
     */
    maxSumGraph.graph.addVertex(new DynamicController("dyn1",maxSumGraph.graph))
    
    /**
     * Run the graph
     */ 
    val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
    val stats = maxSumGraph.graph.execute(execConfig)
    maxSumGraph.graph.shutdown
    
    /**
     * Send remaining utilities
     */
    for(varVertex <- maxSumGraph.varVertices){
      Monitoring.update(varVertex.id, varVertex.messages)
      Thread sleep 20 // Otherwise too many messages at once
    }
    
    /**
     * Results
     */
    println(stats)
    maxSumGraph.show
    
  }
}
