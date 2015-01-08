package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.MeetingConstraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.stack.graph.GraphFactory

object MaxSumGraphFactory extends GraphFactory[MaxSumGraph, MeetingSchedulingProblem] {
  
  // Configuration
  final var MAX_SLOTS : Int = 1000 // Max communication slots
  final var MAX_ROUND : Int = 3000 // Limit of communication rounds
  
  // Current State
  var slot : Int = 0
  
  def build(problem : MeetingSchedulingProblem) : MaxSumGraph = {
    
      // initialize slots state (new build -> new run)
      slot = 0
      
      /**
       * Initialize graph
       */
      val graph = GraphBuilder.build
        
      /**
       * Indices
       */
      var varVertices = Set[VariableVertex]()
      var funcVertices = Set[FunctionVertex]()
      var neighbourhoods = Map[Int, Map[VariableVertex,FunctionVertex]]() // meetingId => variableId -> functionId
        
      /**
       * Build index for each meeting and all agents
       */
      var meetingIndices = Map[Int, Map[Any,Int]]()
      for(meeting <- problem.meetings){
        meetingIndices += (meeting.id -> Map[Any,Int]())
      }
      var agentIndices = Map[Int, Map[Any,Int]]()
        
      /**
       * Build participants
       */
      for(agentId : Int <- problem.allParticipations.keys){
        
        // Agent Index
        var agentIndex = Map[Any,Int]()
        agentIndices += agentId -> agentIndex

        // Process Meetings
        var meetingIds : Set[Int] = problem.allParticipations.apply(agentId)
        for(meetingId <- meetingIds){
          
          // get elements
          var meetingIndex = meetingIndices.apply(meetingId)
          var constraints = problem.allConstraints.apply(agentId)
          
          // build variable vertex
          var varVertex = buildVariableVertex(
            agentId,
            meetingId,
            constraints,
            problem.TIMESLOTS,
            agentIndex,
            meetingIndex,
            graph
          )
          
          // adjust indices
          varVertices += varVertex
          meetingIndex += (agentId -> constraints.preference.apply(meetingId))
          agentIndex += (meetingId -> constraints.preference.apply(meetingId))
          
          // build function vertex
          var funcVertex = buildFunctionVertex(
              agentId, 
              meetingId, 
              constraints,
              problem.TIMESLOTS,
              agentIndex,
              graph
          )
          funcVertices += funcVertex
          
          // add to neighbourhood
          addToNeighbourhoods(meetingId, varVertex, funcVertex, neighbourhoods)
        }
      } 
        
      // build edges
      for(neighbourhood <- neighbourhoods.values){
        connectNeighbourhood(neighbourhood, graph)
      }
        
      // return graph
      new MaxSumGraph(varVertices, funcVertices, neighbourhoods, agentIndices, meetingIndices, graph)
    }
  
    /**
     * Builder Functions
     */
     def buildVariableVertex(
         agentId : Int, 
         meetingId : Int,
         constraints : MeetingConstraints, 
         timeslots : Int,
         agentIndex : Map[Any,Int],
         meetingIndex : Map[Any,Int],
         graph : Graph[Any, Any]) : VariableVertex = {
       
       // increment monitoring slot
       slot += 1
       
       // build vertex
       var variableId : Any = "v" + agentId + "m" + meetingId
       var varVertex = new VariableVertex(variableId, null)
          
       // add parameters
       varVertex.MAX_ROUND = MAX_ROUND
       varVertex.PUSH_ROUND = slot
       varVertex.TIMESLOTS = timeslots
       varVertex.CONSTRAINTS_ORIGINAL = constraints.clone()
       varVertex.CONSTRAINTS_CURRENT = constraints.clone()
       varVertex.MEETING_INDEX = meetingIndex
       varVertex.AGENT_INDEX = agentIndex
       varVertex.MEETING_ID = meetingId
       varVertex.AGENT_ID = agentId
       
       // add to graph
       graph.addVertex(varVertex)
       
       // adjust slot
       if(slot == MAX_SLOTS){
         slot = 0
       }
       
       varVertex
     }
     
     def buildFunctionVertex(
         agentId : Int, 
         meetingId : Int,
         constraints : MeetingConstraints,
         timeslots : Int,
         agentIndex : Map[Any,Int],
         graph : Graph[Any, Any]) : FunctionVertex = {
       
       // build vertex
       var functionId : Any = "f" + agentId + "m" + meetingId
       var funcVertex = new FunctionVertex(functionId,null)
       
       // add parameters
       funcVertex.CONSTRAINTS_CURRENT = constraints
       funcVertex.TIMESLOTS = timeslots
       funcVertex.AGENT_INDEX = agentIndex
       
       // add to graph
       graph.addVertex(funcVertex)
       
       funcVertex
     }
     
     def addToNeighbourhoods(
         meetingId : Int,
         varVertex : VariableVertex, 
         funcVertex : FunctionVertex, 
         neighbourhoods : Map[Int, Map[VariableVertex,FunctionVertex]]
         ) : Map[Int, Map[VariableVertex,FunctionVertex]] = {
       
        var neighbourhood = Map[VariableVertex, FunctionVertex]()
          if(neighbourhoods.contains(meetingId)){
            neighbourhood = neighbourhoods.apply(meetingId)
          }
          neighbourhood += (varVertex -> funcVertex)
          neighbourhoods += (meetingId -> neighbourhood)
     }
     
     def connectNeighbourhood(neighbourhood : Map[VariableVertex,FunctionVertex], graph : Graph[Any, Any]){
       for(variableVertex <- neighbourhood.keys){
          for(functionVertex <- neighbourhood.values){
            graph.addEdge(variableVertex.id, new StateForwarderEdge(functionVertex.id))
            graph.addEdge(functionVertex.id, new StateForwarderEdge(variableVertex.id))
          }
        }
     }
     
    /**
     * Dynamic Functions
     */
    def addAgent(maxSumGraph : MaxSumGraph, problem : MeetingSchedulingProblem, agentId : Int, meetingId : Int){
      
      // prepare
      var participations = Set[Int](meetingId)
      var constraints : MeetingConstraints = MeetingSchedulingFactory.buildSingleConstraints(agentId, participations)
      var agentIndex = 
        if(maxSumGraph.agentIndices.contains(agentId)) 
          maxSumGraph.agentIndices.apply(agentId)
        else 
          Map[Any,Int]()
      var meetingIndex = 
        if(maxSumGraph.meetingIndices.contains(meetingId))
          maxSumGraph.meetingIndices.apply(meetingId)
        else
          Map[Any,Int]()
      
      // build
      var varVertex = buildVariableVertex(
          agentId, 
          meetingId,
          constraints,
          problem.TIMESLOTS,
          agentIndex,
          meetingIndex,
          maxSumGraph.graph
          )
      
      var funcVertex = buildFunctionVertex(
          agentId, 
          meetingId, 
          constraints, 
          problem.TIMESLOTS,
          agentIndex,
          maxSumGraph.graph
          )      
      
      // add to neighbourhoods
      addToNeighbourhoods(meetingId, varVertex, funcVertex, maxSumGraph.neighbourhoods)
      
      // add to indices
      maxSumGraph.varVertices += varVertex
      maxSumGraph.funcVertices += funcVertex
    }
    
    def removeAgent(maxSumGraph : MaxSumGraph, agentId : Int, meetingId : Int){
      
      if(maxSumGraph.neighbourhoods.contains(meetingId)){
        var neighbourhood = maxSumGraph.neighbourhoods.apply(meetingId)
        var varVertex : VariableVertex = null
        var funcVertex : FunctionVertex = null
        for(neighbour <- neighbourhood.keys){
          if(neighbour.AGENT_ID == agentId){
            varVertex = neighbour
            funcVertex = neighbourhood.apply(neighbour)
          }
        }
        
        // remove vertices
        maxSumGraph.graph.removeVertex(varVertex)
        maxSumGraph.graph.removeVertex(funcVertex)
                
        // clear indices
        varVertex.AGENT_INDEX.remove(meetingId)
        varVertex.MEETING_INDEX.remove(agentId)
        maxSumGraph.varVertices -= varVertex
        maxSumGraph.funcVertices -= funcVertex
           
        // clear neighbourhood
        neighbourhood.remove(varVertex)
        maxSumGraph.neighbourhoods += (meetingId -> neighbourhood)
          
      }
    }
    
}
