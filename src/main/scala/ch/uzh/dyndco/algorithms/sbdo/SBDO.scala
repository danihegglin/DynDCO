package ch.uzh.dyndco.algorithms.sbdo;
//package ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo
//
//import com.signalcollect._
//import scala.util.Random
//import com.signalcollect.configuration.ExecutionMode
//import ch.uzh.dyndco.algorithms.dyndco.incomplete.sbdo.SBDOVertex
//
///**
// * Siehe paper
// */
//object SBDO extends App {
//  
//	// configuration
//	println("configuration");
//	val numberOfAgents : Integer = 10000;
//	val numberOfTimeslots : Integer = 3;
//
//	// initialize graph
//	println("initialize graph");
//	val graph = GraphBuilder.withConsole(true,8090).build
//	
//	// build vertices
//	println("starting to create agents");
//	for(vertexID <- 1 to numberOfAgents){
//		println("creating agent:" + vertexID);
//		graph.addVertex(new SBDOVertex(vertexID, Random.nextInt(numberOfTimeslots) + 1, numberOfTimeslots))
//	}
//	
//	// add edges
//	for(senderID <- 1 to numberOfAgents){
//		for(targetID <- 1 to numberOfAgents)
//			if(senderID != targetID){
//				graph.addEdge(senderID, new StateForwarderEdge(targetID))
//			}
//	}
//	
//	// execute
//	println("starting the graph");
//	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
//	val stats = graph.execute(execConfig)
//	
//	// insert change & evaluate resilience
//	
//	
//	// show run info
//	println(stats)
//	graph.foreachVertex(println(_))
//	
//	// shutdown graph
//	graph.shutdown
//}