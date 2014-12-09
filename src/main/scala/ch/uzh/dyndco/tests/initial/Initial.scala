package ch.uzh.dyndco.tests.initial

import com.signalcollect._
import scala.util.Random
import com.signalcollect.configuration.ExecutionMode

object Initial extends App {
  
	// configuration
	println("configuration");
	val numberOfAgents : Integer = 1000;
	val numberOfTimeslots : Integer = 24;

	// initialize graph
	println("initialize graph");
	val graph = GraphBuilder.withConsole(true,8091).build
	
	// build vertices
	println("starting to create agents");
	for(vertexID <- 1 to numberOfAgents){
		println("creating agent:" + vertexID);
		graph.addVertex(new InitialVertex2(vertexID, Random.nextInt(numberOfTimeslots) + 1, numberOfTimeslots))
	}
	
	// add edges
	for(senderID <- 1 to numberOfAgents){
		for(targetID <- 1 to numberOfAgents)
			if(senderID != targetID){
				graph.addEdge(senderID, new StateForwarderEdge(targetID))
			}
	}
	
	// execute
	println("starting the graph");
	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = graph.execute(execConfig)
	
	// show run info
	println(stats)
	graph.foreachVertex(println(_))
	
	// shutdown graph
	graph.shutdown
}