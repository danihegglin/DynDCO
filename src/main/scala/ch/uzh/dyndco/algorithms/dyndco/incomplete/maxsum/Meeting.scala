package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum

import collection.mutable.Set
import collection.mutable.Map

class Meeting(meetingId : Int) {

  var participants : Map[VariableVertex,Set[FunctionVertex]] = Map[VariableVertex,Set[FunctionVertex]]()
  
  def addParticipant(variableVertex : VariableVertex){
    participants += (variableVertex -> Set())
    println("added participants: " + participants.size)
  }
  
  def addParticipantFunctions(variableVertex : VariableVertex, functions : Set[FunctionVertex]){
    participants += (variableVertex -> functions)
  }
  
  def getOtherFunctions(variableVertex : VariableVertex) : Set[FunctionVertex] = {
    var otherFunctions : Set[FunctionVertex] = Set()
    for(otherVariableVertex : VariableVertex <- participants.keys){
      if(otherVariableVertex != variableVertex){
        var functions : Set[FunctionVertex] = participants.apply(otherVariableVertex)
        for(function <- functions){
          otherFunctions + function
        }
      }
    }
    otherFunctions
  }
}