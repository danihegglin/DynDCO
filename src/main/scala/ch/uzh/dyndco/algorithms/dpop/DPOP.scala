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

///**
// * based on: A Scalable Method for Multiagent Constraint Optimization
// */
//

object DPOP extends App {
  
    def run(problem : MeetingSchedulingProblem) = {
	
  	/**
     * Build graph
     */
    val dpopGraph = DPOPGraphFactory.build(problem)
    
    /**
     * Run the graph
     */ 
    val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
    val stats = dpopGraph.graph.execute(execConfig)
    dpopGraph.graph.shutdown
    
    /**
     * Send remaining utilities
     */
    for(varVertex <- dpopGraph.getAgents()){
      Monitoring.update(varVertex.id, varVertex.messages)
      Thread sleep 20 // Otherwise too many messages at once
    }
  	
    /**
     * Results
     */
    /**
     * Results
     */
    println(stats)
    dpopGraph.show
    
    }
	
}
