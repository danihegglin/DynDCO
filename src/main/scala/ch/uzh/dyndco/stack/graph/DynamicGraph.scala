package ch.uzh.dyndco.stack.graph

import com.signalcollect.Graph
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.stack.vertex.DynamicVertex

trait DynamicGraph extends MeetingSchedulingGraph {
  
  def nextNeighbourhood() : Int
  def nextAgent() : Int
  def numOfAgents() : Int
  def numOfNeighbourhoods() : Int
  def getAgents() : Set[DynamicVertex]
  
  
  
}