package ch.uzh.dyndco.studies

import dispatch._
import dispatch.Defaults._
import spray.json._
import DefaultJsonProtocol._
import collection.mutable.Map

object MonitoringTesting extends App {
  
//    final var address = "178.62.200.138"
    final var address = "localhost"
    final var vertexId = 1
    final var agentUtility = 1
    final var id = 1
    
    var messages = Map[String,Double]()
    messages += (""+123 -> 5.0)
    
    val immutable : scala.collection.immutable.Map[String,Double] = messages.toMap 
    val json = immutable.toJson
   
   var svc = url("http://" + address + ":9000/start?id=" + id)
   var result = Http(svc OK as.String)    
   for(i : Int <- 1 to 100000){
     Thread sleep 50
     println(i)
         Http(url("http://" + address + ":9000/utility/agent/" + vertexId + "?id=" + id).setBody(
     "tracker={" + json.toString() + "}").setHeader(
     "Content-Type", "application/json") OK as.String)
   }
   svc = url("http://" + address + ":9000/stop?id=" + id)
   result = Http(svc OK as.String)    
}