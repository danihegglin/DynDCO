package ch.uzh.dyndco.stack.vertex

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
     * Convergence Control
     */
    var converged : Boolean = false
    
    override def scoreSignal: Double = {
      if(this.converged) 0
      else 1
    }
  
  def convergenceCheck() = {
      
    // Check Max Round      
    if(roundCount >= MAX_ROUND){
      converged = true
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