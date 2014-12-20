package ch.uzh.dyndco.util

import dispatch._
import dispatch.Defaults._

object Monitoring {
  
  /**
   * Configuration
   */
//  final var address = "178.62.200.138"
  final var address = "localhost"
  
  def start() = {
    var svc = url("http://" + address + ":9000/start")
    var result = Http(svc OK as.String)    
  }
  
  def stop() = {
    var svc = url("http://" + address + ":9000/stop")
    var result = Http(svc OK as.String)    
  }

  def update(id : Any, agentUtility: Double) = {
    val svc = url("http://" + address + ":9000/utility/agent/" + id + "?utility=" + agentUtility)
    val result = Http(svc OK as.String)
  }
  
  def sucess() = {
    val svc = url("http://" + address + ":9000/success")
    val result = Http(svc OK as.String)
  }

}