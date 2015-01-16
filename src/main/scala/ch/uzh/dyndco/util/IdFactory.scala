package ch.uzh.dyndco.util

object IdFactory {

  def build(
      algorithm : String, 
      execution : String, 
      mode : String, 
      param : String,
      density : Double,
      timeslots : Int, 
      meetings : Int, 
      agents : Int, 
      run : Int) : String = {
    
    // param fix
    var paramFix = param.replaceAll("[.]", "-")
    paramFix = paramFix.replaceAll(",", "-")
    println(paramFix)
    
    var id =
      algorithm +
      "_" + execution +
      "_" + mode +
      "_" + paramFix +
      "_" + density +
      "-" +
      "a" + agents + 
      "m" + meetings + 
      "t" + timeslots + 
      "_" + run
      
      println(id)
      
    id
  }
  
}