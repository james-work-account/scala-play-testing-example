name := """testing-example"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies += "org.scalatest"          %% "scalatest"          % "3.2.9" % Test
libraryDependencies += "org.scalamock"          %% "scalamock"          % "5.1.0" % Test

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "1.0.7"
)

