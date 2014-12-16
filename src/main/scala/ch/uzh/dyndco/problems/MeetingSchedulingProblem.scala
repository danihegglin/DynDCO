package ch.uzh.dyndco.problems

import collection.mutable.Set
import collection.mutable.Map
import scala.collection.mutable.MutableList
import ch.uzh.dyndco.algorithms.maxsum.Meeting

class MeetingSchedulingProblem(
    meetings_ : MutableList[Meeting],
    allParticipations_ : Map[Int, Set[Int]],
    allConstraints_ : Map[Int, Constraints],
    TIMESLOTS_ : Int, 
    MEETINGS_ : Int, 
    AGENTS_ : Int
    ) {
  
  var meetings = meetings_
  var allParticipations = allParticipations_
  var allConstraints = allConstraints_
  
  var TIMESLOTS = TIMESLOTS_
  var MEETINGS = MEETINGS_
  var AGENTS = AGENTS_
  
}