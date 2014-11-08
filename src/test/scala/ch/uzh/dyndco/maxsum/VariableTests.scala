package ch.uzh.dyndco.maxsum

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

@RunWith(classOf[JUnitRunner])
class VariableTests extends FunSuite with BeforeAndAfter {

  var test : Int 
  
  before {
    test = 1 + 1
  }
  
  test("Build prefBuild") {
    assert(test == 2)
  }
  
}
