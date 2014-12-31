package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.algorithms.maxsum.MaxSumGraph
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

trait GraphFactory [GRAPH <: DynamicGraph, PROBLEM <: Problem] {
  
  def build(problem : PROBLEM) : GRAPH
  def addAgent(dynamicGraph : GRAPH, problem : PROBLEM, agentId : Int, meetingId : Int)
  def removeAgent(dynamicGraph : GRAPH, agentId : Int, meetingId : Int)
  
}