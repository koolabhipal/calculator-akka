
scalaVersion := "2.12.4"

name := "calc"
organization := "ch.epfl.scala"
version := "1.0"

libraryDependencies ++= Seq("org.typelevel" %% "cats-core" % "1.0.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "org.specs2" %% "specs2-junit" % "2.4.17" % Test,
  "org.scalatest" %% "scalatest" % "3.0.2" % Test,
  "org.specs2" %% "specs2" % "2.4.17" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
)
