package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.VariableVertex

class FunctionVertex (
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
	type Signal = MeetingProposal
	
		override def scoreSignal: Double = {
      
	  //println(id + ": running scoreSignal: " + lastSignalState + " " + finished)
	  
	  if(this.finished) 
	    0
	   else
	     1
     }
	
	// react: variable message -> create message using messages from Neighbours except the receiver node
	// product of all messages!
  def collect() = {
    
    var blocks = collection.mutable.Map[Int, Int]()
    var free = collection.mutable.Map[Int, Int]()
    var proposed = collection.mutable.Map[Int, Int]()
    
    var sender = Set[VariableVertex]()
    
    // Process messages
    for (signal <- signals.iterator) {
      
      // Add signal sender to senders
      
      var meetingProposal : MeetingProposal = signal
      
      // work the blocked slots
      for(block <- meetingProposal.blocks){
        
      }
      
      // work the free slots
      for(free <- meetingProposal.free){
        
      }
      
      // work the proposed slot
      for(proposed <- meetingProposal.proposed){
        
      }
    }
    
    // Calculate costs for each meeting assignment for each sender
    
    
    
    1
  }
	
	// needs a function that calculates the product of all utilities of all variable assignments
	
	// this function represents the constraints of one agent

}
