package ch.uzh.dyndco.stack

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.util.Monitoring

abstract class BasicVertex (id: Any, initialState: Any) 
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
    var cycleCount = 0
  
    def newRound(){
      roundCount += 1
      cycleCount += 1
    }

}