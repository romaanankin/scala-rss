name := "rss-scala"

version := "0.1"

scalaVersion := "2.12.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.apache.kafka" % "kafka-clients" % "2.3.0",
  "com.rometools" % "rome" % "1.8.1",
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
scalacOptions += "-deprecation"
test in assembly := {}
assemblyJarName in assembly := s"app-assembly.jar"
assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}