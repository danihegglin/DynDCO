package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum;

import com.signalcollect._
import com.signalcollect.configuration.ExecutionMode
import org.slf4j.LoggerFactory
import collection.mutable.Set
import scala.util.Random

object MaxSum extends App {

	/**
	 * Logger
	 */
	//   val log = Logger(LoggerFactory.getLogger("name"))

	/**
	 * Configuration
	 */
	var timeslots : Int = 28
			var agents : Int = 100
			var meetings : Int = 30
			var hardConstraintProb : Double = 0.2

			/**
			 * Produce the graph
			 */
			val graph = GraphBuilder.withConsole(true,8091).build

			var random : Random = new Random

			// build function vertices
			var functionVertices : Map[Int, FunctionVertex] = Map[Int, FunctionVertex]()
			for(meeting <- 1 to meetings){
			  var functVertex = new FunctionVertex("f" + meeting, null, timeslots)
			  println("function vertex: " + functVertex.id)
				functionVertices += (meeting -> functVertex)
				graph.addVertex(functVertex)
			}

			// build variable vertices
			var variableVertices : Set[VariableVertex] = Set[VariableVertex]()
			for(agent <- 1 to agents){
			  
			  println("----------------" + agent + "-----------------------")

			      println("building participations")
						var participationsAmount : Int = random.nextInt(meetings) + 1
						println("possible participations: " + participationsAmount)
								var participations : Set[Int] = Set[Int]()
								for(partAmount <- 1 to participationsAmount){
								    var done : Boolean = false
								    while(done == false){						  
								      var participation = random.nextInt(meetings) + 1
								      println("participation: " + participation)
								      if(!participations.contains(participation)){
									      participations += participation
									      done = true
								      }
								   }
								}

			  		println("building available timeslots")
						// all timeslots
						val availableTimeslots = List[Int](1,2,3,4,5)
//						for(timeslot <- 1 to timeslots){
//						  availableTimeslots + timeslot
//						}

						println("preferences")
						// assign preferences
								var preference : Set[Int] = Set()
								for(participation <- participations){
								  println("preference " + participation)
									var timeslot = random.nextInt(availableTimeslots.size)
											preference += availableTimeslots.apply(timeslot)
											availableTimeslots.drop(timeslot)
								}

			  	  println("building hard constraints")
						// assign hard constraints
						var available = random.nextInt(availableTimeslots.size) + 1
								var numOfHardConstraints : Int = available / 3 // FIXME
								var hard: Set[Int] = Set()
								for(hardConstraint <- 1 to numOfHardConstraints){
									var timeslot = random.nextInt(availableTimeslots.size) + 1
											hard += availableTimeslots.apply(timeslot -1)
											availableTimeslots.drop(timeslot)
								}

			  	  println("building softConstraint")
						// assign soft constraints to the rest
						var soft: Set[Int] = Set()
						for(softConstraint <- availableTimeslots){
						  if(!softConstraint.isNaN()){
						    soft + availableTimeslots.apply(softConstraint -1)
						  }
						}

						println("building proposal")
					  // build constraints
				   var constraints = new Proposal(agent,hard,soft,preference)

					  println("adding variablevertex")
					  var varVertex = new VariableVertex(agent,constraints,timeslots)
						graph.addVertex(varVertex)
						
								      println("participations count: " + participations.size)
						for(target : Int <- participations){
						  println("-----------------------------------------")
				     println("adding edge variable -> function")
				     
				     var functionId = "f" + target
				     var stateforwarder = new StateForwarderEdge(functionId);
						 var variableId = varVertex.id
						 
						 println("functionId:" + functionId)
						 println("variableId:" + variableId)
						 
				     graph.addEdge(variableId, stateforwarder)
				     var functVertex = functionVertices.apply(target)
				     graph.addEdge(functionId, new StateForwarderEdge(agent))
				          }
						
						variableVertices += varVertex
				        }
						
				  // start
				  println("Start graph")
				  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
				  val stats = graph.execute(execConfig)
					
					// show run info
					println(stats)
					graph.foreachVertex(println(_))
					
					// show results
					println("variables: " + variableVertices.size)
					for(variableVertex <- variableVertices){
					  println("----------" + variableVertex.id + "---------------")
					  variableVertex.show()
					}

				 // shutdown graph
				 graph.shutdown

}
