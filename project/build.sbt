

resolvers += ("agent-script" at "http://145.100.135.102:8081/repository/agent-script/").withAllowInsecureProtocol(true)

addSbtPlugin("nl.uva.sne.cci" % "sbt-scriptcc" % "4.27")

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.3.0")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "2.1.1")

addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.5")

