package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

object MaxSum extends App {
	
	// initialize bipartite graph
  val graph = GraphBuilder.withConsole(true,8091).build
  
  // vertices
  graph.addVertex(new VariableVertex(1,1))
  graph.addVertex(new VariableVertex(2,2))
  graph.addVertex(new FunctionVertex(3,new MeetingProposal(null,null,null)))
  
  // edges
  graph.addEdge(1, new StateForwarderEdge(3))
  graph.addEdge(2, new StateForwarderEdge(3))
  graph.addEdge(3, new StateForwarderEdge(1))
  graph.addEdge(3, new StateForwarderEdge(2))
  
  // start
  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = graph.execute(execConfig)

}
