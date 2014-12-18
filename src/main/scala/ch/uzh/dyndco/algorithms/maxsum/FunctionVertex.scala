package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import ch.uzh.dyndco.dynamic.DynamicVertex

class FunctionVertex (
		id : Any, 
		initialState: MaxSumMessage,
		valueSpace: Int
		) extends DynamicVertex(id, initialState) {
  
	/**
	 * Config
	 */
	final var HARD_UTILITY : Double = -20
	final var SOFT_UTILITY : Double = 5
	final var PREF_UTILITY : Double = 10

	/**
	 * Control parameters
	 */
	var finished : Boolean = false
	var finishedCount : Int = 0
	var marginalUtilityHistory = Map[Any, Map[Int,Double]]()

	/**
	 * Indicates that every signal this vertex receives is
	 * an instance of Int. This avoids type-checks/-casts.
	 */
	type Signal = MaxSumMessage

	/**
	 * Score signal function
	 */
	override def scoreSignal: Double = {
		if(this.finished) 0
		else 1
	}
  
  /**
   * Collect Signals
   */
	def collect() = {
    
  		// Unpack messages
      var isNull : Boolean = false
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
      
  	  // create hard, soft and preference builds for every target with minimal costs
  	  val allUtilities = Map[Any, Map[Int,Double]]()
  	  for(messageReceiver <- targetIds.iterator){
          var utilities = Map[Int, Double]()
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
  				
  		 // build constraints object for the assignments
  		 allUtilities += (messageReceiver -> utilities) // Target -> Assignment : Cost
  		}
      
  	  new MaxSumMessage(id, allUtilities)
  }
}
