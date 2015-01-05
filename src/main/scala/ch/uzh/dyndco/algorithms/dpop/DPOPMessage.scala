package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map

class DPOPMessage(
    _sender : DPOPVertex, 
    _utilities : Map[Int,Double], 
//  _value : Map[Int,Int]
    _value : Int
    ) {
  
  var sender : DPOPVertex = _sender
	var utilities = _utilities
  var value = _value
  
//	def getValue : Map[Int,Int] = value
  def getValue : Int = value
	def getUtilities : Map[Int,Double] = utilities
	
}