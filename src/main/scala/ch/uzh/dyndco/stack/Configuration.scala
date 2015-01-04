package ch.uzh.dyndco.stack

import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode

class Configuration(
    execConfig_ : ExecutionConfiguration[Any, Any], 
    testMode_ : TestMode.Value, 
    param_ : Array[String]) {
  
  val execConfig = execConfig_
  val testMode = testMode_
  val parameters = param_
  
}