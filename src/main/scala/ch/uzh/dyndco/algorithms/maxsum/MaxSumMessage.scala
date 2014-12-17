package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints

class MaxSumMessage (
      sender_ : Any,
      utilities_ : Map[Any, Map[Int, Double]]
    ) {
  
  var sender = sender_
  var utilities = utilities_
  
}
