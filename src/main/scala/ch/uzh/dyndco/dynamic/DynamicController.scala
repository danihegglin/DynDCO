package ch.uzh.dyndco.dynamic

import com.signalcollect.Graph

class DynamicController (
    id: Any, 
    graph: Graph[Any,Any],
    ) extends DataGraphVertex(id, initialState) {
  
      // FIXME does not make much sense on single vertex
    def addMeeting(meeting : Any, graph : Graph[Any,Any]){
//      graph.addVertex()
    }
    
    def removeMeeting(meeting : Any, graph : Graph[Any,Any]){
      
    }
    
    def addParticipant(meeting : Any){
      
    }
    
    def removeParticipant(meeting : Any, participantID : Any){
      // remove from index
      // remove
    }

}