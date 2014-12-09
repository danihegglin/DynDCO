package ch.uzh.dyndco.algorithms.maxsum

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.Constraints

object MaxSum extends App {

	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 10 // args(0)
	var AGENTS : Int = 5 // args(1)
	var MEETINGS : Int = 2 // args(2)
	
	/**
	 * Build meetings, participations, constraints
	 */
	val factory = new MeetingSchedulingFactory(TIMESLOTS,MEETINGS,AGENTS)
	var meetings : MutableList[Meeting] = factory.buildMeetings()
	val allParticipations = factory.buildAllParticipations()
	val allConstraints = factory.buildAllConstraints(allParticipations)
	
		/**
		 * Initializae Graph
		 */
		val graph = GraphBuilder.withConsole(true,8091).build
		
		  // build variable vertices
		  var variableVertices : Set[VariableVertex] = Set[VariableVertex]()
			for(agent <- allConstraints.keys){
			  var variableId : Any = "v" + agent
			  var constraints = allConstraints.apply(agent)
			  var maxSumMessage = new MaxSumMessage(
			      constraints.sender,
			      constraints.hard,
			      constraints.soft,
			      constraints.preference) // FIXME
		    var varVertex = new VariableVertex(variableId,maxSumMessage,TIMESLOTS)
		    graph.addVertex(varVertex)
		    variableVertices += varVertex
			}
						
		   // build function vertices
			for(meeting <- meetings){
			  var functionId : Any = "f" + meeting.meetingID
			  println("functionID: " + functionId)
		    var funcVertex = new FunctionVertex(functionId, null, TIMESLOTS)
		    graph.addVertex(funcVertex)
			}
       
       // establish edges to all meeting functions 
		  for(agent : Int <- allParticipations.keys){
			  
				val connectedAgents = Set[Int]()
				var meetingIds : Set[Int] = allParticipations.apply(agent)
				println("meeting set agent: " + meetingIds)
				var agentVariableId : Any = "v" + agent
				
				for(meetingId <- meetingIds){
				  var functionId : Any = "f" + meetingId
				  graph.addEdge(agentVariableId, new StateForwarderEdge(functionId))
	  			graph.addEdge(functionId, new StateForwarderEdge(agentVariableId))
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
		for(variableVertex <- variableVertices){
		  println("----------" + variableVertex.id + "---------------")
			variableVertex.show()
		}

		// shutdown graph
		graph.shutdown
}
