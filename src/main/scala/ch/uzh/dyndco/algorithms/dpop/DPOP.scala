package ch.uzh.dyndco.algorithms.dpop;

import com.signalcollect.configuration.ExecutionMode
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.algorithms.maxsum.Meeting

///**
// * based on: A Scalable Method for Multiagent Constraint Optimization
// */
//
object DPOP extends App {
	
	/**
	 * Configuration
	 */
  val TIMESLOTS : Int = 10
	val AGENTS : Int = 5
	val MEETINGS : Int = 2
	
	/**
	 * Build meetings, participations, constraints
	 */
	val factory = new MeetingSchedulingFactory(TIMESLOTS,MEETINGS,AGENTS)
	var meetings : MutableList[Meeting] = factory.buildMeetings()
	val allParticipations = factory.buildAllParticipations()
	val allConstraints = factory.buildAllConstraints(allParticipations)

	/**
	 *  initialize graph
	 */
	println("initialize graph");
	val graph = GraphBuilder.withConsole(true,8091).build
	
	// build root node
	var rootNode = new DPOPVertex("root", null, TIMESLOTS, null)
	graph.addVertex(rootNode)
	
	// build middle nodes
	var meetingVertices : Map[Int, DPOPVertex] = Map[Int, DPOPVertex]()
	for(meeting <- meetings){
	  var middleNodeId = "m" + meeting.meetingID
	  var middleNode = new DPOPVertex(middleNodeId, null, TIMESLOTS, null)
	 
	  middleNode.addParent(rootNode)
	  rootNode.addChild(middleNode)
	  
	  graph.addVertex(middleNode)
	  
	  graph.addEdge(middleNodeId, new StateForwarderEdge("root"))
	  graph.addEdge("root", new StateForwarderEdge(middleNodeId))
	  
	  meetingVertices += (meeting.meetingID -> middleNode)
	}
	
	// build leaf nodes
	var agentVertices : Set[DPOPVertex] = Set[DPOPVertex]()
	for(agent <- allParticipations.keys){
	  var constraints = allConstraints.apply(agent)
	  var participations = allParticipations.apply(agent)
	  
	  // build vertices & edges
	  for(participation <- participations){
	    
	    var meetingVertex = meetingVertices.apply(participation)
	    
	    var leafNodeId = "a" + agent + "m" + participation
	    var leafNode = new DPOPVertex(leafNodeId, null, TIMESLOTS, constraints) // FIXME
	    
	    leafNode.addParent(meetingVertex)
	    meetingVertex.addChild(leafNode)
	    
	    graph.addVertex(leafNode)
	    
	    graph.addEdge(leafNodeId, new StateForwarderEdge("m" + participation))
	    graph.addEdge("m" + participation, new StateForwarderEdge(leafNodeId))
	    
	    agentVertices += leafNode
	  }
	}
	
	/**
	 * Run the graph
	 */
	
	// start
	val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
	val stats = graph.execute(execConfig)
	
	// show run info
	println(stats)
	graph.foreachVertex(println(_))
	
	// show results
	for(agentVertex <- agentVertices){
	  println("----------" + agentVertex.id + "---------------")
		agentVertex.show()
	}
	
	// shutdown graph
	graph.shutdown
}
