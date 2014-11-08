package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex

class Proposal (
    sender_ : Any,
    hard_ : Set[Int], 
    soft_ : Set[Int], 
    preference_ : Map[Any,Int]
    ) {
  
  var sender : Any = sender_
  var hard : Set[Int] = hard_
  var soft : Set[Int] = soft_
  var preference : Map[Any,Int] = preference_
  
  var allCostAssignments : Map[Any, Map[Int, Double]] = Map[Any, Map[Int, Double]]()
    
  def addCostAssignments(allCostAssignments_ : Map[Any, Map[Int,Double]]) = {
    allCostAssignments = allCostAssignments_
  }
}
