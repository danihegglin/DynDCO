package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.algorithms.maxsum.MaxSumGraph
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

trait GraphFactory {
  
  def build(problem : MeetingSchedulingProblem)
  def addVertex(graph : Graph[Any, Any], dynamicGraph : DynamicGraph, agentId : Int, meetingId : Int)
  def removeVertex(graph : Graph[Any, Any])
  def addEdge(graph : Graph[Any, Any], from : DynamicVertex, to : DynamicVertex)
}