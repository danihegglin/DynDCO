package ch.uzh.dyndco.problems;

import collection.mutable.Map
import collection.mutable.Set

class MeetingConstraints (
		  sender_ : Any,
	    hard_ : Set[Int], 
	    soft_ : Set[Int], 
	    preference_ : Map[Int,Int]
		) {
  
  var sender : Any = sender_
  var hard : Set[Int] = hard_
  var soft : Set[Int] = soft_
  var preference : Map[Int,Int] = preference_
  
  def update(meetingId : Int, pref : Int){
    if(preference != null){
      try {
          
          // add to pref
          var prefNew = preference.clone()
          prefNew.put(meetingId, pref)
          preference = prefNew
          
          // remove from soft constraints
          if(soft.contains(pref))
            soft.remove(pref)
          
      } catch {
           case e : Exception => println("PREF FAIL: " + e.printStackTrace())
      }
      
    }
    else {
      println("preference is null")
    }
  }
  
  override def clone() : MeetingConstraints = {
    new MeetingConstraints(sender,hard,soft,preference)
  }
  
  def show() = {
    println(
        sender + ":" + 
        " hard: " + hard + 
        " pref: " + preference
        )
  }

}
