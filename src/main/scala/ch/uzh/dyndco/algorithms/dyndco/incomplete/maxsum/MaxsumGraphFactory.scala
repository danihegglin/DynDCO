//package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum
//
//import com.signalcollect.Graph
//import scala.util.Random
//import collection.mutable.Map
//import collection.mutable.Set
//import com.signalcollect.GraphBuilder
//import com.signalcollect.StateForwarderEdge
//
//class MaxsumGraphFactory(
//      timeslots : Int,
//      agents : Int,
//      meetings : Int,
//      hardConstraintProb : Double){
//  
//      def create() = {
//        
////        val graph = GraphBuilder.withConsole(true,8091).build
////        
////        var random : Random = new Random
////        
////        // define meeting initiator
//////        val meetingInitiators : Map[Int,Int] = Map[Int,Int]()
//////        for(meeting <- 1 to meetings){
//////           meetingInitiators + (meeting -> random.nextInt(agents))
//////        }
////        
////        // build function vertices
////        val functionVertices : Map[Int, FunctionVertex] = Map[Int, FunctionVertex]()
////        for(meeting <- 1 to meetings){
////          functionVertices + (meeting -> new FunctionVertex("f" + meeting, null, timeslots))
////        }
////        
////        // build variable vertices
////        val variableVertices : Map[VariableVertex, Set[Int]] = Map[VariableVertex, Set[Int]]()
////        for(agent <- 1 to agents){
////          var participationsAmount : Int = random.nextInt(meetings)
////          for(partAmount <- 1 to participationsAmount){
////            var participations : Set[Int] = Set[Int]()
////            participations + random.nextInt(meetings) // Not unique!!!
////            
////            // all timeslots
////            var availableTimeslots = List[Int](timeslots)
////            
////            // assign preferences
////            var preference : Set[Int] = Set[Int]()
////            for(participation <- participations){
////              var timeslot = random.nextInt(availableTimeslots.size)
////              preference + availableTimeslots.apply(timeslot)
////              availableTimeslots.drop(timeslot)
////            }
////            
////            // assign hard constraints
////            var available = random.nextInt(availableTimeslots.size)
////            var numOfHardConstraints : Int = available / 5 // FIXME
////            var hard: Set[Int] = Set()
////            for(hardConstraint <- 1 to numOfHardConstraints){
////              var timeslot = random.nextInt(availableTimeslots.size)
////              hard + availableTimeslots.apply(timeslot)
////              availableTimeslots.drop(timeslot)
////            }
////            
////            // assign soft constraints to the rest
////            var soft: Set[Int] = Set()
////            for(softConstraint <- availableTimeslots){
////              soft + availableTimeslots.apply(softConstraint)
////            }
////            
////            // build constraints
////            var constraints = new Proposal(agent,hard,soft,preference)
////            
////            new VariableVertex(agent,constraints,timeslots)
////          }
////        }
////        
////        // build edges
////        for(variableVertex <- variableVertices.keys){
////          for(target : Int <- variableVertices.apply(variableVertex)){
////            graph.addEdge(variableVertex.id, new StateForwarderEdge(target))
////          }
////        }
////        for(functionVertex <- functionVertices.keys){
////          for(variableVertex <- variableVertices.keys){
////            var participations : Set[Int] = Set[Int]()
////            if(participations.contains(variableVertex.id.asInstanceOf[Int])){
////              graph.addEdge(functionVertex, new StateForwarderEdge(variableVertex.id))
////            }
////          }
////        }
////        
////        graph
////        
////      }
//
//}