package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import org.slf4j.LoggerFactory
import collection.mutable.Set

object MaxSum extends App {
  
   /**
   * Logger
   */
//   val log = Logger(LoggerFactory.getLogger("name"))
	
  /**
   * Configuration
   */
  var timeslots : Int = 5
  println("Configuration")
  
	// initialize bipartite graph
  val graph = GraphBuilder.withConsole(true,8091).build
  println("Graph initialized")
  
  // constraints FIXME generate automatically
  var hard1 : Set[Int] = Set(1,2,3)
  var soft1 : Set[Int] = Set(5)
  var preference1 : Set[Int] = Set(4)
  var constraints1 = new Proposal(1,hard1,soft1,preference1)
  var hard2: Set[Int] = Set(1)
  var soft2: Set[Int] = Set(2,3,4)
  var preference2 : Set[Int] = Set(5)
  var constraints2 = new Proposal(2,hard2,soft2,preference2)
  var hard3: Set[Int] = Set()
  var soft3: Set[Int] = Set(2,3,5)
  var preference3 : Set[Int] = Set(4)
  var constraints3 = new Proposal(3,hard3,soft3,preference3)
  println("Build constraints")
  
  // vertices FIXME generate automatically
  var variable1 = new VariableVertex(1,constraints1,timeslots)
  var variable2 = new VariableVertex(2,constraints2,timeslots)
  var variable3 = new VariableVertex(3,constraints3,timeslots)
  graph.addVertex(variable1)
  graph.addVertex(variable2)
  graph.addVertex(variable3)
  graph.addVertex(new FunctionVertex(4,null,timeslots))
  println("Build vertices")
  
  // edges FIXME generate automatically
  graph.addEdge(1, new StateForwarderEdge(4))
  graph.addEdge(2, new StateForwarderEdge(4))
  graph.addEdge(3, new StateForwarderEdge(4))
  
  graph.addEdge(4, new StateForwarderEdge(1))
  graph.addEdge(4, new StateForwarderEdge(2))
  graph.addEdge(4, new StateForwarderEdge(3))
  println("Build edges")
  
  // start
  println("Start graph")
  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
  val stats = graph.execute(execConfig)
	
	// show run info
	println(stats)
	graph.foreachVertex(println(_))
	
	variable1.show()
	variable2.show()
	variable3.show()
	
	// shutdown graph
	graph.shutdown

}
