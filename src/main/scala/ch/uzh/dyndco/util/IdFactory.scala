package ch.uzh.dyndco.util

object IdFactory {

  def build(
      algorithm : String, 
      execution : String, 
      mode : String, 
      timeslots : Int, 
      meetings : Int, 
      agents : Int, 
      run : Int) : String = {
    
    var id =
      algorithm +
      "_" + execution +
      "_" + mode +
      "-" +
      "a" + agents + 
      "m" + meetings + 
      "t" + timeslots + 
      "_" + run
    id
  }
  
}