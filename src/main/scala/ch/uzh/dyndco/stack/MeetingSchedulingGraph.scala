package ch.uzh.dyndco.stack

import collection.mutable.Map
import collection.mutable.Set

trait MeetingSchedulingGraph extends BasicGraph {
  
    def agentIndices : Map[Int, Map[Any,Int]]
    def meetingIndices : Map[Int, Map[Any,Int]]
  
}