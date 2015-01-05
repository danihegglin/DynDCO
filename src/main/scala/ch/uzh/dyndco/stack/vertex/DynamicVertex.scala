package ch.uzh.dyndco.stack.vertex

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.util.Monitoring

abstract class DynamicVertex (id: Any, initialState: Any) 
    extends MeetingSchedulingVertex(id, initialState) {
  
  /**
   * Round control
   */
  var CHANGE_ROUND : Int = -1
  
  def isChangeRound() : Boolean = {

    // Check Change
    if(roundCount >= CHANGE_ROUND){
        changeConstraints()
        true
    }
    else{
      false
    }
    
  }
  
  def changeConstraints(){
//      var participations : Set[Int] = Set[Int](meetingID)
//      constraints = MeetingSchedulingFactory.buildSingleConstraints(id, participations)
   }
  
}
