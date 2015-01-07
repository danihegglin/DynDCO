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
import ch.uzh.dyndco.stack.graph.GraphFactory

object MGMGraphFactory extends GraphFactory[MGMGraph, MeetingSchedulingProblem] {
  
  // Configuration
  final var MAX_SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 3000 // Limit of communication rounds
  
  // Current State
  var slot : Int = 0
  
  def build(problem : MeetingSchedulingProblem) : MGMGraph = {
    
    // initialize slots state (new build -> new run)
    var slot = 0
    
     /**
      * Initialize Graph
      */
      val graph = GraphBuilder.build
    
      /**
       * Indices
       */
      var neighbourhoods = Map[Int, Set[MGMVertex]]()
      var vertices = Set[MGMVertex]()
      
      /**
       *  Build index for each meeting and all agents
       */
      var meetingIndices = Map[Int, Map[Any,Int]]()
      for(meeting <- problem.meetings){
         neighbourhoods += (meeting.id -> Set[MGMVertex]())
         meetingIndices += (meeting.id -> Map[Any,Int]())
      }
              
      // establish edges to all meeting functions
      var agentIndices : Map[Int, Map[Any,Int]] = Map[Int, Map[Any,Int]]()
      for(agentId : Int <- problem.allParticipations.keys){
        
        // build agent index
        var agentIndex : Map[Any, Int] = Map[Any, Int]()
        agentIndices += agentId -> agentIndex
        
        // prepare elements
        var constraints = problem.allConstraints.apply(agentId)
        var participations : Set[Int] = problem.allParticipations.apply(agentId)
        
        for(meetingId <- participations){
          
           slot += 1
           
           // register in meeting index
           var meetingIndex = meetingIndices.apply(meetingId)
           meetingIndex += (agentId -> constraints.preference.apply(meetingId))
            
           // build agent vertex
           var vertex = buildMgmVertex(
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
           addToNeighbourhoods(meetingId, vertex, neighbourhoods)
           
          if(slot == MAX_SLOTS){
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
  
    /**
     * Builder Functions
     */
     def buildMgmVertex(
         agentId : Int, 
         meetingId : Int,
         constraints : MeetingConstraints, 
         timeslots : Int,
         agentIndex : Map[Any,Int],
         meetingIndex : Map[Any,Int],
         graph : Graph[Any, Any]) : MGMVertex = {
       
       // increment monitoring slot
       slot += 1
       
       // build agent vertex
       var vertexId : Any = "v" + agentId + "m" + meetingId
       var vertex = new MGMVertex(vertexId,new MGMMessage(null,0,0))
           
       // add parameters
       vertex.MAX_ROUND = MAX_ROUND
       vertex.PUSH_ROUND = slot
       vertex.TIMESLOTS = timeslots
       vertex.CONSTRAINTS_ORIGINAL = constraints.clone()
       vertex.CONSTRAINTS_CURRENT = constraints.clone()
       vertex.MEETING_INDEX = meetingIndex
       vertex.AGENT_INDEX = agentIndex
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
         leafVertex : MGMVertex, 
         neighbourhoods : Map[Int, Set[MGMVertex]]
         ) : Map[Int, Set[MGMVertex]] = {
       
        var neighbourhood = Set[MGMVertex]()
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
      mgmGraph : MGMGraph,
      problem: MeetingSchedulingProblem,
      agentId: Int,
      meetingId: Int) {
      
      // prepare
      var participations = Set[Int](meetingId)
      var constraints : MeetingConstraints = MeetingSchedulingFactory.buildSingleConstraints(agentId, participations)
      var agentIndex = 
        if(mgmGraph.agentIndices.contains(agentId)) 
          mgmGraph.agentIndices.apply(agentId)
        else 
          Map[Any,Int]()
      var meetingIndex = 
        if(mgmGraph.meetingIndices.contains(meetingId))
          mgmGraph.meetingIndices.apply(meetingId)
        else
          Map[Any,Int]()
      
      // build
      var vertex = buildMgmVertex(
          agentId, 
          meetingId,
          constraints,
          problem.TIMESLOTS,
          agentIndex,
          meetingIndex,
          mgmGraph.graph
          )
            
      // add to neighbourhoods
      addToNeighbourhoods(meetingId, vertex, mgmGraph.neighbourhoods)
      
      // add to indices
      mgmGraph.vertices += vertex
      
    }
  
  def removeAgent(
      mgmGraph : MGMGraph,
      agentId: Int, 
      meetingId: Int) {
    
    if(mgmGraph.neighbourhoods.contains(meetingId)){
        var neighbourhood = mgmGraph.neighbourhoods.apply(meetingId)
        var vertex : MGMVertex = null
        for(neighbour <- neighbourhood){
          if(neighbour.AGENT_ID == agentId){
            vertex = neighbour
          }
        }
        
        // remove vertices
        mgmGraph.graph.removeVertex(vertex)
              
        // clear indices
        vertex.AGENT_INDEX.remove(meetingId)
        vertex.MEETING_INDEX.remove(agentId)
        mgmGraph.vertices -= vertex
        
        // adjust neighbourhoods
        neighbourhood.remove(vertex)
        mgmGraph.neighbourhoods += (meetingId -> neighbourhood)
       
      }
    
  }
}