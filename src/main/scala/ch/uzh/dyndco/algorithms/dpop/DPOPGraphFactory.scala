package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.Graph
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge
import collection.mutable.Set

object DPOPGraphFactory {
  
  // Configuration
  final var SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 10000 // Limit of communication rounds
  final var CHANGE_ROUND : Int = 10
  
  def build(problem : MeetingSchedulingProblem) : DPOPGraph = {
    
    /**
     * Initialize Graph
     */
     val graph = GraphBuilder.withConsole(true,8091).build
    
     /**
      * Build Graph
      */
      // build root node
      var rootNode = new DPOPVertex("root", null)
      rootNode.TIMESLOTS = problem.TIMESLOTS
      graph.addVertex(rootNode)
      
      // build middle nodes
      var middleNodes : Map[Int, DPOPVertex] = Map[Int, DPOPVertex]()
      var meetingIndex : Map[Int, Map[Any, Int]] = Map[Int, Map[Any, Int]]()
      for(meeting <- problem.meetings){
        var middleNodeId = "m" + meeting.meetingID
        var middleNode = new DPOPVertex(middleNodeId, null)
        
        middleNode.TIMESLOTS = problem.TIMESLOTS
//        middleNode.MEETING_INDEX = meetingIndex
        
        middleNode.addParent(rootNode)
        rootNode.addChild(middleNode)
        
        graph.addVertex(middleNode)
        
        graph.addEdge(middleNodeId, new StateForwarderEdge("root"))
        graph.addEdge("root", new StateForwarderEdge(middleNodeId))
        
        middleNodes += (meeting.meetingID -> middleNode)
      }
      
      // build leaf nodes
      var leafNodes : Set[DPOPVertex] = Set[DPOPVertex]()
      
      var slot = 0
      
      for(agent <- problem.allParticipations.keys){
        
        var agentIndex : Map[Any, Int] = Map[Any, Int]()
        var constraints = problem.allConstraints.apply(agent)
        var participations = problem.allParticipations.apply(agent)
        
        println(agent + ": " + 
            constraints.hard + " | " + 
            constraints.soft + " | " + 
            constraints.preference)
        
        // build vertices & edges
        for(participation <- participations){
          
          slot += 1
          
          var meetingVertex = middleNodes.apply(participation)
          
          var leafNodeId = "a" + agent + "m" + participation
          var leafNode = new DPOPVertex(leafNodeId, null) // FIXME
          
          // Basic
          leafNode.MAX_ROUND = MAX_ROUND
          leafNode.PUSH_ROUND = slot
          
          // Meeting Scheduling
          leafNode.TIMESLOTS = problem.TIMESLOTS
          leafNode.CONSTRAINTS_ORIGINAL = constraints
          leafNode.CONSTRAINTS_CURRENT = constraints
          leafNode.AGENT_INDEX = agentIndex
          
          // Dynamic
          leafNode.CHANGE_ROUND = CHANGE_ROUND // FIXME
          
          leafNode.addParent(meetingVertex)
          meetingVertex.addChild(leafNode)
          
          graph.addVertex(leafNode)
          
          graph.addEdge(leafNodeId, new StateForwarderEdge("m" + participation))
          graph.addEdge("m" + participation, new StateForwarderEdge(leafNodeId))
          
          leafNodes += leafNode
        }
      }
     
     // return graph
      new DPOPGraph(rootNode, middleNodes, leafNodes, graph)
  }
	
}