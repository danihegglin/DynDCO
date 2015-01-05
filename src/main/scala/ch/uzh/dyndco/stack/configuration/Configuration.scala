package ch.uzh.dyndco.stack.configuration

import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.stack.tests.TestMode

class Configuration(
    execConfig_ : ExecutionConfiguration[Any, Any], 
    testMode_ : TestMode.Value, 
    param_ : Array[String]) {
  
  val execConfig = execConfig_
  val testMode = testMode_
  val parameters = param_
  
}