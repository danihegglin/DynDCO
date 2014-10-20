/*
 *  @author Genc Mazlami
 *
 *  Copyright 2013 University of Zurich
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

package com.signalcollect.dcop.util

import scala.collection.mutable.HashMap
import com.signalcollect.dcop.vertices.id.MaxSumId
import scala.collection.mutable.ArrayBuffer
import com.signalcollect.dcop.vertices.SimpleVertex
import ch.uzh.dyndco.algorithms.dyndco.incomplete.maxsum.old.MaxSumVertex
import com.signalcollect.dcop.vertices.VariableVertex

/**
 * Static object holding globally predefined constants used throughout the computation.
 * The vals can never be updated/changed, hence distribution is no problem. 
 */

object ProblemConstants {

  /*
   * set this flag to true to activate convergence detection on the algorithms
   * --> to use for convergence benchmarks
   */
  val convergenceEnabled = false
  
  val numOfColors: Int = 3

}