package ch.uzh.dyndco.monitoring

import dispatch._
import dispatch.Defaults._

object ReliabilityTest extends App {
  
    final var address = "178.62.200.138"
    final var vertexId = 1
    final var agentUtility = 1
    final var id = 1
   
   for(i : Int <- 1 to 1000000){
     val svc = url("http://" + address + ":9000/utility/agent/" + vertexId + "?utility=" + agentUtility + "&id=" + id)
     val result = Http(svc OK as.String)
   }
}