package ch.uzh.dyndco.algorithms.dco.complete.dpop

class DPOPMessage (_item : Double, _messageType : String) {
  
	val item = _item
	val messageType = _messageType
  
	def getMessageType : String = messageType
	def getItem : Double = item
	
}