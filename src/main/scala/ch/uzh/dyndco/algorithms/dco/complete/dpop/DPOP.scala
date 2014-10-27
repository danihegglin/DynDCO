//package ch.uzh.dyndco.algorithms.dco.complete.dpop
//
//import com.signalcollect.GraphBuilder
//import com.signalcollect.ExecutionConfiguration
//import com.signalcollect.configuration.ExecutionMode
//import scala.util.Random
//import com.signalcollect.StateForwarderEdge
//import scala.collection.mutable.MutableList
//
///**
// * siehe paper
// */
//
//object DPOP extends App {
//	
//	// configuration
//	println("configuration");
//	val numberOfLeafNodes : Int = 4
//	val numberOfMiddleNodes : Int = 2
//	val numberOfTopNodes : Int = 2500
//	val numberOfTimeslots : Int = 24
//
//	// initialize graph
//	println("initialize graph");
//	val graph = GraphBuilder.withConsole(true,8091).build
//	
//	// build nodes
//	var rootNode = new DPOPVertex(0, null, numberOfTimeslots)
//	graph.addVertex(rootNode)
//	
//	// -------------- TOP -------------------
//	for(topId <- 1 to numberOfTopNodes){
//		
//		// create vertex
//		var topIdStr = "t" + topId
//		var topVertex = new DPOPVertex(topIdStr, null, numberOfTimeslots)
//		
//		// parent, child stuff
//		topVertex.addParent(rootNode)
//		rootNode.addChild(topVertex)
//		
//		// add vertex to graph
//		graph.addVertex(topVertex)
//		
//		// --------------- MIDDLE ----------------
//		for(middleId <- 1 to numberOfMiddleNodes){
//		  
//			// create vertex
//			var middleIdStr = "t" + topId + "m" + middleId
//			var middleVertex = new DPOPVertex(middleIdStr, null, numberOfTimeslots)
//		
//			// parent, child stuff
//			middleVertex.addParent(topVertex)
//			topVertex.addChild(middleVertex)
//			
//			// add vertex to graph
//			graph.addVertex(middleVertex)
//			
//			// -------------- LEAF ---------------
//			for(leafId <- 1 to numberOfLeafNodes){
//			  
//			  // create vertex
//			  var leafIdStr = "t" + topId + "m" + middleId + "l" + leafId
//			  var leafVertex = new DPOPVertex(leafIdStr, null, numberOfTimeslots)
//			  
//			  // parent, child stuff
//			  leafVertex.addParent(middleVertex)
//			  middleVertex.addChild(leafVertex)
//			  
//			  // add vertex to graph
//			  graph.addVertex(leafVertex)
//			  
//			  // add edge
//			  graph.addEdge(leafIdStr, new StateForwarderEdge(middleIdStr))
//			}
//			
//			// add edge
//			graph.addEdge(middleIdStr, new StateForwarderEdge(topIdStr))
//		}
//		
//		// add edge
//		graph.addEdge(topIdStr, new StateForwarderEdge(0))
//	}
//
//	// execute
//	println("starting the graph");
//	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
//	val stats = graph.execute(execConfig)
//	
//	// insert change & evaluate resilience
////	Dynamics.initialize(vertices);
//	
//	// show run info
//	println(stats)
//	//graph.foreachVertex(println(_))
//	
//	// shutdown graph
//	graph.shutdown
//}
//
