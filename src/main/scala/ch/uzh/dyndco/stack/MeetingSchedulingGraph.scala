package ch.uzh.dyndco.stack

import collection.mutable.Map
import collection.mutable.Set

trait MeetingSchedulingGraph extends BasicGraph {
  
//    def neighbourhoods : Map[Int, Any]
    def agentIndices : Map[Int, Map[Any,Int]]
    def meetingIndices : Map[Int, Map[Any,Int]]
  
}