package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex

class VariableVertex (
      id: Any, 
      initialState: MeetingProposal
    ) extends DataGraphVertex(id, initialState) {
  
   /**
	 * Finish boolean
	 */
	var finished : Boolean = false
  
  	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = Constraints
	
		override def scoreSignal: Double = {
      
	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
	  
	  if(this.finished) 
	    0
	   else
	     1
     }
	
		// calculate sum of all costs received, choose the one with lowest costs, send to funtionvertex
  def collect() = {
    
     // unpack messages
    for (signal <- signals.iterator) {
     var constraints : Constraints = signal  
    }
    
    // combine all assignments (different meetings) to a complete one
    // find the assignment with the minimal sum of costs
    // constraint: no overlapping of meetings
    // constraint: no overlapping with blocks
    // question: does this include the preferences of current user?
    
    
    // Adjust own preferences
    
    // Send out new preferences
    meetingProposal
  }
  
}
