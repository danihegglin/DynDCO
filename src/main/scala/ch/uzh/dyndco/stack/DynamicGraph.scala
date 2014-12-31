package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import collection.mutable.Map

trait DynamicGraph extends MeetingSchedulingGraph {
  
  def nextNeighbourhood() : Int
  def nextAgent() : Int
  def numOfAgents() : Int
  def getAgents() : Set[DynamicVertex]

}