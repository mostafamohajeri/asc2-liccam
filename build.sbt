import sbt.{ThisBuild, file}
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
lazy val AkkaVersion = "2.6.17"
name := "legal-intervententions-demo"
lazy val AkkaManagementVersion = "1.1.1"
lazy val AkkaHttpVersion = "10.2.7"
version := "0.1"

scalaVersion := "2.13.7"
organization := "nl.uva.sne.cci"
testOptions in Test += Tests.Argument("-oD")
resolvers += ("agent-script" at "http://145.100.135.102:8081/repository/agent-script/").withAllowInsecureProtocol(true)


enablePlugins(AkkaGrpcPlugin)



libraryDependencies += "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion
libraryDependencies += "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % AkkaManagementVersion
libraryDependencies += "com.typesafe.akka" %% "akka-discovery" % AkkaVersion


libraryDependencies +=  "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion
libraryDependencies +=  "com.typesafe.akka" %% "akka-discovery" % AkkaVersion


libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
//libraryDependencies += "com.typesafe.akka" %% "akka-http" % AkkaVersion
libraryDependencies += "nl.uva.sne.cci" % "agentscript-parser" % "2.27"
libraryDependencies += "nl.uva.sne.cci" % "agentscript-scala-generator" % "2.27"
libraryDependencies += "nl.uva.sne.cci" %% "styla" % "0.2.2"

libraryDependencies += "nl.uva.sne.cci" % "agentscript-grounds_2.13" % "0.2.40"
libraryDependencies += "nl.uva.sne.cci" % "agentscript-commons_2.13" % "0.2.40"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"

libraryDependencies += "net.sourceforge.plantuml" % "plantuml" % "1.2021.12"

// https://mvnrepository.com/artifact/guru.nidi/graphviz-java
libraryDependencies += "guru.nidi" % "graphviz-java" % "0.18.1"


libraryDependencies ++= Seq(
 "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
 "com.typesafe.akka" %% "akka-http2-support" % AkkaHttpVersion,
 "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
 "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
 "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
 "com.typesafe.akka" %% "akka-discovery" % AkkaVersion,
 "com.typesafe.akka" %% "akka-pki" % AkkaVersion,

 // The Akka HTTP overwrites are required because Akka-gRPC depends on 10.1.x
 "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
 "com.typesafe.akka" %% "akka-http2-support" % AkkaHttpVersion,

 "ch.qos.logback" % "logback-classic" % "1.2.3",

 "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
 "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
 "org.scalatest" %% "scalatest" % "3.1.1" % Test
)

enablePlugins(AgentScriptCCPlugin)

(agentScriptCC / agentScriptCCPath) in Compile :=  (baseDirectory.value / "src" / "main" / "asl")
 Compile / sourceGenerators += (Compile / agentScriptCC).taskValue


classLoaderLayeringStrategy in Test := ClassLoaderLayeringStrategy.ScalaLibrary
parallelExecution in Test := false