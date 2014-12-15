package ch.uzh.dyndco.algorithms.dpop;

import scala.collection.mutable.Map

class DPOPMessage(
    _sender : Any, 
    _item : Double, 
    _map : Map[Int,Double], 
    _messageType : String) {
  
  var sender : Any = _sender
	var item : Double = _item
	var map : scala.collection.mutable.Map[Int,Double] = _map
	var messageType : String = _messageType
  
//	def this(_sender : Any, _item : Double, _messageType : String){
//	  this(_sender, _item, null, _messageType);
//	  sender = _sender
//		item = _item
//		messageType = _messageType
//	}
//	
//	def this(sender_ : Any, _map : Map[Int,Double], _messageType : String){
//	  this(sender_, 0, _map, _messageType);
//		map = _map
//		messageType = _messageType
//	}
  
	def getMessageType : String = messageType
	def getItem : Double = item
	def getUtilValueMap : Map[Int,Double] = map
	
}