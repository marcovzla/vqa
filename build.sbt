name := "qa"

version := "0.0.1-SNAPSHOT"

organization := "org.clulab"

scalaVersion := "2.12.6"

libraryDependencies := Seq(
    "ai.lum" %% "common" % "0.0.8",
    "jline" % "jline" % "2.14.2",
    "org.apache.lucene" % "lucene-core" % "6.6.0",
    "org.apache.lucene" % "lucene-queryparser" % "6.6.0",
    "org.scala-lang.modules" %% "scala-xml" % "1.1.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.10"
)
