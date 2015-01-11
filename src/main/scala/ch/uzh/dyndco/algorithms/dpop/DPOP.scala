package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import scala.collection.mutable.MutableList
import collection.mutable.Map
import collection.mutable.Set
import dispatch._
import dispatch.Defaults._
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import ch.uzh.dyndco.stack.configuration.Configuration
import ch.uzh.dyndco.stack.dynamic.DynamicController
import ch.uzh.dyndco.stack.tests.TestMode

///**
// * based on: A Scalable Method for Multiagent Constraint Optimization
// */
//

object DPOP extends App {
  
    def run(
        problem : MeetingSchedulingProblem, 
        configuration : Configuration
      ) = {	
      
  	/**
     * Build graph
     */
    val dpopGraph = DPOPGraphFactory.build(problem)
    
    /**
     * Build dynamic if mode is set
     */
    if(configuration.testMode != TestMode.Normal){
      
      var dynamicController = new DynamicController(dpopGraph,problem)
    
      configuration.testMode match {
        case TestMode.DynamicConstraints => dynamicController.changeConstraints(configuration.parameters)
        case TestMode.DynamicVariables => dynamicController.changeVariables(configuration.parameters)
        case TestMode.DynamicDomain => dynamicController.changeDomain(configuration.parameters)
      }
    }
    
    /**
     * Run the graph
     */ 
    val stats = dpopGraph.graph.execute(configuration.execConfig)
    dpopGraph.graph.shutdown
    
    /**
     * Send remaining utilities
     */
    for(varVertex <- dpopGraph.getAgents()){
      Monitoring.update(varVertex.id, varVertex.updates)
      Thread sleep 20 // Otherwise too many messages at once
    }
  	
    /**
     * Results
     */
    var prepStats = dpopGraph.prepareStats(stats)
    Monitoring.stats(prepStats)
    Thread sleep 1000 // Otherwise stop too fast
    dpopGraph.show
    
    }
	
}
