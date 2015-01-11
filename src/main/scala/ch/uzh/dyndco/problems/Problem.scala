package ch.uzh.dyndco.problems

import collection.mutable.Set
import collection.mutable.Map
import scala.collection.mutable.MutableList

trait Problem {
  
  def getDomain() : Any
  def setDomain(domain : Any)
  def increaseDomain(percentage : Double)
  def decreaseDomain(percentage : Double)
  
}