package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Problem
import ch.uzh.dyndco.algorithms.maxsum.MaxSumGraph
import ch.uzh.dyndco.problems.MeetingSchedulingProblem

trait GraphFactory {
  
  def build(problem : Problem) : DynamicGraph
  def addAgent(dynamicGraph : DynamicGraph, problem : Problem, agentId : Int, meetingId : Int)
  def removeAgent(dynamicGraph : DynamicGraph, agentId : Int, meetingId : Int)
  
}