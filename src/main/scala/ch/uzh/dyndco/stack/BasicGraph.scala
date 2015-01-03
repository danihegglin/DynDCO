package ch.uzh.dyndco.stack

import com.signalcollect.Graph
import ch.uzh.dyndco.problems.Problem

trait BasicGraph {
  
    def graph : Graph[Any,Any]
    def getFactory() : GraphFactory[DynamicGraph, Problem]
    def show()

}