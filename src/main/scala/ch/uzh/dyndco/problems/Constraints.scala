package ch.uzh.dyndco.problems;

import collection.mutable.Map
import collection.mutable.Set

class Constraints (
		  sender_ : Any,
	    hard_ : Set[Int], 
	    soft_ : Set[Int], 
	    preference_ : Map[Int,Int]
		) {
  
  var sender : Any = sender_
  var hard : Set[Int] = hard_
  var soft : Set[Int] = soft_
  var preference : Map[Int,Int] = preference_
  
  def show() = {
    println(
        sender + ":" + 
        " hard: " + hard + 
        " pref: " + preference
        )
  }

}
