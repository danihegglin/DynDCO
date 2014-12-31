package ch.uzh.dyndco.stack

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.util.Monitoring

abstract class MonitoredVertex (id: Any, initialState: Any) 
  extends DataGraphVertex(id, initialState) {
  
    /**
     * Configuration
     */    
    var MAX_ROUND : Int = -1
    var PUSH_ROUND = -1
//    var SLOTS = -1
  
    /**
     * Finish Control
     */
    var finished : Boolean = false
    
    override def scoreSignal: Double = {
      if(this.finished) 0
      else 1
    }
  
  def finishedCheck() = {
      
    // Check Max Round      
    if(roundCount >= MAX_ROUND){
      finished = true
    }
          
  }
  
  /**
   * Round Control
   */
    var roundCount = 0
    var cycleCount = 0
  
    def newRound(){
      roundCount += 1
      cycleCount += 1
      
//      if(cycleCount == MAX_SLOTS){
//        cycleCount = 0
//      }
    }
    
 /**
  * Utility Message Control
  */
 var messages : Map[String, Double] = Map[String, Double]()
  
    var agentUtility : Double = 0
    def storeUtility(){
            
          // add current utility to messages
            val timestamp: Long = System.currentTimeMillis
            messages += timestamp.toString() -> agentUtility
            
            // Send if reached max
            if(cycleCount == PUSH_ROUND){
//              println("push: " + cycleCount + " | " + id + " | " + PUSH_ROUND + " | " + messages.size)
              Monitoring.update(id, messages)
              messages.clear()
              cycleCount = 0
            }
    }

}