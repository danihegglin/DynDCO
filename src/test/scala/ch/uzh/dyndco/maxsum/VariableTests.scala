//package ch.uzh.dyndco.maxsum
//
//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
//import org.scalatest.FunSuite
//import org.scalatest.BeforeAndAfter
//import ch.uzh.dyndco.algorithms.maxsum.VariableVertex
//import ch.uzh.dyndco.algorithms.maxsum.Proposal
//import collection.mutable.Map
//import collection.mutable.Set
//
//@RunWith(classOf[JUnitRunner])
//class VariableTests extends FunSuite with BeforeAndAfter {
//  
//  /**
//   * Test Case: 3 Variables, 3 Functions, 3 Timeslots -> 3 Agents, 2 Meetings, 3 Timeslots
//   */
//  
//  /**
//   * Prepare Values
//   * 
//   * Initial Assignments
//   * -----------------------------------------------
//   * Timeslots:		|	1	|	2	|	3	|
//   * -----------------------------------------------
//   * Variable 1:	|	x	|	1	|	-	|
//   * Variable 2:	|	x	|	2	|	1	|
//   * Variable 3:	|	-	|	x	|	2	|
//   * -----------------------------------------------
//   * 1-9: Meeting, x: Hard Constraint, -: Soft Constraint
//   */
//  var timeslots : Int = 3
//  
//  /**
//   * Build variable2 vertex to test
//   */
//  var initialState = new Proposal(1,Set(1),Set(),Map[Int,Int](1 -> 3, 2 -> 2))
//  var variable2 = new VariableVertex(2,initialState,1)
//   
//  /**
//   * Cost Assignment
//   * ----------------------------------------------- 
//   * Function 1: 	Variable 1:		Timeslot 1:	2
//   * 								Timeslot 2:	2
//   *         						Timeslot 3:	0
//   * 				Variable 2:		Timeslot 1:	2
//   *     							Timeslot 2:	0
//   *      							Timeslot 3:	1
//   * -----------------------------------------------
//   * Function 2:	Variable 2:		Timeslot 1:	1
//   * 								Timeslot 2:	2
//   *     							Timeslot 3:	0
//   * 				Variable 3:		Timeslot 1:	2
//   *     							Timeslot 2:	0
//   *            					Timeslot 3:	2
//   * -----------------------------------------------
//   * Function 3:	Variable 2:		Timeslot 1:	1
//   * 								Timeslot 2:	2
//   *         						Timeslot 3:	0
//   * 				Variable 3:		Timeslot 1:	2
//   *     							Timeslot 2:	0
//   *            					Timeslot 3:	2
//   * -----------------------------------------------
//   */
//  
//  // allAssignmentCosts: function -> variable -> assignment -> cost
//  var allMarginalUtilities : Map[Any, Map[Any, Map[Int, Double]]] = Map[Any, Map[Any, Map[Int, Double]]]()
//  var f1v1 : Map[Int,Double] = Map(1 -> 2.0, 2 -> 2.0, 3 -> 0.0)
//  var f1v2 : Map[Int,Double] = Map(1 -> 2.0, 2 -> 0.0, 3 -> 1.0)
//  var f2v1 : Map[Int,Double] = Map(1 -> 2.0, 2 -> 2.0, 3 -> 2.0)
//  var f2v2 : Map[Int,Double] = Map(1 -> 1.0, 2 -> 2.0, 3 -> 0.0)
//  var f2v3 : Map[Int,Double] = Map(1 -> 2.0, 2 -> 0.0, 3 -> 2.0)
//  var f1 : Map[Any, Map[Int, Double]] = Map(1 -> f1v1, 2 -> f1v2)
//  var f2 : Map[Any, Map[Int, Double]] = Map(1 -> f2v1, 2 -> f2v2, 3 -> f2v3)
//  var f3 : Map[Any, Map[Int, Double]] = Map(2 -> f2v2, 3 -> f2v3)
//  allMarginalUtilities += (1 -> f1, 2 -> f2, 3 -> f3)
//  
//  /**
//   * Run Tests
//   */
//  var result = Map[Any,Map[Int, Double]]()
//  var result2 = Map[Int,Int]()
//  
//  before {
//    println(allMarginalUtilities)
//    result = variable2.buildMarginalUtilities(allMarginalUtilities, timeslots)
//    result2 = variable2.findBestValueAssignment(result)
//  }
//  
//  /**
//   * Results for variable2
//   * ---------------------------------
//   * function 1	->	Variable 1: 2,0,1
//   * ---------------------------------
//   * local		->	Variable 2:	2,2,0
//   * 			->	Solution queue: 3,2,1 // FIXME need to check the other solution queue
//   * --------------------------------
//   * function 2	->	Variable 3: 1,2,0
//   * --------------------------------
//   * local		->	Variable 2: 2,0,2
//   * 			->	Solution queue: 2/3,1 // FIXME need to check the other solution queue
//   * ---------------------------------
//   * Best assessment: f1 -> 3, f2 -> 2
//   * 
//   * Results for variable1
//   * ---------------------------------
//   * function 1	->	Variable 1: 2,2,0
//   * local		->	Variable 1:	2,0,1 // FIXME is this needed?
//   * 			->	Solution queue: 3,2,1
//   * ---------------------------------
//   * Best assessment: f1 -> 3
//   *
//   * Results for variable3
//   * ---------------------------------
//   * function 2	->	Variable 3: 2,0,2
//   * local		->	Variable 3:	1,2,0 // FIXME is this needed?
//   * 			->	Solution queue: 2/3,1
//   * ---------------------------------
//   * Best assessment: f2 -> 2
//   */
//  test("Testing result of buildMarginalUtilities"){
//    var conclusion1 : Int = result2.get(1).getOrElse(fail)
//    var conclusion2 : Int = result2.get(2).getOrElse(fail)
//    
//    assert(conclusion1 == 2)
//    assert(conclusion2 == 3)
//  }
//  
//  after {
//    println("meeting 1: " + result2.get(1).getOrElse(fail))
//    println("meeting 2: " + result2.get(2).getOrElse(fail))
//  }
//  
//}
