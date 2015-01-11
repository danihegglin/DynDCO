package ch.uzh.dyndco.stack.vertex

import com.signalcollect.DataGraphVertex
import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.util.Monitoring
import ch.uzh.dyndco.problems.MeetingSchedulingFactory

abstract class DynamicVertex (id: Any, initialState: Any) 
    extends MeetingSchedulingVertex(id, initialState) {
  
  def changeConstraints(){
      var participations : Set[Int] = Set[Int](MEETING_ID)
      var newConstraints = MeetingSchedulingFactory.buildSingleConstraints(AGENT_ID, participations)
      CONSTRAINTS_CURRENT = newConstraints.clone() 
      CONSTRAINTS_ORIGINAL = newConstraints.clone()
  }
  
  def changeDomain(domain : Any){
        TIMESLOTS = domain.asInstanceOf[Int]
        // FIXME
  }
  
}
