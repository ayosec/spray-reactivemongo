import AssemblyKeys._

name := "rmtest"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.8",
  "io.spray" % "spray-routing" % "1.1-M7",
  "io.spray" % "spray-can" % "1.1-M7",
  "com.typesafe.akka" %% "akka-actor" % "2.1.2"
)

assemblySettings

