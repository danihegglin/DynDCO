import AssemblyKeys._ 

assemblySettings

/** Project */
name := "dyndco"

version := "1.1.0-SNAPSHOT"

organization := "ch.uzh"

scalaVersion := "2.11.4"

/** 
 * See https://github.com/sbt/sbt-assembly/issues/123
 */
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList(ps @ _*) if ps.last == ".DS_Store" => MergeStrategy.discard
    case other => old(other)
  }
}

test in assembly := {}

scalacOptions ++= Seq("-optimize", "-Ydelambdafy:inline", "-Yclosure-elim", "-Yinline-warnings", "-Ywarn-adapted-args", "-Ywarn-inaccessible", "-feature", "-deprecation", "-Xelide-below", "INFO")

//, "-Ylog:icode"
//, "-Ydebug"

assembleArtifact in packageScala := true

parallelExecution in Test := false

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.withSource := true

jarName in assembly := "dyndco-1.1-SNAPSHOT.jar"

/** Dependencies */
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.7" % "compile",
  "com.typesafe.akka" %% "akka-remote" % "2.3.7" % "compile",
  "org.scala-lang" % "scala-library" % "2.11.4" % "compile",
  "com.github.romix.akka" %% "akka-kryo-serialization-custom" % "0.3.5" % "compile",
  "org.json4s" %% "json4s-native" % "3.2.9",
  "org.java-websocket" % "Java-WebSocket" % "1.3.0" % "compile",
  "junit" % "junit" % "4.8.2"  % "test",
  "org.specs2" % "classycle" % "1.4.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.0"  % "test",
  "org.specs2" %% "specs2" % "2.3.11"  % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.3" % "test",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test",
  "org.scalatest" %% "scalatest" % "2.1.3" % "compile",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.5",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "org.scalaj" % "scalaj-http_2.8.1" % "0.3.0",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",
  "ch.ethz.ganymed" % "ganymed-ssh2" % "build210"  % "compile",
  "commons-codec" % "commons-codec" % "1.7"  % "compile",
  "org.easymock" % "easymock" % "3.2" % "test",
  "org.webjars" % "d3js" % "3.4.13",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "reconnecting-websocket" % "23d2fbc",
  "org.webjars" % "intro.js" % "1.0.0"
)

resolvers += "Scala-Tools Repository" at "https://oss.sonatype.org/content/groups/scala-tools/"

resolvers += "Sonatype Snapshots Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Ifi Public" at "https://maven.ifi.uzh.ch/maven2/content/groups/public/"

transitiveClassifiers := Seq("sources")
