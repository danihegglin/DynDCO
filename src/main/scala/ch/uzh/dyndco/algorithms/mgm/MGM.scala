package ch.uzh.dyndco.algorithms.mgm

/**
 * Chapman (2011): A unifying framework for iterative approximate
 * best-response algorithms for distributed constraint
 * optimization problems. P.440.
 */

import dispatch._
import dispatch.Defaults._
import collection.mutable.Set
import collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import scala.collection.mutable.MutableList
import com.signalcollect.GraphBuilder
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.Meeting
import com.signalcollect.StateForwarderEdge

object MGM extends App {
  
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
		var neighbourhoods : Map[Int, Set[MGMVertex]] = Map[Int, Set[MGMVertex]]()
    for(meeting <- meetings){
       neighbourhoods += meeting.meetingID -> Set[MGMVertex]()
    }
						
    // establish edges to all meeting functions
    var agentIndices : Map[Any, Map[Int,Int]] = Map[Any, Map[Int,Int]]()
		for(agent : Int <- allParticipations.keys){
        
      var agentIndex : Map[Int,Int] = Map[Int,Int]()
      agentIndices += agent -> agentIndex
			  
			var meetingIds : Set[Int] = allParticipations.apply(agent)
			println("meeting set agent: " + meetingIds)
				
			for(meetingId <- meetingIds){
          
         // build agent vertex
         var agentVariableId : Any = "v" + agent + "m" + meetingId
         var constraints = allConstraints.apply(agent)
         var varVertex = new MGMVertex(agentVariableId,new MGMMessage(null,0,0),TIMESLOTS, constraints, agentIndex)
         graph.addVertex(varVertex)
         
         var neighbourhood : Set[MGMVertex] = neighbourhoods.apply(meetingId)
         neighbourhood += varVertex
         neighbourhoods += meetingId -> neighbourhood 
        
		  }
		} 
	
		// build edges
		for(neighbourhoodId <- neighbourhoods.keys){
		  var neighbourhood = neighbourhoods.apply(neighbourhoodId)
		  for(agent <- neighbourhood){
		    for(neighbour <- neighbourhood){
		      if(agent != neighbour){
		         graph.addEdge(agent.id, new StateForwarderEdge(neighbour.id))
		      }
		    }
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
    for(agent <- agentIndices.keys){
      println(agent + " -> " + agentIndices.apply(agent))
    }

		// shutdown graph
		graph.shutdown
}