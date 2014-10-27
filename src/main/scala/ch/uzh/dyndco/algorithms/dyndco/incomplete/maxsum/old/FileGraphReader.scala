//package ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.old
//
///*
// *  @author Genc Mazlami, with contributions by Robin Hafen
// *
// *  Copyright 2013 University of Zurich
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// *
// */
//
//import scala.collection.mutable.LinkedHashSet
//import java.io.FileNotFoundException
//import scala.collection.mutable.HashMap
//import scala.collection.mutable.ArrayBuffer
//
//class FileGraphReader {
//
//  def readToList(fileName: String): List[SimpleVertex] = {
//    val tuple = fromEdgeList(fileName)
//    toSimpleGraph(tuple._1, tuple._2)
//  }
//
//  def readToMap(fileName: String): HashMap[Int, SimpleVertex] = {
//    val map: HashMap[Int, SimpleVertex] = new HashMap()
//    val list = readToList(fileName)
//    list.foreach {
//      element =>
//        map += (element.id -> element)
//    }
//    map
//  }
//
//  def readFromAdoptFileToList(fileName : String) = {
//    val reader = new AdoptFileReader(fileName)
//    reader.read()
//    val tuple = reader.getGraphTuple
//    toSimpleGraph(tuple._1,tuple._2)
//  }
//  
//  def readFromAdoptFileToMap(fileName : String) = {
//	val map: HashMap[Int, SimpleVertex] = new HashMap()
//	val list = readFromAdoptFileToList(fileName)
//	 list.foreach {
//      element =>
//        map += (element.id -> element)
//    }
//    map
//  }
//  
//  private def toSimpleGraph(set: LinkedHashSet[Set[Int]], vertices: scala.collection.mutable.Set[Int]) = {
//    var simpleGraph = List[SimpleVertex]()
//    vertices.foreach(v => simpleGraph = simpleGraph :+ new SimpleVertex(v, findNeighbors(v, set)))
//    simpleGraph
//  }
//
//  private def findNeighbors(vertexId: Int, set: LinkedHashSet[Set[Int]]) = {
//    var neighborSet = Set[Int]()
//    set.foreach(
//      s =>
//        if (s.contains(vertexId)) {
//          s.foreach(
//            i =>
//              if (i != vertexId) {
//                neighborSet += i
//              })
//        })
//    neighborSet
//  }
//
////  //store neighboring structure of the graph to make it globally accessible, especially when needed during the computation
////  def storeNeighborStructure(simpleGraph: List[SimpleVertex], vertices: HashMap[Int, SimpleVertex]) = {
////    simpleGraph.foreach { current =>
////      var neighborSetForVariable: ArrayBuffer[MaxSumId] = ArrayBuffer()
////      var neighborSetForFunction: ArrayBuffer[MaxSumId] = ArrayBuffer()
////
////      neighborSetForVariable += current.functionVertex.id
////      neighborSetForFunction += current.variableVertex.id
////
////      current.neighborhood.foreach { neighborId =>
////
////        val simpleVertex = vertices(neighborId)
////
////        neighborSetForVariable += simpleVertex.functionVertex.id
////        neighborSetForFunction += simpleVertex.variableVertex.id
////      }
////
////      ProblemConstants.neighborStructure += (current.variableVertex.id -> neighborSetForVariable)
////      ProblemConstants.neighborStructure += (current.functionVertex.id -> neighborSetForFunction)
////
////    }
////  }
//  
//  def getNeighbors(functionIdNum : Int, vertices: HashMap[Int, SimpleVertex]) = {
//    val simpleVertex = vertices(functionIdNum)
//    var neighborSetForFunction: ArrayBuffer[MaxSumId] = ArrayBuffer()
//    simpleVertex.neighborhood.foreach{ neighborId =>
//      val neighbor = vertices(neighborId)
//      neighborSetForFunction += neighbor.variableVertex.id
//    }
//    neighborSetForFunction
//  }
//
//  /* 
//   * Load a graph from an edge list.
//   * Edge lists have the format:
//   * 0 1
//   * 1 2
//   * 3 4
//   * ...
//   * where the numbers corresponed to the ids of vertices.
//   * Two edges on a line mean that these two edges are connected
//   * through an edge.
//   * 
//   * Main Author: Robin Hafen, slight modifications by Genc Mazlami
//   */
//  def fromEdgeList(filename: String): (LinkedHashSet[Set[Int]], scala.collection.mutable.Set[Int]) = {
//    val undirectedEdges = LinkedHashSet[Set[Int]]()
//    var vertices = scala.collection.mutable.Set[Int]()
//
//    try {
//      var iStr, jStr, kStr = ""
//      val src = io.Source.fromFile(filename)
//      src.getLines foreach { line =>
//      val array = line.split(" ")
//        if(array.length == 2){
//          iStr = array(0)
//          jStr  = array(1)
//        }else if(array.length == 3){
//          kStr = array(0)
//          iStr  = array(1)
//          jStr  = array(2)
//        }
//        if (iStr != jStr) {
//          undirectedEdges.add(Set(iStr.toInt, jStr.toInt))
//
//          if (!vertices.contains(jStr.toInt)) {
//            vertices += jStr.toInt
//          }
//
//          if (!vertices.contains(iStr.toInt)) {
//            vertices += iStr.toInt
//          }
//        }
//      }
//    } catch {
//      case e: FileNotFoundException => throw e
//    }
//    (undirectedEdges, vertices)
//  }
//
//}
