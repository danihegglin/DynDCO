package ch.uzh.dyndco.algorithms.maxsum

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import collection.mutable.Set
import collection.mutable.Map
import scala.util.Random
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.Constraints
import dispatch._
import dispatch.Defaults._

object MaxSum extends App {

	/**
	 * Configuration
	 */
	var TIMESLOTS : Int = 10 // args(0)
	var AGENTS : Int = 5 // args(1)
	var MEETINGS : Int = 3 // args(2)
  
  /**
   * Monitoring
   */
  // communicate to monitoring
  val svc = url("http://localhost:9000/start")
  val result = Http(svc OK as.String)
	
	/**
	 * Build meetings, participations, constraints
	 */
	val factory = new MeetingSchedulingFactory(TIMESLOTS,MEETINGS,AGENTS)
	var meetings : MutableList[Meeting] = factory.buildMeetings()
	val allParticipations = factory.buildAllParticipations()
	val allConstraints = factory.buildAllConstraints(allParticipations)
	
		/**
		 * Initialize Graph
		 */
		val graph = GraphBuilder.withConsole(true,8091).build
		
		  // build variable vertices
		  var variableVertices : Set[VariableVertex] = Set[VariableVertex]()
						
		   // build function vertices
			for(meeting <- meetings){
        var meetingIndex : Map[Any, Int] = Map[Any, Int]()
			  var functionId : Any = "f" + meeting.meetingID
			  println("functionID: " + functionId)
		    var funcVertex = new FunctionVertex(functionId, null, TIMESLOTS, meetingIndex)
		    graph.addVertex(funcVertex)
			}
       
       // establish edges to all meeting functions
       var agentIndices : Map[Any, Map[Int,Int]] = Map[Any, Map[Int,Int]]()
		  for(agent : Int <- allParticipations.keys){
        
        var agentIndex : Map[Int,Int] = Map[Int,Int]()
        agentIndices += agent -> agentIndex
			  
				val connectedAgents = Set[Int]()
				var meetingIds : Set[Int] = allParticipations.apply(agent)
				println("meeting set agent: " + meetingIds)
//				var agentVariableId : Any = "v" + agent
				
				for(meetingId <- meetingIds){
          
          // build agent vertex
          var agentVariableId : Any = "v" + agent + "m" + meetingId
          var constraints = allConstraints.apply(agent)
          var maxSumMessage = new MaxSumMessage(
            constraints.sender,
            constraints.hard,
            constraints.soft,
            constraints.preference) // FIXME
          var varVertex = new VariableVertex(agentVariableId,maxSumMessage,TIMESLOTS, agentIndex)
          graph.addVertex(varVertex)
          variableVertices += varVertex
          
          // build edges
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
//		graph.foreachVertex(println(_))
					
		// show results
		for(variableVertex <- variableVertices){
		  println("----------" + variableVertex.id + "---------------")
			variableVertex.show()
		}
    for(agent <- agentIndices.keys){
      println(agent + " -> " + agentIndices.apply(agent))
    }

		// shutdown graph
		graph.shutdown
}
