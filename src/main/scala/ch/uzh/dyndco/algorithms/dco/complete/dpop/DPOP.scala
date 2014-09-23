package ch.uzh.dyndco.algorithms.dco.complete.dpop

import com.signalcollect.GraphBuilder
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import scala.util.Random
import com.signalcollect.StateForwarderEdge
import scala.collection.mutable.MutableList

object DPOP extends App {
	
	// configuration
	println("configuration");
	val numberOfLeafNodes : Int = 1000
	val numberOfMiddleNodes : Int = 500
	val numberOfTopNodes : Int = 250
	val numberOfTimeslots : Int = 24

	// initialize graph
	println("initialize graph");
	val graph = GraphBuilder.withConsole(true,8091).build
	
	// build nodes
	var counter : Int = 0
	var rootNode = new DPOPVertex(counter, null, numberOfTimeslots)
	var topNodes : MutableList[DPOPVertex] = MutableList()
	for(id <- (counter + 1) to (numberOfTopNodes)){
		var currentVertex = new DPOPVertex(id, null, numberOfTimeslots)
		currentVertex.addParent(rootNode)
		rootNode.addChild(currentVertex)
		topNodes += currentVertex
		counter + 1
	}
	var middleNodes : MutableList[DPOPVertex] = MutableList()
	var topCount = 0
	for(id <- (counter + 1) to (counter + numberOfMiddleNodes)){
		var currentVertex = new DPOPVertex(id, null, numberOfTimeslots)
		for(parentCount <- 0 to 1){
			currentVertextopNodes.get(topCount + parentCount)
		}
		topCount + 2
		middleNodes += currentVertex
		counter + 1
	}
	var leafNodes : MutableList[DPOPVertex] = MutableList()
	for(id <- (counter + 1) to (counter + numberOfLeafNodes)){
		leafNodes += new DPOPVertex(id, null, numberOfTimeslots)
	}
	
	// build child-parent relationships & edges
	
	
	
//	// build vertices
//	val parent : DPOPVertex = new DPOPVertex(1, new DPOPMessage(0.0,"Util"), numberOfTimeslots)
//	val child1 : DPOPVertex = new DPOPVertex(2, null, numberOfTimeslots)
//	val child2 : DPOPVertex = new DPOPVertex(3, null, numberOfTimeslots)
//
//	// FIXME make nicer
//	val children : List[DPOPVertex] = List(child1,child2)
//	parent.addChildren(children)
//	child1.addParent(parent)
//	child2.addParent(parent)
//	
//	graph.addVertex(parent)
//	graph.addVertex(child1)
//	graph.addVertex(child2)
//	
//	// add edges
//	graph.addEdge(1, new StateForwarderEdge(2))
//	graph.addEdge(1, new StateForwarderEdge(3))
//	graph.addEdge(2, new StateForwarderEdge(1))
//	graph.addEdge(2, new StateForwarderEdge(3))
//	graph.addEdge(3, new StateForwarderEdge(1))
//	graph.addEdge(3, new StateForwarderEdge(2))
	
	// execute
	println("starting the graph");
	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = graph.execute(execConfig)
	
	// insert change & evaluate resilience
	
	
	// show run info
	println(stats)
	graph.foreachVertex(println(_))
	
	// shutdown graph
	graph.shutdown
}

