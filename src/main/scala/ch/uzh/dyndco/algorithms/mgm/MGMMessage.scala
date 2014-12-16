package ch.uzh.dyndco.algorithms.mgm

class MGMMessage(sender_ : Any, value_ : Int, gain_ : Double) {
  
  var sender : Any = sender_
  var value : Int = value_
  var gain : Double = gain_
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