//ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","script-cc-grammars")


//githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")
//resolvers += Resolver.githubPackages("mostafamohajeri", "sbt-scriptcc")


resolvers += ("agent-script" at "http://145.100.135.102:8081/repository/agent-script/").withAllowInsecureProtocol(true)

addSbtPlugin("nl.uva.sne.cci" % "sbt-scriptcc" % "4.27")

addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.3.0")

//addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.5.3")