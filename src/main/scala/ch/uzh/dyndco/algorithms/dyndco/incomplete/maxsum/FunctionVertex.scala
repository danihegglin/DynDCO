package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect.DataGraphVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.VariableVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.Constraints

class FunctionVertex (
      id: Any, 
      initialState: Constraints
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
	
	/**
	 * Score signal function
	 */
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
    
    // Add signal sender to senders
        //var senders = Set[VariableVertex]()
    targetIds.iterator; // Iterates all targetids of the vertex
    
    // Process constraints
    var hardConstraints = collection.mutable.Map[Object, Set[Int]]() // Blocked timeslots (vertex, set of constraints)
    var softConstraints = collection.mutable.Map[Object, Set[Int]]() // Free timeslots (vertex, set of constraints)
    var preference = collection.mutable.Map[Object, Set[Int]]() // Proposed timeslots (vertex, set of constraints)
    
    for (signal <- signals.iterator) {
      
      // Process proposal -> create hard, soft and preference builds for every target
      var constraints : Constraints = signal
      
      // work the blocked slots
//      for(hard <- constraints.hard){
        hardConstraints + (constraints.sender -> constraints.hard)
//      }
      
      // work the free slots
//      for(soft <- constraints.soft){
        softConstraints + (constraints.sender -> constraints.soft)
//      }
      
      // work the proposed slot
      for(preference <- constraints.preference){
        
      }
    }
    
    // Calculate costs for each meeting assignment for each sender (his preference build)
    
    
    // Choose minimal costs assignment -> build packet for variable with all assignments
    
    
    
    1
  }
	
	// needs a function that calculates the product of all utilities of all variable assignments
	
	// this function represents the constraints of one agent

}
