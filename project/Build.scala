import sbt._
import Keys._

object CoreBuild extends Build {
  lazy val core = Project(id = "dyndco", base = file("."))
}
