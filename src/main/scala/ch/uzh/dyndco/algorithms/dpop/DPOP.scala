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
    val DpopGraph = DPOPGraphFactory.build(problem)
    
    /**
     * Run the graph
     */ 
    val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
    val stats = DpopGraph.graph.execute(execConfig)
    DpopGraph.graph.shutdown
  	
    /**
     * Results
     */
  	println(stats)
  	DpopGraph.graph.foreachVertex(println(_))
  	
  //	for(agentVertex <- agentVertices){
  //	  println("----------" + agentVertex.id + "---------------")
  //		agentVertex.show()
  //	}
    }
	
}
