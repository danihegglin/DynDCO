package ch.uzh.dyndco.problems;

import collection.mutable.Map
import collection.mutable.Set

class MeetingConstraints (
		  sender_ : Any,
	    blocked_ : Set[Int], 
	    free_ : Set[Int], 
	    preferred_ : Map[Int,Int]
		) {
  
  var sender : Any = sender_
  var blocked : Set[Int] = blocked_
  var free : Set[Int] = free_
  var preferred : Map[Int,Int] = preferred_
  
  def update(meetingId : Int, pref : Int){
    if(preferred != null){
      try {
          
          // add to prefered
          var prefNew = preferred.clone()
          prefNew.put(meetingId, pref)
          preferred = prefNew
          
          // remove from free
          if(free.contains(pref))
            free.remove(pref)
          
      } catch {
           case e : Exception => println("PREF FAIL: " + e.printStackTrace())
      }
      
    }
    else {
      println("preference is null")
    }
  }
  
  override def clone() : MeetingConstraints = {
    new MeetingConstraints(sender,blocked,free,preferred)
  }
  
  def show() = {
    println(
        sender + ":" + 
        " blocked: " + blocked + 
        " preferred: " + preferred
        )
  }

}
