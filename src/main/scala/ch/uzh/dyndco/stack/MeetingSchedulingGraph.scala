package ch.uzh.dyndco.stack

import collection.mutable.Map
import collection.mutable.Set
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.util.Tabulator

trait MeetingSchedulingGraph extends BasicGraph {
  
    def agentIndices : Map[Int, Map[Any,Int]]
    def meetingIndices : Map[Int, Map[Any,Int]]
    
    def showMeetingResultsMultiple(neighbourhoods : Map[Int, Map[MeetingSchedulingVertex,MeetingSchedulingVertex]]){
      
      var meetingResults = MutableList[List[Any]]()
      meetingResults += List[Any]("meeting","timeslot")
      for(meeting <- neighbourhoods.keys.toList.sorted){
        
        var correct = true
        var value = -1
        var wrong = Set[Int]()
        for(neighbour <- neighbourhoods.apply(meeting).keys){
          if(value < 0){
            value = neighbour.value
          }
          else {
            if(value != neighbour.value){
              correct = false
              wrong += neighbour.value
            }
          }
        }
        
        if(correct){
          meetingResults += List[Any](meeting,value)
        }
        else {
          meetingResults += List[Any](meeting,value + ", " + wrong)
        }
      }
      println(Tabulator.format(meetingResults))
      
    }
    
    def showMeetingResults(neighbourhoods : Map[Int, Set[MeetingSchedulingVertex]]){
      
      var meetingResults = MutableList[List[Any]]()
      meetingResults += List[Any]("meeting","timeslot")
      for(meeting <- neighbourhoods.keys.toList.sorted){
        var correct = true
        var value = -1
        var wrong = Set[Int]()
        for(neighbour <- neighbourhoods.apply(meeting)){
          if(value < 0){
            value = neighbour.value
          }
          else {
            if(value != neighbour.value){
              correct = false
              wrong += neighbour.value
            }
          }
        }
        
        if(correct){
          meetingResults += List[Any](meeting,value)
        }
        else {
          meetingResults += List[Any](meeting,value + ", " + wrong)
        }
      }
      println(Tabulator.format(meetingResults))
            
    }
    
    def showAgentResults(vertices : Set[MeetingSchedulingVertex]){
      
     var agentResults = MutableList[List[Any]]()
     agentResults += List[Any]("agent","no overlaps")
     for(vertex <- vertices){
        
        var fullSize = vertex.AGENT_INDEX.size
        var distinctSize = vertex.AGENT_INDEX.values.toList.distinct.size
        
        var isDifferent = true
        if(fullSize != distinctSize)
          isDifferent = false
          agentResults += List[Any](vertex.id,isDifferent)
      }
      println(Tabulator.format(agentResults))
      
    }
  
}