package ch.uzh.dyndco.util

import dispatch._
import dispatch.Defaults._

object Monitoring {
  
  def start() = {
    var svc = url("http://localhost:9000/start")
    var result = Http(svc OK as.String)    
  }
  
  def stop() = {
    var svc = url("http://localhost:9000/stop")
    var result = Http(svc OK as.String)    
  }

}