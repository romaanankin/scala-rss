name := "rss-scala"

version := "0.1"

scalaVersion := "2.12.3"

//libraryDependencies +=  "com.rometools" % "rome" % "1.8.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
    "com.lucidchart" %% "xtract" % "2.0.1",
    "com.typesafe.play" % "play-json_2.11" % "2.7.4",
    "org.scalatest" %% "scalatest" % "3.0.8"

)

scalacOptions += "-deprecation"