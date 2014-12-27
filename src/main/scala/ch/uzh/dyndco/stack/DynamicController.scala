package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import com.signalcollect.DataGraphVertex

class DynamicController (id: Any, graph_ : Graph[Any,Any]) 
  extends DataGraphVertex(id, graph_) {
  
  var graph = graph_
  
    def addMeeting(meetingID : Any){
//      graph.addVertex()
      
      // number of meetings
    }
    
    def removeMeeting(meeting : Any){
      
    }
    
    def addParticipant(meeting : Any){
      
      // number of agents
      
    }
    
    def removeParticipant(meeting : Any, participantID : Any){
      // remove from index
      // remove
    }
    
    def collect() = {
      null
    }

}