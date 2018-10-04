name := "loci-commons"

version := "0.1"

scalaVersion := "2.12.7"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")
resolvers += Resolver.bintrayRepo("andyglow", "scala-tools")


val jacksonVersion = "2.9.1"


scalacOptions ++=
  Seq("-Yrangepos"
    , "-Ywarn-unused"
    , "-Ywarn-unused-import"
    , "-deprecation"
    , "-feature"
    , "-unchecked"
  )
parallelExecution in Test := false


libraryDependencies ++=
  Seq("ch.qos.logback" % "logback-classic" % "1.1.11"
    , "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
    , "org.scalatest" %% "scalatest" % "3.0.3" % "test"
    , "net.cakesolutions" %% "scala-kafka-client-akka" % "2.0.0"
    , "com.whisk" %% "docker-testkit-scalatest" % "0.9.6" % "test"
    , "com.whisk" %% "docker-testkit-impl-spotify" % "0.9.6" % "test"
  )