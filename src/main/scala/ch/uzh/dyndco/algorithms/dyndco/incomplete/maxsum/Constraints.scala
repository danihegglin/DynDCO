package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import collection.mutable.Map
import com.signalcollect.AbstractVertex

class Constraints (
    sender_ : AbstractVertex[Any, Any],
    hard_ : Set[Int], 
    soft_ : Set[Int], 
    preference_ : Set[Int]
    ) {
  
    var sender = sender_
    var hard = hard_
    var soft = soft_
    var preference = preference_
}
