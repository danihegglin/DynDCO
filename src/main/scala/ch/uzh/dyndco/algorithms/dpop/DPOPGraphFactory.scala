package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.Graph
import com.signalcollect.GraphBuilder
import com.signalcollect.StateForwarderEdge
import collection.mutable.Set
import ch.uzh.dyndco.problems.MeetingConstraints
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.stack.graph.GraphFactory

object DPOPGraphFactory extends GraphFactory[DPOPGraph, MeetingSchedulingProblem] {
  
  // Configuration
  final var MAX_SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 3000 // Limit of communication rounds
  
  // Current State
  var slot : Int = 0
  
  def build(problem : MeetingSchedulingProblem) : DPOPGraph = {
    
    /**
     * Initialize Graph
     */
     val graph = GraphBuilder.build
    
     /**
      * Build Graph
      */
     
      // indices
      var middleVertices : Map[Int, DPOPVertex] = Map[Int, DPOPVertex]()
      var leafVertices : Set[DPOPVertex] = Set[DPOPVertex]()
      var neighbourhoods = Map[Int, Set[DPOPVertex]]()
      var meetingIndices : Map[Int, Map[Any, Int]] = Map[Int, Map[Any, Int]]()
      var agentIndices = Map[Int, Map[Any, Int]]()
     
      // build root node
      var rootVertex = buildRootVertex(problem.TIMESLOTS, graph)
      
      // build middle nodes
      for(meeting <- problem.meetings){
        
        // add to meeting index
        var meetingIndex : Map[Any, Int] = Map[Any, Int]()
        meetingIndices += (meeting.id -> meetingIndex)
                
        var middleNode = buildMiddleVertex(
            meeting.id,
            problem.TIMESLOTS,
            meetingIndex,
            rootVertex,
            graph)
        
        middleVertices += (meeting.id -> middleNode)
        neighbourhoods += (meeting.id -> Set[DPOPVertex]())
      }
      
      // build leaf nodes
      var slot = 0
      for(agentId <- problem.allParticipations.keys){
        
        // build agent index
        var agentIndex : Map[Any, Int] = Map[Any, Int]()
        agentIndices += (agentId -> agentIndex)
        
        // prepare elements
        var constraints = problem.allConstraints.apply(agentId)
        var participations = problem.allParticipations.apply(agentId)
        
        // build vertices & edges
        for(meetingId <- participations){
          
          slot += 1
          
          var meetingVertex = middleVertices.apply(meetingId)
          
          var leafVertex = buildLeafVertex(
              agentId,
              meetingId,
              constraints,
              problem.TIMESLOTS,
              agentIndex,
              meetingVertex,
              graph
          )
          
          // add to index
          leafVertices += leafVertex
          
          // add to neighbourhood
          addToNeighbourhoods(
            meetingId,
            leafVertex,
            neighbourhoods    
            )
        }
      }
     
     // return graph
      new DPOPGraph(rootVertex, middleVertices, leafVertices, neighbourhoods, agentIndices, meetingIndices, graph)
  }
  
    /**
     * Builder Functions
     */
     def buildRootVertex(
         timeslots : Int,
         graph : Graph[Any, Any]) : DPOPVertex = {
       
       // build vertex
       var rootVertex = new DPOPVertex("root", null)
       
       // parameters
       rootVertex.TIMESLOTS = timeslots
       
       // add to graph
       graph.addVertex(rootVertex)
       
       rootVertex
     }
     
     def buildMiddleVertex(
         meetingId : Int,
         timeslots : Int,
         meetingIndex : Map[Any,Int],
         rootVertex : DPOPVertex,
         graph : Graph[Any, Any]) : DPOPVertex = {
       
          // build vertex
          var middleVertexId = "m" + meetingId
          var middleVertex = new DPOPVertex(middleVertexId, null)
          
          // parameters
          middleVertex.TIMESLOTS = timeslots
          middleVertex.MEETING_INDEX = meetingIndex
          
          // add middle to root vertex
          middleVertex.addParent(rootVertex)
          rootVertex.addChild(middleVertex)
          
          // add to graph
          graph.addVertex(middleVertex)
          
          // add edges
          graph.addEdge(middleVertexId, new StateForwarderEdge("root"))
          graph.addEdge("root", new StateForwarderEdge(middleVertexId))
          
          middleVertex
       
     }
     
     def buildLeafVertex(
         agentId : Int, 
         meetingId : Int,
         constraints : MeetingConstraints, 
         timeslots : Int,
         agentIndex : Map[Any,Int],
         meetingVertex : DPOPVertex,
         graph : Graph[Any, Any]) : DPOPVertex = {
       
       // increment monitoring slot
       slot += 1
       
       // build vertex
       var leafVertexId = "a" + agentId + "m" + meetingId
       var leafVertex = new DPOPVertex(leafVertexId, null)
          
       // parameters
       leafVertex.MAX_ROUND = MAX_ROUND
       leafVertex.PUSH_ROUND = slot
       leafVertex.TIMESLOTS = timeslots
       leafVertex.CONSTRAINTS_ORIGINAL = constraints.clone()
       leafVertex.CONSTRAINTS_CURRENT = constraints.clone()
       leafVertex.AGENT_INDEX = agentIndex
       
       // add leaf to middle vertex
       leafVertex.addParent(meetingVertex)
       meetingVertex.addChild(leafVertex)
       
       // add to graph
       graph.addVertex(leafVertex)
       
       // add edges
       graph.addEdge(leafVertexId, new StateForwarderEdge("m" + meetingId))
       graph.addEdge("m" + meetingId, new StateForwarderEdge(leafVertexId))
       
       // adjust slot
       if(slot == MAX_SLOTS){
         slot = 0
       }
       
       leafVertex
     }
     
     def addToNeighbourhoods(
         meetingId : Int,
         leafVertex : DPOPVertex, 
         neighbourhoods : Map[Int, Set[DPOPVertex]]
         ) : Map[Int, Set[DPOPVertex]] = {
       
        var neighbourhood = Set[DPOPVertex]()
          if(neighbourhoods.contains(meetingId)){
            neighbourhood = neighbourhoods.apply(meetingId)
          }
          neighbourhood += (leafVertex)
          neighbourhoods += (meetingId -> neighbourhood)
     }
  
  /**
   * Dynamic Functions
   */
  def addAgent(
      dpopGraph : DPOPGraph,
      problem: MeetingSchedulingProblem,
      agentId: Int,
      meetingId: Int) {
    
          // prepare
      var participations = Set[Int](meetingId)
      var constraints : MeetingConstraints = MeetingSchedulingFactory.buildSingleConstraints(agentId, participations)
      var agentIndex = 
        if(dpopGraph.agentIndices.contains(agentId)) 
          dpopGraph.agentIndices.apply(agentId)
        else 
          Map[Any,Int]()
      var meetingIndex = 
        if(dpopGraph.meetingIndices.contains(meetingId))
          dpopGraph.meetingIndices.apply(meetingId)
        else
          Map[Any,Int]()
      
      // build
      var meetingVertex : DPOPVertex = null
      if(!dpopGraph.neighbourhoods.contains(meetingId)){
        meetingVertex = 
          buildMiddleVertex(
              meetingId,
              problem.TIMESLOTS,
              meetingIndex,
              dpopGraph.root,
              dpopGraph.graph
           )
           
        dpopGraph.middle += (meetingId -> meetingVertex)
        dpopGraph.neighbourhoods += (meetingId -> Set[DPOPVertex]())
      }
      else {
        meetingVertex = dpopGraph.middle.apply(meetingId)
      }
          
      var leafVertex = buildLeafVertex(
          agentId, 
          meetingId,
          constraints,
          problem.TIMESLOTS,
          agentIndex,
          meetingVertex,
          dpopGraph.graph
          )
          
      // add to neighbourhoods
      addToNeighbourhoods(meetingId, leafVertex, dpopGraph.neighbourhoods)
      
      // add to indices
      dpopGraph.leaf += leafVertex
  }
  
  def removeAgent(
      dpopGraph : DPOPGraph,
      agentId: Int, 
      meetingId: Int) {
    
      if(dpopGraph.neighbourhoods.contains(meetingId)){
        var neighbourhood = dpopGraph.neighbourhoods.apply(meetingId)
        var leafVertex : DPOPVertex = null
        for(neighbour <- neighbourhood){
          if(neighbour.AGENT_ID == agentId){
            leafVertex = neighbour
          }
        }
        
        // remove vertices
        dpopGraph.graph.removeVertex(leafVertex)
              
        // clear indices
        leafVertex.AGENT_INDEX.remove(meetingId)
        neighbourhood.remove(leafVertex)
        dpopGraph.neighbourhoods += (meetingId -> neighbourhood)
        dpopGraph.leaf -= leafVertex
        
        // remove from meeting index in middle vertex
        var middleVertex = dpopGraph.middle.apply(meetingId)   
        middleVertex.MEETING_INDEX.remove(agentId)
        
        if(neighbourhood.size == 0){
          dpopGraph.graph.removeVertex(middleVertex)
          dpopGraph.middle -= middleVertex.MEETING_ID
        }
       
      }
    
  }
	
}