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
import ch.uzh.dyndco.stack.configuration.Configuration
import ch.uzh.dyndco.stack.tests.TestMode
import ch.uzh.dyndco.stack.dynamic.DynamicController

object MGM extends App {
  
  def run(
      problem : MeetingSchedulingProblem, 
      configuration : Configuration
      ) = {
      
    /**
     * Build graph
     */
    val mgmGraph = MGMGraphFactory.build(problem)
    
     /**
     * Build dynamic if mode is set
     */
    if(configuration.testMode != TestMode.Normal){
      
      var dynamicController = new DynamicController("dyn1",mgmGraph,problem)
      mgmGraph.graph.addVertex(dynamicController)
    
      configuration.testMode match {
        case TestMode.DynamicConstraints => dynamicController.changeConstraints(configuration.parameters)
        case TestMode.DynamicMeetings => dynamicController.changeMeetings(configuration.parameters)
      }
    }
    
  	/**
  	 * Run the graph
  	 */	
  	val stats = mgmGraph.graph.execute(configuration.execConfig)
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
    var prepStats = mgmGraph.prepareStats(stats)
    Monitoring.stats(prepStats)
    Thread sleep 1000 // Otherwise stop too fast
    mgmGraph.show
  }
}