package ch.uzh.dyndco.studies

import dispatch._
import dispatch.Defaults._

object MonitoringTesting extends App {
  
    final var address = "178.62.200.138"
    final var vertexId = 1
    final var agentUtility = 1
    final var id = 1
   
   var svc = url("http://" + address + ":9000/start?id=" + id)
   var result = Http(svc OK as.String)    
   for(i : Int <- 1 to 100000){
     Thread sleep 50
     println(i)
     val svc = url("http://" + address + ":9000/utility/agent/" + vertexId + "?utility=" + agentUtility + "&id=" + id)
     val result = Http(svc OK as.String)
   }
   svc = url("http://" + address + ":9000/stop?id=" + id)
   result = Http(svc OK as.String)    
}