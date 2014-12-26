name := """dyndco-monitoring"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  ws, // Play's web services module
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.4",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "flot" % "0.8.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test",
  "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1",
  "io.spray" %%  "spray-json" % "1.3.1"
)
