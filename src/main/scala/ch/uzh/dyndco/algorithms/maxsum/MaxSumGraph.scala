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
import ch.uzh.dyndco.stack.DynamicGraph
import ch.uzh.dyndco.stack.DynamicVertex

class MaxSumGraph (
    varVertices_ : Set[VariableVertex], 
    funcVertices_ : Set[FunctionVertex], 
    neighbourhoods_ : Map[Int, Map[VariableVertex,FunctionVertex]],
    agentIndices_ : Map[Int, Map[Any,Int]],
    meetingIndices_ : Map[Int, Map[Any,Int]],
    graph_ : Graph[Any,Any]) extends DynamicGraph {
  
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
  
  def show {
    for(meeting <- neighbourhoods.keys.toList.sorted){
      var correct = true
      var value = -1
      var wrong = Set[Int]()
      for(neighbour <- neighbourhoods.apply(meeting).keys){
        if(value < 0){
          value = neighbour.bestValueAssignment
        }
        else {
          if(value != neighbour.bestValueAssignment){
            correct = false
            wrong += neighbour.bestValueAssignment
          }
        }
      }
      
      if(correct){
        println("meeting " + meeting + " -> " + value)
      }
      else {
         println("meeting " + meeting + " -> " + value + ", " + wrong)
      }
    }
    
   for(vertex <- varVertices){
      
      var fullSize = vertex.AGENT_INDEX.size
      var distinctSize = vertex.AGENT_INDEX.values.toList.distinct.size
      
      var isDifferent = true
      if(fullSize != distinctSize)
        isDifferent = false
        
      println(vertex.id + " -> " + isDifferent)
    }
  }
}
