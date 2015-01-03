package ch.uzh.dyndco.algorithms.mgm

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.MeetingConstraints
import scala.collection.mutable.MutableList
import com.signalcollect.Graph
import com.signalcollect.StateForwarderEdge
import ch.uzh.dyndco.problems.MeetingSchedulingProblem
import com.signalcollect.GraphBuilder
import ch.uzh.dyndco.stack.DynamicGraph
import ch.uzh.dyndco.stack.DynamicVertex
import ch.uzh.dyndco.stack.GraphFactory
import ch.uzh.dyndco.problems.Problem

class MGMGraph (
    vertices_ : Set[MGMVertex], 
    neighbourhoods_ : Map[Int, Set[MGMVertex]],
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
  def getFactory : GraphFactory[DynamicGraph, Problem] = MGMGraphFactory.asInstanceOf[GraphFactory[DynamicGraph, Problem]]

  
   def show {
  
    for(meeting <- neighbourhoods.keys.toList.sorted){
      var correct = true
      var value = -1
      var wrong = Set[Int]()
      for(neighbour <- neighbourhoods.apply(meeting)){
        if(value < 0){
          value = neighbour.value
        }
        else {
          if(value != neighbour.value){
            correct = false
            wrong += neighbour.value
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
    
    for(vertex <- vertices){
      
      var fullSize = vertex.AGENT_INDEX.size
      var distinctSize = vertex.AGENT_INDEX.values.toList.distinct.size
      
      var isDifferent = true
      if(fullSize != distinctSize)
        isDifferent = false
        
      println(vertex.id + " -> " + isDifferent)
    }
    
  }
  
}
