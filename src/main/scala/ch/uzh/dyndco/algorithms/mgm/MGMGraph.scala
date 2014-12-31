package ch.uzh.dyndco.algorithms.mgm

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

class MGMGraph (
    vertices_ : Set[MGMVertex], 
    neighbourhoods_ : Map[Int, Set[MGMVertex]], 
    graph_ : Graph[Any,Any]) extends DynamicGraph {
  
  var vertices = vertices_
  var neighbourhoods = neighbourhoods_
  var graph = graph_
  
  def nextNeighbourhood() : Int = neighbourhoods.size + 1
  def nextAgent : Int = vertices.size + 1
  def numOfAgents : Int = vertices.size
  def getAgents : Set[MGMVertex] = vertices 
  
   def show {
  
    for(meeting <- neighbourhoods.keys.toList.sorted){
      var correct = true
      var value = -1
      var wrong = Set[Int]()
      for(neighbour <- neighbourhoods.apply(meeting)){
        if(value < 0){
          value = neighbour.lastValue
        }
        else {
          if(value != neighbour.lastValue){
            correct = false
            wrong += neighbour.lastValue
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
