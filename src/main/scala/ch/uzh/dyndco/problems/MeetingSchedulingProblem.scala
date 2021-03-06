package ch.uzh.dyndco.problems

import collection.mutable.Set
import collection.mutable.Map
import scala.collection.mutable.MutableList

class MeetingSchedulingProblem (
    meetings_ : MutableList[Meeting],
    allParticipations_ : Map[Int, Set[Int]],
    allConstraints_ : Map[Int, MeetingConstraints],
    TIMESLOTS_ : Int, 
    MEETINGS_ : Int, 
    AGENTS_ : Int
    ) extends Problem {
  
  var meetings = meetings_
  var allParticipations = allParticipations_
  var allConstraints = allConstraints_
  
  var TIMESLOTS = TIMESLOTS_
  var MEETINGS = MEETINGS_
  var AGENTS = AGENTS_
  
  def getDomain() : Any = TIMESLOTS
  def setDomain(domain : Any) {TIMESLOTS = domain.asInstanceOf[Int]}
  def increaseDomain(percentage : Double){ 
    // override 
  }
  def decreaseDomain(percentage : Double){
    // override
  }
  
}