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
	var timeslots : Int = 5
			var agents : Int = 5
			var meetings : Int = 3
			var hardConstraintProb : Double = 0.2

			/**
			 * Produce the graph
			 */
			val graph = GraphBuilder.withConsole(true,8091).build

			var random : Random = new Random

			// build function vertices
			val functionVertices : Map[Int, FunctionVertex] = Map[Int, FunctionVertex]()
			for(meeting <- 1 to meetings){
				functionVertices + (meeting -> new FunctionVertex("f" + meeting, null, timeslots))
			}

			// build variable vertices
			val variableVertices : Map[VariableVertex, Set[Int]] = Map[VariableVertex, Set[Int]]()
			for(agent <- 1 to agents){

						var participationsAmount : Int = random.nextInt(meetings)
								var participations : Set[Int] = Set[Int]()
								for(partAmount <- 1 to participationsAmount){
									participations + random.nextInt(meetings) // Not unique!!!
								}

						// all timeslots
						var availableTimeslots = List[Int](timeslots)

								// assign preferences
								var preference : Set[Int] = Set[Int]()
								for(participation <- participations){
									var timeslot = random.nextInt(availableTimeslots.size)
											preference + availableTimeslots.apply(timeslot)
											availableTimeslots.drop(timeslot)
								}

						// assign hard constraints
						var available = random.nextInt(availableTimeslots.size)
								var numOfHardConstraints : Int = available / 5 // FIXME
								var hard: Set[Int] = Set()
								for(hardConstraint <- 1 to numOfHardConstraints){
									var timeslot = random.nextInt(availableTimeslots.size)
											hard + availableTimeslots.apply(timeslot)
											availableTimeslots.drop(timeslot)
								}

						// assign soft constraints to the rest
						var soft: Set[Int] = Set()
						for(softConstraint <- availableTimeslots){
//						soft + availableTimeslots.apply(softConstraint)
						}

					  // build constraints
				   var constraints = new Proposal(agent,hard,soft,preference)

				   variableVertices + (
				       new VariableVertex(agent,constraints,timeslots) -> preference)
					
			  }

				// build edges
			  for(variableVertex <- variableVertices.keys){
				   for(target : Int <- variableVertices.apply(variableVertex)){
				            graph.addEdge(variableVertex.id, new StateForwarderEdge(target))
				          }
				        }
				        for(functionVertex <- functionVertices.keys){
				          for(variableVertex <- variableVertices.keys){
				            var participations : Set[Int] = Set[Int]()
				            if(participations.contains(variableVertex.id.asInstanceOf[Int])){
				              graph.addEdge(functionVertex, new StateForwarderEdge(variableVertex.id))
				            }
				          }
				        }
				  
				  // start
				  println("Start graph")
				  val execConfig = ExecutionConfiguration.withExecutionMode(ExecutionMode.Synchronous)
				  val stats = graph.execute(execConfig)
					
					// show run info
					println(stats)
					graph.foreachVertex(println(_))
					
					for(variableVertex <- variableVertices.keys){
					  variableVertex.show()
					}

				 // shutdown graph
				 graph.shutdown

}
