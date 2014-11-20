package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum

import collection.mutable.Set
import collection.mutable.Map

class Meeting(meetingId : Int) {

  var participants : Map[VariableVertex,Set[FunctionVertex]] = Map[VariableVertex,Set[FunctionVertex]]()
  var participantsCount = 0
  
  def addParticipant(variableVertex : VariableVertex){
    participantsCount+1
    participants + (variableVertex -> null)
  }
  
  def addParticipantFunctions(variableVertex : VariableVertex, functions : Set[FunctionVertex]){
    participants + (variableVertex -> functions)
  }
  
  def getOtherFunctions(variableVertex : VariableVertex) : Set[FunctionVertex] = {
    var otherFunctions : Set[FunctionVertex] = Set()
    for(otherVariableVertex : VariableVertex <- participants.keys){
      if(otherVariableVertex != variableVertex){
        otherFunctions + participants.apply(otherVariableVertex)
      }
    }
    otherFunctions
  }
}