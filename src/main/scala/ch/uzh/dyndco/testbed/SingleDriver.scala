package ch.uzh.dyndco.testbed

import org.clapper.argot._
import ArgotConverters._

object SingleDriver extends App {

	// parse parameters
  val parser = new ArgotParser("DynDCO: Single")
	val algorithm = parser.option[String](List("algorithm"), "s","algorithm")
	val execution = parser.option[String](List("execution"), "s","execution")
	val mode = parser.option[String](List("mode"), "s","mode")
	val param = parser.option[String](List("param"), "s","param")
	val timeslots = parser.option[String](List("timeslots"), "s","timeslots")
	val meetings = parser.option[String](List("meetings"), "s","meetings")
	val agents = parser.option[String](List("agents"), "s","agents")
	parser.parse(args)

	// build map
  var parameters = Map[String, String]()
  try {
  	parameters += ("algorithm" -> algorithm.value.get)
  	parameters += ("execution" -> execution.value.get)
  	parameters += ("mode" -> mode.value.get)
  	parameters += ("param" -> param.value.get)
  	parameters += ("timeslots" -> timeslots.value.get)
  	parameters += ("meetings" -> meetings.value.get)
  	parameters += ("agents" -> agents.value.get)
  } 
  catch {
    case e: Exception  => println("Too few arguments")
  }
  
  SingleTest.execute(parameters, null)

	System.exit(0)

}