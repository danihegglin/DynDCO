package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder

object MaxSumGraphFactory {
  
  // Configuration
  final var SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 10000 // Limit of communication rounds
  final var CHANGE_ROUND : Int = 10
  
  def build(problem : MeetingSchedulingProblem) : MaxSumGraph = {
    
    var varVertices = Set[VariableVertex]()
    var funcVertices = Set[FunctionVertex]()
    var neighbourhoods = Map[Int, Map[VariableVertex,FunctionVertex]]() // meetingId => variableId -> functionId
    
     /**
      * Initialize graph
      */
      val graph = GraphBuilder.build
      
      /**
       * Build index for each meeting
       */
      var meetingIndices = Map[Int, Map[Any,Int]]()
      for(meeting <- problem.meetings){
        meetingIndices += (meeting.meetingID -> Map[Any,Int]())
      }
      
      /**
       * Build neighbourhoods
       */
      var slot = 0
      for(agent : Int <- problem.allParticipations.keys){
        
        var agentIndex = Map[Any,Int]()

        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
        
        for(meetingId <- meetingIds){
          
          slot += 1
          
          // get index
          var meetingIndex = meetingIndices.apply(meetingId)
          
          // build variable vertex
          var variableId : Any = "v" + agent + "m" + meetingId
          var constraints = problem.allConstraints.apply(agent)
          var varVertex = new VariableVertex(variableId, null)
          
          // Basic
          varVertex.MAX_ROUND = MAX_ROUND
          varVertex.PUSH_ROUND = slot
          varVertex.SLOTS = SLOTS
          
          // Meeting Scheduling
          varVertex.TIMESLOTS = problem.TIMESLOTS
          varVertex.CONSTRAINTS_ORIGINAL = constraints
          varVertex.CONSTRAINTS_CURRENT = constraints
          varVertex.MEETING_INDEX = meetingIndex
          varVertex.AGENT_INDEX = agentIndex
          varVertex.MEETING_ID = meetingId
          
          // Dynamic
          varVertex.CHANGE_ROUND = CHANGE_ROUND // FIXME
          
          graph.addVertex(varVertex)
          varVertices += varVertex
          meetingIndex += (variableId -> constraints.preference.apply(meetingId))
          
          // build function vertex
          var functionId : Any = "f" + agent + "m" + meetingId
          var funcVertex = new FunctionVertex(functionId,null)
          graph.addVertex(funcVertex)
          funcVertices += funcVertex
          
          // add to neighbourhood
          var neighbourhood = Map[VariableVertex, FunctionVertex]()
          if(neighbourhoods.contains(meetingId)){
            neighbourhood = neighbourhoods.apply(meetingId)
          }
          neighbourhood += (varVertex -> funcVertex)
          neighbourhoods += (meetingId -> neighbourhood)
          
          // FIXME make nicer
          if(slot == SLOTS){
            slot = 0
          }
          
        }
      } 
      
//      graph.foreachVertex(println(_))
      
      // build edges
      for(neighbourhood <- neighbourhoods.values){
        for(variableVertex <- neighbourhood.keys){
          for(functionVertex <- neighbourhood.values){
            graph.addEdge(variableVertex.id, new StateForwarderEdge(functionVertex.id))
            graph.addEdge(functionVertex.id, new StateForwarderEdge(variableVertex.id))
          }
        }
      }
      
      // return graph
      new MaxSumGraph(varVertices, funcVertices, neighbourhoods, graph)
    }
    
}
