package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Map
import collection.mutable.Set
import com.signalcollect.AbstractVertex
import ch.uzh.dyndco.problems.Constraints

class MaxSumMessage (
    sender_ : Any,
    hard_ : Set[Int], 
    soft_ : Set[Int], 
    preference_ : Map[Int,Int]
    ) {
  
  var sender : Any = sender_
  var hard : Set[Int] = hard_
  var soft : Set[Int] = soft_
  var preference : Map[Int,Int] = preference_
  
  var allCostAssignments : Map[Any, Map[Int, Double]] = Map[Any, Map[Int, Double]]()
  var participationIndex : Map[Any, Map[Int,Int]] = Map[Any, Map[Int,Int]]()
    
  def addCostAssignments(allCostAssignments_ : Map[Any, Map[Int,Double]]) = {
    allCostAssignments = allCostAssignments_
  }
  
  def addParticipationIndex(participationIndex_ : Map[Any, Map[Int,Int]]) = {
    participationIndex = participationIndex_
  }
  
}
