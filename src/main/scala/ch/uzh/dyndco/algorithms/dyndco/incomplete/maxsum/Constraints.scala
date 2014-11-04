package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import collection.mutable.Map
import com.signalcollect.AbstractVertex

class Constraints (
    sender_ : Any,
    hard_ : Set[Int], 
    soft_ : Set[Int], 
    preference_ : Set[Int]
    ) {
  
    var sender : Any = sender_
    var hard = hard_
    var soft = soft_
    var preference = preference_
    var allCostAssignments : Map[Any, Map[Int, Double]]
    
    def addCostAssignments(allCostAssignments_ : Map[Any, Map[Int,Double]]) = {
      allCostAssignments = allCostAssignments_
    }
}
