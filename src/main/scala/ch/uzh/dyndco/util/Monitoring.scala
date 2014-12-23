package ch.uzh.dyndco.util

import dispatch._
import dispatch.Defaults._

object Monitoring {
  
  /**
   * Configuration
   */
  final var address = "178.62.200.138"
//  final var address = "localhost"
  
  /**
   * Main
   */
  def start(id : String) = {
    var svc = url("http://" + address + ":9000/start?id=" + id)
    var result = Http(svc OK as.String)    
  }
  
  def stop(id : String) = {
    val hello = new Thread(new Runnable {
      def run() {
         Thread sleep 60000
         var svc = url("http://" + address + ":9000/stop?id=" + id)
         var result = Http(svc OK as.String)
      }
    })    
  }

  def update(vertexId : Any, agentUtility: Double, id : String) = {
    val svc = url("http://" + address + ":9000/utility/agent/" + vertexId + "?utility=" + agentUtility + "&id=" + id)
    val result = Http(svc OK as.String)
  }
  
  def sucess(id : String) = {
    val svc = url("http://" + address + ":9000/success?id=" + id)
    val result = Http(svc OK as.String)
  }
  
}