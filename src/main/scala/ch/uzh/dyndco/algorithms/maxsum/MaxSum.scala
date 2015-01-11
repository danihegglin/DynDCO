package ch.uzh.dyndco.algorithms.maxsum

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.MeetingConstraints
import dispatch._
import dispatch.Defaults._
import ch.uzh.dyndco.util.Monitoring
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.Problem
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import ch.uzh.dyndco.stack.dynamic.DynamicController
import ch.uzh.dyndco.stack.vertex.MeetingSchedulingVertex
import ch.uzh.dyndco.stack.tests.DCOAlgorithm
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.stack.tests.TestMode
import ch.uzh.dyndco.stack.configuration.Configuration

/**
 * Based on: FIXME
 */

object MaxSum extends DCOAlgorithm {

  def run(
      problem : MeetingSchedulingProblem,
      configuration : Configuration
      ) = {

    /**
     * Build graph
     */
    val maxSumGraph = MaxSumGraphFactory.build(problem)
    
    /**
     * Build dynamic if mode is set
     */
    if(configuration.testMode != TestMode.Normal){
      
      var dynamicController = new DynamicController(maxSumGraph,problem)
    
      configuration.testMode match {
        case TestMode.DynamicConstraints => dynamicController.changeConstraints(configuration.parameters)
        case TestMode.DynamicVariables => dynamicController.changeVariables(configuration.parameters)
        case TestMode.DynamicDomain => dynamicController.changeDomain(configuration.parameters)
      }
    }
    
    /**
     * Run the graph
     */ 
    val stats = maxSumGraph.graph.execute(configuration.execConfig)
    maxSumGraph.graph.shutdown
    
    /**
     * Send remaining utilities & conflicts
     */
    for(varVertex <- maxSumGraph.getAgents()){
      Monitoring.update(varVertex.id, varVertex.updates)
      Thread sleep 20 // Otherwise too many messages at once
    }
    for(varVertex <- maxSumGraph.getAgents()){
      Monitoring.conflict(varVertex.id, varVertex.conflicts)
    }
    
    /**
     * Send & show results
     */
    var prepStats = maxSumGraph.prepareStats(stats)
    Monitoring.stats(prepStats)
    maxSumGraph.show()
    
    Thread sleep 5000 // Otherwise stop too fast
    
  }
}
