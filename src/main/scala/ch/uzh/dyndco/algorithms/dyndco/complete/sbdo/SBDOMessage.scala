package ch.uzh.dyndco.algorithms.dyndco.complete.sbdo

import com.signalcollect._
import scala.util.Random
import com.signalcollect.configuration.ExecutionMode

class SBDOMessage (_messageType : String, _content : Object){
  
	val messageType = _messageType
	val content = _content
	
	def getMessageType : String = messageType
	def getContent : Object = content
  
}