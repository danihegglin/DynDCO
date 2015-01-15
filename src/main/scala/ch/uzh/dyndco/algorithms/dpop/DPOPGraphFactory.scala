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
import scala.util.Random
import scala.collection.mutable.MutableList

object DPOPGraphFactory extends GraphFactory[DPOPGraph, MeetingSchedulingProblem] {
  
  // Configuration
  final var MAX_SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 1000 // Limit of communication rounds
  
  // Current State
  var slot : Int = 0
  
  def build(problem : MeetingSchedulingProblem) : DPOPGraph = {
    
    // initialize state (new build -> new run)
    slot = 0
    connectableVertices.clear()
    
    /**
     * Initialize Graph
     */
     val graph = GraphBuilder.build
    
     /**
      * Build Graph
      */
     
      // indices
      var vertices : Set[DPOPVertex] = Set[DPOPVertex]()
      var neighbourhoods = Map[Int, Set[DPOPVertex]]()
      var meetingIndices : Map[Int, Map[Any, Int]] = Map[Int, Map[Any, Int]]()
      var agentIndices = Map[Int, Map[Any, Int]]()
     
      // prepare meetings
      for(meeting <- problem.meetings){
        meetingIndices += (meeting.id -> Map[Any, Int]())
        neighbourhoods += (meeting.id -> Set[DPOPVertex]())
      }
      
      // build nodes
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
          
          var meetingIndex = meetingIndices.apply(meetingId)
          
          var vertex = buildVertex(
              agentId,
              meetingId,
              constraints,
              problem.TIMESLOTS,
              agentIndex,
              meetingIndex,
              graph
          )
          
          // add to index
          vertices += vertex
          
          // add to neighbourhood
          addToNeighbourhoods(
            meetingId,
            vertex,
            neighbourhoods    
            )
        }
      }
    
     // build edges
     buildPseudoTree(vertices, graph)
    
     // return graph
      new DPOPGraph(vertices, neighbourhoods, agentIndices, meetingIndices, graph)
  }
  
    /**
     * Builder Functions
     */
     def buildVertex(
         agentId : Int, 
         meetingId : Int,
         constraints : MeetingConstraints, 
         timeslots : Int,
         agentIndex : Map[Any,Int],
         meetingIndex : Map[Any,Int],
         graph : Graph[Any, Any]) : DPOPVertex = {
       
       // increment monitoring slot
       slot += 1
       
       // build vertex
       var vertexId = "a" + agentId + "m" + meetingId
       var vertex = new DPOPVertex(vertexId, null)
          
       // parameters
       vertex.MAX_ROUND = MAX_ROUND
       vertex.PUSH_ROUND = slot
       vertex.TIMESLOTS = timeslots
       vertex.CONSTRAINTS_ORIGINAL = constraints.clone()
       vertex.CONSTRAINTS_CURRENT = constraints.clone()
       vertex.AGENT_INDEX = agentIndex
       vertex.MEETING_INDEX = meetingIndex
       vertex.MEETING_ID = meetingId
       vertex.AGENT_ID = agentId
       
       // add to graph
       graph.addVertex(vertex)

       // adjust slot
       if(slot == MAX_SLOTS){
         slot = 0
       }
       
       vertex
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
      * Pseudotree Functions
      */
     var connectableVertices = MutableList[DPOPVertex]()
     
     def buildPseudoTree(vertices : Set[DPOPVertex], graph : Graph[Any,Any]){
      
      // build list and random picks
      var verticesList = vertices.toList
      var randomPicks = Random.shuffle((0 until verticesList.length).toList)
      
      // build main edges
      for(randomPick <- randomPicks){
        
        // choose leader
        if(connectableVertices.isEmpty){
          connectableVertices += verticesList.apply(randomPick)
        }
        else {
          addLeaf(verticesList.apply(randomPick), vertices, graph)
        }
      }
      
    }
    
    def addLeaf(childVertex : DPOPVertex, vertices : Set[DPOPVertex], graph : Graph[Any,Any]) {
       
      /**
       * Tree Edge
       */
        // add random nodes from the set to the tree
        var parentVertex = connectableVertices.apply(0)
          
         // add main parent/child relationship
         childVertex.addParent(parentVertex)
         parentVertex.addChild(childVertex)
         
         // add main edges
         graph.addEdge(childVertex.id, new StateForwarderEdge(parentVertex.id))
         graph.addEdge(parentVertex.id, new StateForwarderEdge(childVertex.id))
         
         // remove parent from connectable list if full
         if(parentVertex.children.size == 2){
           connectableVertices.drop(0)
         }
          
         // add child to connectable list
         connectableVertices += childVertex
         
       /**
        * Back-Edges
        */
        parentVertex = vertices.toVector(Random.nextInt(vertices.size))
        
        // add hidden parent/child relationship FIXME
//        childVertex.addParent(parentVertex)
//        parentVertex.addChild(childVertex)
//        
//        // add hidden edges
//        graph.addEdge(childVertex.id, new StateForwarderEdge(parentVertex.id))
//        graph.addEdge(parentVertex.id, new StateForwarderEdge(childVertex.id))
         
    }
    
    def removeVertex(vertex : DPOPVertex, grap : Graph[Any, Any]) {
      for(child <- vertex.children){
        child.parent = vertex.parent
        vertex.parent.addChild(child)
      }
      vertex.parent.removeChild(vertex)
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
      if(dpopGraph.agentIndices.contains(agentId)){
        dpopGraph.agentIndices.apply(agentId)
      }
      else { 
        Map[Any,Int]()
      }
      
      // indices
      var meetingIndex = 
      if(dpopGraph.meetingIndices.contains(meetingId)){
        dpopGraph.meetingIndices.apply(meetingId)
      }
      else {
        var meetingIndex = Map[Any,Int]()
        dpopGraph.meetingIndices += (meetingId -> meetingIndex)
        meetingIndex
      }
      
      var vertex = buildVertex(
          agentId, 
          meetingId,
          constraints,
          problem.TIMESLOTS,
          agentIndex,
          meetingIndex,
          dpopGraph.graph
          )
          
      // add to neighbourhoods
      addToNeighbourhoods(meetingId, vertex, dpopGraph.neighbourhoods)
      
      // add to indices
      dpopGraph.vertices += vertex
      
      // add to graph
      addLeaf(vertex, dpopGraph.vertices, dpopGraph.graph)
  }
  
  def removeAgent(
      dpopGraph : DPOPGraph,
      agentId: Int, 
      meetingId: Int) {
    
      // get vertex object
      if(dpopGraph.neighbourhoods.contains(meetingId)){
        var neighbourhood = dpopGraph.neighbourhoods.apply(meetingId)
        var vertex : DPOPVertex = null
        for(participant <- neighbourhood){
          if(participant.AGENT_ID == agentId){
            vertex = participant
          }
        }
        
        // connect children to parent
        vertex.parent.removeChild(vertex)
        if(vertex.children.size > 0){
          vertex.parent.addChild(vertex.children.apply(0))
          if(vertex.children.size > 1){
            addLeaf(vertex.children.apply(1), dpopGraph.vertices, dpopGraph.graph)
          }
        }
          
        // remove vertices
        dpopGraph.graph.removeVertex(vertex)
                
        // clear indices
        vertex.AGENT_INDEX.remove(meetingId)
        neighbourhood.remove(vertex)
        if(neighbourhood.size > 0)
          dpopGraph.neighbourhoods += (meetingId -> neighbourhood)
        else
          dpopGraph.neighbourhoods.remove(meetingId)
        
        dpopGraph.vertices -= vertex
        
      }
    
    }
	
}