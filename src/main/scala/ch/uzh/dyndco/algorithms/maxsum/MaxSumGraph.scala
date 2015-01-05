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
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.util.Tabulator
import ch.uzh.dyndco.stack.graph.DynamicGraph
import ch.uzh.dyndco.stack.vertex.DynamicVertex
import ch.uzh.dyndco.stack.graph.GraphFactory
import ch.uzh.dyndco.stack.vertex.MeetingSchedulingVertex

class MaxSumGraph (
      varVertices_ : Set[VariableVertex], 
      funcVertices_ : Set[FunctionVertex], 
      neighbourhoods_ : Map[Int, Map[VariableVertex,FunctionVertex]],
      agentIndices_ : Map[Int, Map[Any,Int]],
      meetingIndices_ : Map[Int, Map[Any,Int]],
      graph_ : Graph[Any,Any]
    )
    extends DynamicGraph {
  
  var varVertices = varVertices_
  var funcVertices = funcVertices_
  var neighbourhoods = neighbourhoods_
  var agentIndices = agentIndices_
  var meetingIndices = meetingIndices_
  var graph = graph_
  
  def nextNeighbourhood() : Int = neighbourhoods.size + 1
  def nextAgent : Int = varVertices.size + 1
  def numOfAgents : Int = varVertices.size
  def numOfNeighbourhoods : Int = neighbourhoods.size
  def getAgents : Set[DynamicVertex] = varVertices.asInstanceOf[Set[DynamicVertex]]
  
  def getFactory : GraphFactory[DynamicGraph, Problem] = MaxSumGraphFactory.asInstanceOf[GraphFactory[DynamicGraph, Problem]]
  
  def show {
     showMeetingResultsMultiple(neighbourhoods
         .asInstanceOf[Map[Int, Map[MeetingSchedulingVertex,MeetingSchedulingVertex]]])
     showAgentResults(varVertices
         .asInstanceOf[Set[MeetingSchedulingVertex]])
  }
}
