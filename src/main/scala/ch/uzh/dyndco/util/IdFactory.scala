package ch.uzh.dyndco.util

object IdFactory {

  def build(timeslots : Int, meetings : Int, agents : Int, run : Int) : String = {
    var id = "a" + agents + "m" + meetings + "t" + timeslots + "_" + run
    id
  }
  
}