package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.Graph
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge

object DPOPGraph {
  
  def build(problem : MeetingSchedulingProblem) : Graph[Any, Any] = {
    
    /**
     * Initialize Graph
     */
     val graph = GraphBuilder.withConsole(true,8091).build
    
     /**
      * Build Graph
      */
      // build root node
      var rootNode = new DPOPVertex("root", null, problem.TIMESLOTS, null, null)
      graph.addVertex(rootNode)
      
      // build middle nodes
      var meetingVertices : Map[Int, DPOPVertex] = Map[Int, DPOPVertex]()
      var meetingIndex : Map[Int, Map[Any, Int]] = Map[Int, Map[Any, Int]]()
      for(meeting <- problem.meetings){
        var middleNodeId = "m" + meeting.meetingID
        var middleNode = new DPOPVertex(middleNodeId, null, problem.TIMESLOTS, null, meetingIndex)
       
        middleNode.addParent(rootNode)
        rootNode.addChild(middleNode)
        
        graph.addVertex(middleNode)
        
        graph.addEdge(middleNodeId, new StateForwarderEdge("root"))
        graph.addEdge("root", new StateForwarderEdge(middleNodeId))
        
        meetingVertices += (meeting.meetingID -> middleNode)
      }
      
      // build leaf nodes
      var agentVertices : Set[DPOPVertex] = Set[DPOPVertex]()
      var agentIndex : Map[Int, Map[Any, Int]] = Map[Int, Map[Any, Int]]()
      for(agent <- problem.allParticipations.keys){
        var constraints = problem.allConstraints.apply(agent)
        var participations = problem.allParticipations.apply(agent)
        
        println(agent + ": " + 
            constraints.hard + " | " + 
            constraints.soft + " | " + 
            constraints.preference)
        
        // build vertices & edges
        for(participation <- participations){
          
          var meetingVertex = meetingVertices.apply(participation)
          
          var leafNodeId = "a" + agent + "m" + participation
          var leafNode = new DPOPVertex(leafNodeId, null, problem.TIMESLOTS, constraints, agentIndex) // FIXME
          
          leafNode.addParent(meetingVertex)
          meetingVertex.addChild(leafNode)
          
          graph.addVertex(leafNode)
          
          graph.addEdge(leafNodeId, new StateForwarderEdge("m" + participation))
          graph.addEdge("m" + participation, new StateForwarderEdge(leafNodeId))
          
          agentVertices += leafNode
        }
      }
     
     /**
      * Return Graph
      */
      graph
  }
	
}