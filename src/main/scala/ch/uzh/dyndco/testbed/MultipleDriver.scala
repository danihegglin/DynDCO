package ch.uzh.dyndco.testbed;

import ch.uzh.dyndco.problems.MeetingSchedulingFactory
import com.signalcollect.ExecutionConfiguration
import com.signalcollect.configuration.ExecutionMode
import ch.uzh.dyndco.algorithms.maxsum.MaxSum
import com.signalcollect.deployment.DeployableAlgorithm
import akka.actor.ActorRef
import org.clapper.argot._
import ArgotConverters._

object MultipleDriver extends App {
  
  val parser = new ArgotParser("DynDCO: Single")
  
  // parse parameters
  val algorithm = parser.option[String](List("algorithm"), "s","algorithm").toString()
  val execution = parser.option[String](List("execution"), "s","execution").toString()
  val mode = parser.option[String](List("mode"), "s","mode").toString()
  val param = parser.option[String](List("param"), "s","param").toString()
  val timeslots = parser.option[String](List("timeslots"), "s","timeslots").toString()
  val meetings = parser.option[String](List("meetings"), "s","meetings").toString()
  val agents = parser.option[String](List("agents"), "s","agents").toString()
  val runs = parser.option[String](List("runs"), "s","runs").toString()
  val factor = parser.option[String](List("factor"), "s","factor").toString()
  val max = parser.option[String](List("max"), "s","max").toString()
  
  // build map
  var parameters = Map[String, String]()
  
  parameters += ("algorithm" -> algorithm)
  parameters += ("execution" -> execution)
  parameters += ("mode" -> mode)
  parameters += ("param" -> param)
  parameters += ("timeslots" -> timeslots)
  parameters += ("meetings" -> meetings)
  parameters += ("agents" -> agents)
  parameters += ("runs" -> runs)
  parameters += ("factor" -> factor)
  parameters += ("max" -> max)
    
  MultipleTest.execute(parameters,null)
  
  System.exit(0)
	
}
