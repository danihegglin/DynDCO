package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map

class DPOPMessage(
    _sender : DPOPVertex, 
    _utilities : Map[Int,Map[Int,Double]], 
    _values : Map[Int,Int]
    ) {
  
  var sender : DPOPVertex = _sender
	var utilities = _utilities
  var values = _values
  
	def getValues : Map[Int,Int] = values
	def getUtilities : Map[Int,Map[Int,Double]] = utilities
	
}