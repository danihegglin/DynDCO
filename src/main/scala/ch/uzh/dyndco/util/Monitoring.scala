package ch.uzh.dyndco.util

import dispatch._
import dispatch.Defaults._
import collection.mutable.Map
import spray.json._
import DefaultJsonProtocol._

object Monitoring {
  
  /**
   * Configuration
   */
  final var address = "178.62.200.138"
//  final var address = "localhost"
  var runID : String = "" 
  
  /**
   * Main
   */
  def start(runID : String) = {
    this.runID = runID
    var svc = url("http://" + address + ":9000/start?id=" + runID)
    var result = Http(svc OK as.String)    
  }
  
  def stop(runID : String) = {
    val hello = new Thread(new Runnable {
      def run() {
         Thread sleep 60000
         var svc = url("http://" + address + ":9000/stop?id=" + runID)
         var result = Http(svc OK as.String)
      }
    })    
  }

  def update(vertexId : Any, messages : Map[String, Double]) = {
    
    val immutable : scala.collection.immutable.Map[String,Double] = messages.toMap 
    val json = immutable.toJson
    
    Http(url("http://" + address + ":9000/utility/agent/" + vertexId + "?id=" + runID).setBody(
     "tracker={" + json.toString() + "}").setHeader(
     "Content-Type", "application/json") OK as.String)
    
    //    val svc = url("http://" + address + ":9000/utility/agent/" + vertexId + "?id=" + id)
//    val result = Http(svc OK as.String)
  }
  
  def sucess(id : String) = {
    val svc = url("http://" + address + ":9000/success?id=" + id)
    val result = Http(svc OK as.String)
  }
  
}