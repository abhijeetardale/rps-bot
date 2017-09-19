// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
val playVersion = scala.util.Properties.envOrElse("PLAY_VERSION", "2.5.14")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % playVersion)

