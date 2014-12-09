package ch.uzh.dyndco.algorithms.maxsum

import collection.mutable.Set
import collection.mutable.Map

class Meeting(meetingId : Int) {
  
  var meetingID = meetingId

  var participants : Map[Int,Set[FunctionVertex]] = Map[Int,Set[FunctionVertex]]()
  
  def addParticipant(agent : Int){
    participants += (agent -> Set())
//    println("added participants: " + participants.size)
  }
  
//  def getParticipants() : Map[Int,Set[FunctionVertex]] = {
//    participants
//  }
  
//  def addParticipantFunctions(agent : Int, functions : Set[FunctionVertex]){
//    participants += (agent -> functions)
//  }
//  
//  def getOtherFunctions(agent : VariableVertex) : Set[FunctionVertex] = {
//    var otherFunctions : Set[FunctionVertex] = Set()
//    for(otherVariableVertex : VariableVertex <- participants.keys){
//      if(otherVariableVertex != variableVertex){
//        var functions : Set[FunctionVertex] = participants.apply(otherVariableVertex)
//        for(function <- functions){
//          otherFunctions + function
//        }
//      }
//    }
//    otherFunctions
//  }
  
}