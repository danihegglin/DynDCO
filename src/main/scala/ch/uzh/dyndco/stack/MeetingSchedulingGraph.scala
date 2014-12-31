package ch.uzh.dyndco.stack

trait MeetingSchedulingGraph extends BasicGraph {
  
    def neighbourhoods : Map[Int, Any]
    def agentIndices_ : Map[Int, Map[Any,Int]]
    def meetingIndices_ : Map[Int, Map[Any,Int]]
  
}