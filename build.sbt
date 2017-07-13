lazy val commonSettings = Seq(
  name := """rps-bot""",
  organization := "com.example",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.10.6", "2.12.2"),
  libraryDependencies ++= Seq(cache, filters,
    "org.specs2" %% "specs2-core" % "3.8.4" % "test",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.mockito" % "mockito-core" % "1.9.0" % "test",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test"
  )
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).enablePlugins(PlayScala)

