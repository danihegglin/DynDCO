package ch.uzh.dyndco.algorithms.mgm

import com.signalcollect.Graph
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.MeetingConstraints
import com.signalcollect.StateForwarderEdge
import collection.mutable.Set
import collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

object MGMGraphFactory {
  
  // Configuration
  final var SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 10000 // Limit of communication rounds
  final var CHANGE_ROUND : Int = 10
  
  var vertices = Set[MGMVertex]() // FIXME should not be here, create graph object
  
  def build(problem : MeetingSchedulingProblem) : MGMGraph = {
    
     /**
      * Initialize Graph
      */
      val graph = GraphBuilder.build
    
    // build neighbourhoods
      var neighbourhoods = Map[Int, Set[MGMVertex]]()
      var meetingIndices = Map[Int, Map[Any,Int]]()
      for(meeting <- problem.meetings){
         neighbourhoods += (meeting.meetingID -> Set[MGMVertex]())
         meetingIndices += (meeting.meetingID -> Map[Any,Int]())
      }
              
      // establish edges to all meeting functions
      var agentIndices : Map[Int, Map[Any,Int]] = Map[Int, Map[Any,Int]]()
      var slot = 0
      for(agent : Int <- problem.allParticipations.keys){
          
        var agentIndex : Map[Any, Int] = Map[Any, Int]()
        agentIndices += agent -> agentIndex
          
        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
          
        for(meetingId <- meetingIds){
          
            slot += 1
            
           // build agent vertex
           var agentId : Any = "v" + agent + "m" + meetingId
           var constraints = problem.allConstraints.apply(agent)
           var vertex = new MGMVertex(agentId,new MGMMessage(null,0,0))
            
           // initial index registration
           var meetingIndex = meetingIndices.apply(meetingId)
           meetingIndex += (agent -> constraints.preference.apply(meetingId))
           
          // Add parameters
          vertex.MAX_ROUND = MAX_ROUND
          vertex.PUSH_ROUND = slot
          vertex.TIMESLOTS = problem.TIMESLOTS
          vertex.CONSTRAINTS_ORIGINAL = constraints.clone()
          vertex.CONSTRAINTS_CURRENT = constraints.clone()
          vertex.MEETING_INDEX = meetingIndex
          vertex.AGENT_INDEX = agentIndex
          vertex.MEETING_ID = meetingId
          vertex.AGENT_ID = agent
          vertex.CHANGE_ROUND = CHANGE_ROUND // FIXME
           
           graph.addVertex(vertex)
           vertices += vertex
           
           var neighbourhood : Set[MGMVertex] = neighbourhoods.apply(meetingId)
           neighbourhood += vertex
           neighbourhoods += meetingId -> neighbourhood 
           
          if(slot == SLOTS){
            slot = 0
          }
          
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
      
      // return graph
      new MGMGraph(vertices, neighbourhoods, agentIndices, meetingIndices, graph)
  }

}