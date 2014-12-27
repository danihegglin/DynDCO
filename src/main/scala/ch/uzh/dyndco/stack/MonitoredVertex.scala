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
  
    def newRound(){
      roundCount += 1
    }
    
 /**
  * Utility Message Control
  */
 var messages : Map[String, Double] = Map[String, Double]()
  
    var agentUtility : Double = 0
    def storeUtility(){
            
          // add current utility to messages
            val timestamp: Long = System.currentTimeMillis / 1000
            messages += timestamp.toString() -> agentUtility
            
            // Send if reached max
            if(roundCount % PUSH_ROUND == 0){
              println("push")
              Monitoring.update(id, messages)
              messages.clear()
            }
    }

}