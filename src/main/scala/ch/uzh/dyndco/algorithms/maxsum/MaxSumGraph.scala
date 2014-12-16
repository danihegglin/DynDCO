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
      * Initialize Graph
      */
      val graph = GraphBuilder.withConsole(true,8091).build
    
    // build variable vertices
      var variableVertices : Set[VariableVertex] = Set[VariableVertex]()
            
       // build function vertices FIXME change
      for(meeting <- problem.meetings){
        var meetingIndex : Map[Any, Int] = Map[Any, Int]()
        var functionId : Any = "f" + meeting.meetingID
        println("functionID: " + functionId)
        var funcVertex = new FunctionVertex(functionId, null, problem.TIMESLOTS, meetingIndex)
        graph.addVertex(funcVertex)
      }
       
       // establish edges to all meeting functions
       var agentIndices : Map[Any, Map[Int,Int]] = Map[Any, Map[Int,Int]]()
      for(agent : Int <- problem.allParticipations.keys){
        
        var agentIndex : Map[Int,Int] = Map[Int,Int]()
        agentIndices += agent -> agentIndex
        
        val connectedAgents = Set[Int]()
        var meetingIds : Set[Int] = problem.allParticipations.apply(agent)
        println("meeting set agent: " + meetingIds)
//        var agentVariableId : Any = "v" + agent
        
        for(meetingId <- meetingIds){
          
          // build agent vertex
          var agentVariableId : Any = "v" + agent + "m" + meetingId
          var constraints = problem.allConstraints.apply(agent)
          var maxSumMessage = new MaxSumMessage(
            constraints.sender,
            constraints.hard,
            constraints.soft,
            constraints.preference) // FIXME
          var varVertex = new VariableVertex(agentVariableId,maxSumMessage,problem.TIMESLOTS, agentIndex)
          graph.addVertex(varVertex)
          variableVertices += varVertex
          
          // build edges
          var functionId : Any = "f" + meetingId
          graph.addEdge(agentVariableId, new StateForwarderEdge(functionId))
          graph.addEdge(functionId, new StateForwarderEdge(agentVariableId))
        }
      } 
       
      // return graph
      graph
    }
}
