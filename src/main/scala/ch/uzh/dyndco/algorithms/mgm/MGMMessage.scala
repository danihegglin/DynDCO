package ch.uzh.dyndco.algorithms.mgm

class MGMMessage(sender : Any, value : Int, gain : Double) {
  
  var sender : Any = null
  var value : Int = -1
  var gain : Double = 0
  var messageType : String = ""
  
  def this(sender : Any, value : Int) = {
    this(sender, value, 0)
    messageType = "value"
  }
  
  def this(sender : Any, gain : Double) = {
    this(sender, -1, gain)
    messageType = "gain"
  }
  
  def getType() : String = {
    messageType
  }

}