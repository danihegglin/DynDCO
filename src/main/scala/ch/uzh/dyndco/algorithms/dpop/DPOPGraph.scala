package ch.uzh.dyndco.algorithms.dpop

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.stack.graph.DynamicGraph
import ch.uzh.dyndco.stack.vertex.DynamicVertex
import ch.uzh.dyndco.stack.graph.GraphFactory
import ch.uzh.dyndco.stack.vertex.MeetingSchedulingVertex

class DPOPGraph (
    vertices_ : Set[DPOPVertex], 
    neighbourhoods_ : Map[Int, Set[DPOPVertex]],
    agentIndices_ : Map[Int, Map[Any,Int]],
    meetingIndices_ : Map[Int, Map[Any,Int]],
    graph_ : Graph[Any,Any]) extends DynamicGraph {
  
  var vertices = vertices_
  var neighbourhoods = neighbourhoods_
  var agentIndices = agentIndices_
  var meetingIndices = meetingIndices_
  var graph = graph_
  
  def nextNeighbourhood() : Int = neighbourhoods.size + 1
  def nextAgent : Int = vertices.size + 1
  def numOfAgents : Int = vertices.size
  def numOfNeighbourhoods : Int = neighbourhoods.size
  def getAgents : Set[DynamicVertex] = vertices.asInstanceOf[Set[DynamicVertex]]
  def getFactory : GraphFactory[DynamicGraph, Problem] = DPOPGraphFactory.asInstanceOf[GraphFactory[DynamicGraph, Problem]]
  
  def show {
     
    showMeetingResults(neighbourhoods
         .asInstanceOf[Map[Int, Set[MeetingSchedulingVertex]]])
     showAgentResults(vertices
         .asInstanceOf[Set[MeetingSchedulingVertex]])  
  
  }
  
}
