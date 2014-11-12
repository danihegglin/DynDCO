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
//  val graph = MaxsumGraphFactory.create()
  
	// initialize bipartite graph
//  val graph = GraphBuilder.withConsole(true,8091).build
//  println("Graph initialized")
//  
//  // constraints FIXME generate automatically
//  var hard1 : Set[Int] = Set(1,2,3)
//  var soft1 : Set[Int] = Set(5)
//  var preference1 : Set[Int] = Set(4)
//  var constraints1 = new Proposal(1,hard1,soft1,preference1)
//  var hard2: Set[Int] = Set(1)
//  var soft2: Set[Int] = Set(2,3,4)
//  var preference2 : Set[Int] = Set(5)
//  var constraints2 = new Proposal(2,hard2,soft2,preference2)
//  var hard3: Set[Int] = Set()
//  var soft3: Set[Int] = Set(2,3,5)
//  var preference3 : Set[Int] = Set(4)
//  var constraints3 = new Proposal(3,hard3,soft3,preference3)
//  println("Build constraints")
//  
//  // vertices FIXME generate automatically
//  var variable1 = new VariableVertex(1,constraints1,timeslots)
//  var variable2 = new VariableVertex(2,constraints2,timeslots)
//  var variable3 = new VariableVertex(3,constraints3,timeslots)
//  graph.addVertex(variable1)
//  graph.addVertex(variable2)
//  graph.addVertex(variable3)
//  graph.addVertex(new FunctionVertex(4,null,timeslots))
//  println("Build vertices")
//  
//  // edges FIXME generate automatically
//  graph.addEdge(1, new StateForwarderEdge(4))
//  graph.addEdge(2, new StateForwarderEdge(4))
//  graph.addEdge(3, new StateForwarderEdge(4))
//  
//  graph.addEdge(4, new StateForwarderEdge(1))
//  graph.addEdge(4, new StateForwarderEdge(2))
//  graph.addEdge(4, new StateForwarderEdge(3))
//  println("Build edges")
  
  val graph = GraphBuilder.withConsole(true,8091).build
        
        var random : Random = new Random
        
        // define meeting initiator
//        val meetingInitiators : Map[Int,Int] = Map[Int,Int]()
//        for(meeting <- 1 to meetings){
//           meetingInitiators + (meeting -> random.nextInt(agents))
//        }
        
        // build function vertices
        val functionVertices : Map[Int, FunctionVertex] = Map[Int, FunctionVertex]()
        for(meeting <- 1 to meetings){
          functionVertices + (meeting -> new FunctionVertex("f" + meeting, null, timeslots))
        }
        
        // build variable vertices
        val variableVertices : Map[VariableVertex, Set[Int]] = Map[VariableVertex, Set[Int]]()
        for(agent <- 1 to agents){
          var participationsAmount : Int = random.nextInt(meetings)
          for(partAmount <- 1 to participationsAmount){
            var participations : Set[Int] = Set[Int]()
            participations + random.nextInt(meetings) // Not unique!!!
            
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
              soft + availableTimeslots.apply(softConstraint)
            }
            
            // build constraints
            var constraints = new Proposal(agent,hard,soft,preference)
            
            new VariableVertex(agent,constraints,timeslots)
          }
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
