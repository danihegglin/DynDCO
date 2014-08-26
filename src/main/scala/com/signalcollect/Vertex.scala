/*
 *  @author Philip Stutz
 *
 *  Copyright 2010 University of Zurich
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.signalcollect

/**
 *  This abstract class represents the framework's view of a vertex.
 *
 *  @author Philip Stutz
 */
abstract class Vertex[@specialized(Int, Long) +Id, State, GraphIdUpperBound, GraphSignalUpperBound] extends Serializable {

  override def hashCode = id.hashCode

  /**
   * Two vertices are equal if their ids are equal.
   */
  override def equals(other: Any): Boolean =
    other match {
      case v: Vertex[_, _, _, _] => v.id == id
      case _ => false
    }

  /**
   * A vertex id uniquely defines a vertex in the graph.
   */
  def id: Id
  def state: State
  def setState(s: State)

  /**
   *  Adds a new outgoing `Edge` to this `Vertex`.
   *  @param e the edge to be added.
   */
  def addEdge(e: Edge[GraphIdUpperBound], graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound]): Boolean

  /**
   *  Removes an outgoing `Edge` from this `Vertex`.
   *  @param edgeId the edge id to be removed
   *  @return returns if an edge was removed
   */
  def removeEdge(targetId: GraphIdUpperBound, graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound]): Boolean

  /**
   *  Removes all outgoing `Edge`s from this `Vertex`, returns the number of edges that were removed.
   */
  def removeAllEdges(graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound]): Int

  /**
   *  Delivers signals that are addressed to this specific vertex
   *
   *  @param signal the the signal to deliver to this vertex
   *  @param sourceId optional: the id of the vertex from which this signal was sent
   *
   *  @return true if the vertex decided to collect immediately.
   */
  def deliverSignalWithSourceId(signal: GraphSignalUpperBound, sourceId: GraphIdUpperBound, graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound]): Boolean

  /**
   *  Delivers signals that are addressed to this specific vertex
   *
   *  @param signal the the signal to deliver to this vertex
   *
   *  @return true if the vertex decided to collect immediately.
   */
  def deliverSignalWithoutSourceId(signal: GraphSignalUpperBound, graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound]): Boolean

  /**
   *  This method tells this `Vertex` to execute the signal operation on all its outgoing
   *  Edges. This method is going to be called by the framework during its execution (i.e. the
   *  `Worker` implementations).
   */
  def executeSignalOperation(graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound])

  /**
   *  Tells this vertex to execute the `collect` method.
   */
  def executeCollectOperation(graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound])

  /**
   * This method is used by the framework in order to decide if the vertex' signal operation should be executed.
   * The higher the returned value the more likely the vertex will be scheduled for executing its signal method.
   * @return the score value.
   */
  def scoreSignal: Double

  /**
   * This method is used by the framework in order to decide if the collect operation
   * should be executed.
   *
   * @return the score value. The meaning of this value depends on the thresholds set in the framework.
   */
  def scoreCollect: Double

  /**
   *  @return the number of outgoing edges of this `Vertex`
   */
  def edgeCount: Int

  /**
   *  This method gets called by the framework after the vertex has been fully initialized.
   */
  def afterInitialization(graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound])

  /**
   *  This method gets called by the framework before the vertex gets removed.
   */
  def beforeRemoval(graphEditor: GraphEditor[GraphIdUpperBound, GraphSignalUpperBound])

  /**
   * Returns the ids of the target vertices of outgoing edges of the vertex.
   */
  def targetIds: Traversable[GraphIdUpperBound]

  /**
   * The expose function can provide additional information about the vertex.
   * By default, it returns an empty map, but it can be overridden to return
   * any kind of Map[String,Any]. The Map will be serialized to json
   * recursively and can be viewed in the console when enabling the "expose
   * vertex details on click" option.
   * @return a string-keyed map of details about the node.
   */
  def expose: Map[String, Any] = Map[String, Any]()

}
