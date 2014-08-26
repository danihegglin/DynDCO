/*
 *  @author Philip Stutz
 *  @author Thomas Keller
 *
 *  Copyright 2012 University of Zurich
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

package com.signalcollect.interfaces

import com.signalcollect.worker.AkkaWorker

// Has to be a trait to be proxied.
trait Node[Id, Signal] {
  def createWorker(workerId: Int, creator: () => AkkaWorker[Id, Signal]): String // string = remote actor address
  def initializeMessageBus(numberOfWorkers: Int, numberOfNodes: Int, messageBusFactory: MessageBusFactory[Id, Signal], mapperFactory: MapperFactory[Id])
  def initializeIdleDetection
  def numberOfCores: Int
  def shutdown
}
