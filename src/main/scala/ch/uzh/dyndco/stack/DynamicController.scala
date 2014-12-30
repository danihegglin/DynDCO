package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.maxsum.VariableVertex

class DynamicController (id: Any, graph_ : Graph[Any,Any], vertices_ : Set[DynamicVertex]) 
  extends DataGraphVertex(id, graph_) {
  
  /**
   * Connections
   */
  var graph = graph_
  var vertices = vertices_
  
  /**
   * New Containers
   */
  var meetings = Set[Int]()
  
  /**
   * Configuration
   */
  var CONSTRAINT_SWITCH_TIME = 10000 // every ten seconds 
  var CONSTRAINT_SWITCH_PERCENTAGE = 50 // 50 % of all vertices get switched
  var MEETING_ADD_TIME = 20000 // every twenty seconds 
  var MEETING_ADD_AMOUNT = 1 // adds five meetings all twenty seconds
  
  
    def addMeeting(meetingID : Any){
    
//      graph.addVertex()
      
      // number of meetings
    }
    
    def removeMeeting(meeting : Any){
      
    }
    
    def addParticipant(meeting : Any){
      
      graph.addVertex(new VariableVertex(null,null))
      
      // number of agents
      
    }
    
    def removeParticipant(meeting : Any, participantID : Any){
      // remove from index
      // remove
    }
    
    def collect() = {
      null
    }
    
    /**
     * Test runners
     */
    def initializeSwitch(){
        while(true){
          println("switcher")
        }      
    }
    
    def initializeAdding(){
        while(true){
          println("adder")
        }   
    }
    
    def initializeRemovals(){
        while(true){
          println("removals")
        }   
    }

}