package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode

object MaxSum extends App {
	
  /**
   * Configuration
   */
  var timeslots : Int = 5
  
	// initialize bipartite graph
//  val graph = GraphBuilder.withConsole(true,8091).build
    val graph = GraphBuilder.build
  
//  // constraints
//  var hard1 : Set[Int] = Set(1,2,3)
//  var soft1 : Set[Int] = Set(5)
//  var preference1 : Set[Int] = Set(4)
//  var constraints1 = new Constraints(null,hard1,soft1,preference1)
//  var hard2: Set[Int] = Set(1)
//  var soft2: Set[Int] = Set(2,3,4)
//  var preference2 : Set[Int] = Set(5)
//  var constraints2 = new Constraints(null,hard2,soft2,preference2)
//  
//  // vertices
//  graph.addVertex(new VariableVertex(1,constraints1,timeslots))
//  graph.addVertex(new VariableVertex(2,constraints2,timeslots))
//  graph.addVertex(new FunctionVertex(3,null,timeslots))
//  
//  // edges
//  graph.addEdge(1, new StateForwarderEdge(3))
//  graph.addEdge(2, new StateForwarderEdge(3))
//  graph.addEdge(3, new StateForwarderEdge(1))
//  graph.addEdge(3, new StateForwarderEdge(2))
//  
  // start
//  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
//  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.PureAsynchronous)
//	val stats = graph.execute(execConfig)
	
	// show run info
//	println(stats)
//	graph.foreachVertex(println(_))
	
	// shutdown graph
//	graph.shutdown

}
