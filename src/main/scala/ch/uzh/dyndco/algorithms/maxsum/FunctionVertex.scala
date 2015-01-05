package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.stack.vertex.DynamicVertex

class FunctionVertex (id : Any, initialState: MaxSumMessage) 
  extends DynamicVertex(id, initialState) {
  
  /**
   * This avoids type-checks/-casts.
   */
  type Signal = MaxSumMessage
  
  /**
   * Collect Signals
   */
	def collect() = {
    
  		// Process messages
      var receivedUtilities = Map[Any, Map[Int,Double]]()
  		for (signal <- signals.iterator) {
        try {
  		    var message : MaxSumMessage = signal
          var messageUtilities = message.utilities
          for(sender <- messageUtilities.keys){
            receivedUtilities += (sender -> message.utilities.apply(sender))
          }
        }
        catch {
          case e : Exception => 
            //println("signal was null")
        }
  		}
      
  	  // Build Utilities for all Variable Vertices
  	  val allUtilities = Map[Any, Map[Int,Double]]()
  	  for(messageReceiver <- targetIds.iterator){
          
          var utilities = Map[Int, Double]()
          
          // add utilities from messages
          for(messageSender <- receivedUtilities.keys){
            if(messageReceiver != messageSender){
              var sentUtilities = receivedUtilities.apply(messageSender)
              for(timeslot <- sentUtilities.keys){
                var utility = 0.0
                if(utilities.contains(timeslot)){
                  utility += utilities.apply(timeslot)
                }
                utility += sentUtilities.apply(timeslot)
                utilities += (timeslot -> utility)
              }
            }
         }
          
         // add original utility represented by f -> 
         var currentUtilities = calculateAllUtilities(CONSTRAINTS_CURRENT)
         for(timeslot <- utilities.keys){
           var utility = utilities.apply(timeslot)
           utility += currentUtilities.apply(timeslot)
           utilities += (timeslot -> utility)
         } 
  				
  		   // build constraints object for the assignments
  		   allUtilities += (messageReceiver -> utilities) // Target -> Assignment : Cost
  		}
      
  	  new MaxSumMessage(id, allUtilities)
  }
}
