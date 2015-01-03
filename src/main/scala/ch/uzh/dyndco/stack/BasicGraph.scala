package ch.uzh.dyndco.stack

import collection.mutable.Map
import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Problem
import com.signalcollect.ExecutionInformation

trait BasicGraph {
  
    def graph : Graph[Any,Any]
    def getFactory() : GraphFactory[DynamicGraph, Problem]
    def show()
    
    def prepareStats(stats : ExecutionInformation[Any,Any]) : Map[String, String] = {
      
      var s = stats.aggregatedWorkerStatistics
      var e = stats.executionStatistics
      
      var scInfo = Map[String,String]()
      
      // Add infos
      scInfo += "signalOperationsExecuted" -> s.signalOperationsExecuted.toString()
      scInfo += "signalMessagesReceived" -> s.signalMessagesReceived.toString()
      scInfo += "collectOperationsExecuted" -> s.collectOperationsExecuted.toString()
      scInfo += "continueMessagesReceived" -> s.continueMessagesReceived.toString()
      scInfo += "bulkSignalMessagesReceived" -> s.bulkSignalMessagesReceived.toString()
      scInfo += "verticesAdded" -> s.verticesAdded.toString()
      scInfo += "verticesRemoved" -> s.verticesRemoved.toString()
      scInfo += "outgoingEdgesAdded" -> s.outgoingEdgesAdded.toString()
      scInfo += "outgoingEdgesRemoved" -> s.outgoingEdgesRemoved.toString()
      scInfo += "toCollectSize" -> s.toCollectSize.toString()
      scInfo += "toSignalSize" -> s.toSignalSize.toString()
      scInfo += "requestMessagesReceived" -> s.requestMessagesReceived.toString()
      scInfo += "otherMessagesReceived" -> s.otherMessagesReceived.toString()
      scInfo += "collectSteps" -> e.collectSteps.toString()
      scInfo += "signalSteps" -> e.signalSteps.toString()
      scInfo += "computationTime" -> e.computationTime.toString()
      scInfo += "jvmCpuTime" -> e.jvmCpuTime.toString()
      scInfo += "totalExecutionTime" -> e.totalExecutionTime.toString()
      scInfo += "terminationReason" -> e.terminationReason.toString()
      
      scInfo
    }  

}