//package ch.uzh.dyndco.algorithms.dco.complete.dpop
//
//class DPOPMessage(_item : Double, _map : scala.collection.mutable.Map[Int,Double], _messageType : String) {
//  
//	var item : Double
//	var map : scala.collection.mutable.Map[Int,Double] = scala.collection.mutable.Map()
//	var messageType : String
//  
//	def this(_item : Double, _messageType : String){
//	  this(_item, null, _messageType);
//		item = _item
//		messageType = _messageType
//	}
//	
//	def this(_map : scala.collection.mutable.Map[Int,Double], _messageType : String){
//	  this(0, _map, _messageType);
//		map = _map
//		messageType = _messageType
//	}
//  
//	def getMessageType : String = messageType
//	def getItem : Double = item
//	def getUtilValueMap : scala.collection.mutable.Map[Int,Double] = map
//	
//}