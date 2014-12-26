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
    messages += (""+124 -> 8.0)
    
    val immutable : scala.collection.immutable.Map[String,Double] = messages.toMap 
    val json = immutable.toJson
   
   var svc = url("http://" + address + ":9000/start?id=" + id)
   var result = Http(svc OK as.String)    
   for(i : Int <- 1 to 250){
     Thread sleep 50
     println(i)
     println(json.toString())
     
     var svc = url("http://" + address + ":9000/utility/agent/" + vertexId + "?id=" + id)
//       .POST
       .setBody(json.toString())
//       .addHeader("Content-Type", "application/json")
//       .setBodyEncoding("UTF-8")
       val postFields: Map[String, String] = Map[String,String]()
     var result = Http(svc << postFields OK as.String)
//     
//        println(result)
     
//     val SERVICES_URL = "http://" + address + ":9000/utility/agent/" + vertexId + "?id=" + id
//    val postFields: Map[String, String] = Map[String,String]()
////    postFields += "Content-Type" -> "application/json"
//    val request = url(SERVICES_URL) << postFields OK as.String
//    val post = Http(request)
     
   }
   svc = url("http://" + address + ":9000/stop?id=" + id)
   result = Http(svc OK as.String)
   
   System.exit(0)
}