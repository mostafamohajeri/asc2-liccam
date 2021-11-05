import sbt.{ThisBuild, file}
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
lazy val AkkaVersion = "2.6.10"
name := "legal-intervententions-demo"

version := "0.1"

scalaVersion := "2.13.7"
organization := "nl.uva.sne.cci"
testOptions in Test += Tests.Argument("-oD")
resolvers += ("agent-script" at "http://145.100.135.102:8081/repository/agent-script/").withAllowInsecureProtocol(true)


libraryDependencies += "nl.uva.sne.cci" % "agentscript-parser" % "2.27"
libraryDependencies += "nl.uva.sne.cci" % "agentscript-scala-generator" % "2.27"
libraryDependencies += "nl.uva.sne.cci" %% "styla" % "0.2.2"

libraryDependencies += "nl.uva.sne.cci" % "agentscript-grounds_2.13" % "0.2.32"
libraryDependencies += "nl.uva.sne.cci" % "agentscript-commons_2.13" % "0.2.32"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"

libraryDependencies += "net.sourceforge.plantuml" % "plantuml" % "1.2021.12"

// https://mvnrepository.com/artifact/guru.nidi/graphviz-java
libraryDependencies += "guru.nidi" % "graphviz-java" % "0.18.1"


enablePlugins(AgentScriptCCPlugin)

  (agentScriptCC / agentScriptCCPath) in Compile :=  (baseDirectory.value / "src" / "main" / "asl")


  Compile / sourceGenerators += (Compile / agentScriptCC).taskValue
  skip in publish := true
  jacocoReportSettings := JacocoReportSettings(
    "Jacoco Coverage Report",
    None,
    JacocoThresholds(),
    Seq(JacocoReportFormats.ScalaHTML),
    "utf-8")



classLoaderLayeringStrategy in Test := ClassLoaderLayeringStrategy.ScalaLibrary
parallelExecution in Test := false