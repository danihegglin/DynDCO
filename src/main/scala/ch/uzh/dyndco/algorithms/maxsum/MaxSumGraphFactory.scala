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
  
  def build(problem : MeetingSchedulingProblem, runID : String) : MaxSumGraph = {
    
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
      for(agent : Int <- problem.allParticipations.keys){
        
        var agentIndex = Map[Any,Int]()

        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
        
        for(meetingId <- meetingIds){
          
          // get index
          var meetingIndex = meetingIndices.apply(meetingId)
          
          // build variable vertex
          var variableId : Any = "v" + agent + "m" + meetingId
          var constraints = problem.allConstraints.apply(agent)
          var varVertex = new VariableVertex(variableId,null,problem.TIMESLOTS, constraints, agentIndex, meetingIndex, meetingId, runID)
          graph.addVertex(varVertex)
          varVertices += varVertex
          meetingIndex += (variableId -> constraints.preference.apply(meetingId))
          
          // build function vertex
          var functionId : Any = "f" + agent + "m" + meetingId
          var funcVertex = new FunctionVertex(functionId,null,problem.TIMESLOTS)
          graph.addVertex(funcVertex)
          funcVertices += funcVertex
          
          // add to neighbourhood
          var neighbourhood = Map[VariableVertex, FunctionVertex]()
          if(neighbourhoods.contains(meetingId)){
            neighbourhood = neighbourhoods.apply(meetingId)
          }
          neighbourhood += (varVertex -> funcVertex)
          neighbourhoods += (meetingId -> neighbourhood)
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
