lazy val commonSettings = Seq(
  name := """rps-bot""",
  organization := "com.example",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.10.6", "2.12.2"),
  libraryDependencies ++= Seq(cache, filters,
    "org.scalatest" %% "scalatest" % "2.2.6" % Test,
    "org.mockito" % "mockito-core" % "1.9.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "test"
  )
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).enablePlugins(PlayScala)

