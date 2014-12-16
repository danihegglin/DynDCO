package ch.uzh.dyndco.algorithms.mgm

import com.signalcollect.Graph
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.problems.Constraints
import ch.uzh.dyndco.algorithms.maxsum.Meeting
import com.signalcollect.StateForwarderEdge
import collection.mutable.Set
import collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

object MGMGraph {
  
  var vertices = Set[MGMVertex]()
  
  def build(problem : MeetingSchedulingProblem) : Graph[Any, Any] = {
    
     /**
      * Initialize Graph
      */
      val graph = GraphBuilder.withConsole(true,8091).build
    
    // build variable vertices
      var neighbourhoods : Map[Int, Set[MGMVertex]] = Map[Int, Set[MGMVertex]]()
      for(meeting <- problem.meetings){
         neighbourhoods += meeting.meetingID -> Set[MGMVertex]()
      }
              
      // establish edges to all meeting functions
      var agentIndices : Map[Any, Map[Int,Int]] = Map[Any, Map[Int,Int]]()
      for(agent : Int <- problem.allParticipations.keys){
          
        var agentIndex : Map[Int,Int] = Map[Int,Int]()
        agentIndices += agent -> agentIndex
          
        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
          
        for(meetingId <- meetingIds){
            
           // build agent vertex
           var agentVariableId : Any = "v" + agent + "m" + meetingId
           var constraints = problem.allConstraints.apply(agent)
           var varVertex = new MGMVertex(agentVariableId,new MGMMessage(null,0,0),problem.TIMESLOTS, constraints, agentIndex)
           graph.addVertex(varVertex)
           vertices += varVertex
           
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
      
      // return graph
      graph
  }

}