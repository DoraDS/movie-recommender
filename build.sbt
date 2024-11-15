ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "movie-recommender"
  )

libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.3.15"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"
libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7"

scalaVersion := "2.12.18"

version := "1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)

fork := true

libraryDependencies += "io.delta" %% "delta-spark" % "3.2.0"
