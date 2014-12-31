package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import collection.mutable.Map
import collection.mutable.Set

trait DynamicGraph extends MeetingSchedulingGraph {
  
  def nextNeighbourhood() : Int
  def nextAgent() : Int
  def numOfAgents() : Int
  def numOfNeighbourhoods() : Int
  def getAgents() : Set[DynamicVertex]

}