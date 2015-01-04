package ch.uzh.dyndco.stack

import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode

object ConfigurationFactory {
  
  def build(EXECUTION : String, MODE : String, PARAM : String) : Configuration = {
    
    var execConfig = buildConfig(EXECUTION)
    var testMode = buildMode(MODE)
    var param = buildParam(PARAM)
    
    new Configuration(execConfig, testMode, param)
    
  }
  
  private def buildConfig(EXECUTION : String) : ExecutionConfiguration[Any, Any] = {
      EXECUTION match {
        case "asynchronous" => ExecutionConfiguration.withExecutionMode(ExecutionMode.OptimizedAsynchronous)
        case "synchronous" => ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
      }
  }
  
  private def buildMode(MODE : String) : TestMode.Value = {
      MODE match {
        case "normal" => TestMode.Normal
        case "dynamicConstraints" => TestMode.DynamicConstraints
        case "dynamicMeetings" => TestMode.DynamicMeetings
      }
  }

  private def buildParam(PARAM : String) : Array[String] = {
    PARAM.split(",")
  }

}