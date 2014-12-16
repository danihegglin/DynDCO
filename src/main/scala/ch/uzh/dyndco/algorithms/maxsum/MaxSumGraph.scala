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

object MaxSumGraph {
  
  def build(problem : MeetingSchedulingProblem) : Graph[Any, Any] = {
    
     /**
      * Initialize graph
      */
      val graph = GraphBuilder.withConsole(true,8091).build
      
      /**
       * Build neighbourhoods
       */
      var neighbourhoods : Map[Int, Map[Any, Any]] = Map[Int, Map[Any,Any]]() // meetingId => variableId -> functionId
      
      for(agent : Int <- problem.allParticipations.keys){
        
        var agentIndex : Map[Int,Int] = Map[Int,Int]()

        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
        println("meeting set agent: " + meetingIds)
        
        for(meetingId <- meetingIds){
          
          // build variable vertex
          var variableId : Any = "v" + agent + "m" + meetingId
          var constraints = problem.allConstraints.apply(agent)
//          var maxSumMessage = new MaxSumMessage(constraints.sender,constraints.hard,constraints.soft, constraints.preference) // FIXME
          var varVertex = new VariableVertex(variableId,null,problem.TIMESLOTS, constraints, agentIndex)
          graph.addVertex(varVertex)
          
          // build function vertex
          var functionId : Any = "f" + agent + "m" + meetingId
          var funcVertex = new FunctionVertex(functionId,null,problem.TIMESLOTS,null)
          graph.addVertex(funcVertex)
          
          // add to neighbourhood
          var neighbourhood : Map[Any,Any] = Map[Any, Any]()
          if(neighbourhoods.contains(meetingId)){
            neighbourhood = neighbourhoods.apply(meetingId)
          }
          neighbourhood += (varVertex.id -> funcVertex.id)
          neighbourhoods += (meetingId -> neighbourhood)
        }
      } 
      
      graph.foreachVertex(println(_))
      
      // build edges
      for(neighbourhood <- neighbourhoods.values){
        for(variableVertexId <- neighbourhood.keys){
          for(functionVertexId <- neighbourhood.values){
            graph.addEdge(variableVertexId, new StateForwarderEdge(functionVertexId))
            graph.addEdge(functionVertexId, new StateForwarderEdge(variableVertexId))
          }
        }
      }
      
      // return graph
      graph
    }
}
