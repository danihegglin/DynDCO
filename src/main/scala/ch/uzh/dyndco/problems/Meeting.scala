package ch.uzh.dyndco.problems

import collection.mutable.Set
import collection.mutable.Map
import ch.uzh.dyndco.algorithms.maxsum.FunctionVertex

class Meeting(meetingId : Int) {
  
  var id = meetingId

  var participants : Map[Int,Set[FunctionVertex]] = Map[Int,Set[FunctionVertex]]()
  
  def addParticipant(agent : Int){
    participants += (agent -> Set())
  }
  
}