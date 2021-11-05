package drivingdemo

import akka.actor.typed.ActorSystem
import bb.expstyla.exp.{GenericTerm, IntTerm, StructTerm}
import infrastructure.{AgentRequest, AgentRequestMessage, AkkaMessageSource, GoalMessage, IMessage, MAS}
import std.DefaultCommunications

object LICCAM {


  def main(args: Array[String]): Unit = {
    run_success
  }

  def run_success = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()
    val logger = PlantUMLCommunicationLogger()
    val loggableComs = new DefaultCommunications(logger)
    Environment.comsLogger = logger
    Environment.logActors(Seq("car1","sensor1","monitor","enforcer","oem1","oracle"))

    val mas = MAS()
    val system: ActorSystem[IMessage] = ActorSystem(mas(name = "__MAS",createHTTPServer = true,HTTPPort = 3030), "MAS")
    system ! AgentRequestMessage(
      Seq(
        AgentRequest(new asl.sensor(coms=loggableComs).agentBuilder, "sensor1", 1),
        AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car1", 1),
        AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
        AgentRequest(new asl.monitor(coms=loggableComs).agentBuilder, "monitor", 1),
        AgentRequest(new asl.enforcer(coms=loggableComs).agentBuilder, "enforcer", 1),
        AgentRequest(new asl.oracle(coms=loggableComs).agentBuilder, "oracle", 1),
        AgentRequest(new asl.environment(coms=loggableComs).agentBuilder, "scenario", 1)
      ),
      null)
    Thread.sleep(5000)

    val environment = mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address()
    Environment.environmentActor = AkkaMessageSource(environment.ref)


    mas.yellowPages.getAll().filter(n=>n._1.contains("sensor")).foreach(a => Environment.sensors.put(a._1,a._2))
    mas.yellowPages.getAll().filter(n=>n._1.contains("car")).foreach(a => Environment.cars.put(a._1,Car(a._1)))

    mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("start",Seq()),Environment.environmentActor)


    Thread.sleep(6000)

    Environment.print_to_file("all")
  }


  def run_bad_oem = {
        import org.apache.log4j.BasicConfigurator
        BasicConfigurator.configure()
    val logger = PlantUMLCommunicationLogger()
    val loggableComs = new DefaultCommunications(logger)

    val mas = MAS()
    val system: ActorSystem[IMessage] = ActorSystem(mas(), "MAS")
    system ! AgentRequestMessage(
      Seq(
        AgentRequest(new asl.sensor(coms=loggableComs).agentBuilder, "sensor1", 1),
        AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car1", 1),
        AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
        AgentRequest(new asl.monitor(coms=loggableComs).agentBuilder, "monitor", 1),
        AgentRequest(new asl.enforcer(coms=loggableComs).agentBuilder, "enforcer", 1),
        AgentRequest(new asl.oracle(coms=loggableComs).agentBuilder, "oracle", 1),
        AgentRequest(new asl.environment().agentBuilder, "scenario", 1)
      ),
      null)
    Thread.sleep(5000)



    val environment = mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address()
    Environment.environmentActor = AkkaMessageSource(environment.ref)
    Environment.comsLogger = logger

    mas.yellowPages.getAll().filter(n=>n._1.contains("sensor")).foreach(a => Environment.sensors.put(a._1,a._2))
    mas.yellowPages.getAll().filter(n=>n._1.contains("car")).foreach(a => Environment.cars.put(a._1,Car(a._1)))

    mas.yellowPages.getAgent("car1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(140))),Environment.environmentActor)
    Thread.sleep(3000)

    Environment.print_to_file("all")
  }

  def  run_success_2  = {
        import org.apache.log4j.BasicConfigurator
        BasicConfigurator.configure()
    val logger = PlantUMLCommunicationLogger()
    val loggableComs = new DefaultCommunications(logger)

    val mas = MAS()
    val system: ActorSystem[IMessage] = ActorSystem(mas(), "MAS")
    system ! AgentRequestMessage(
      Seq(
        AgentRequest(new asl.sensor(coms=loggableComs).agentBuilder, "sensor1", 1),
        AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car", 4),
        AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
        AgentRequest(new asl.monitor(coms=loggableComs).agentBuilder, "monitor", 1),
        AgentRequest(new asl.enforcer(coms=loggableComs).agentBuilder, "enforcer", 1),
        AgentRequest(new asl.oracle(coms=loggableComs).agentBuilder, "oracle", 1),
        AgentRequest(new asl.environment().agentBuilder, "scenario", 1)
      ),
      null)
    Thread.sleep(10000)



    val environment = mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address()
    Environment.environmentActor = AkkaMessageSource(environment.ref)
    Environment.comsLogger = logger
    Environment.logActors(mas.yellowPages.getAll().keys.toSeq.filter(a => !a.contains("__")))

    mas.yellowPages.getAll().filter(n=>n._1.contains("sensor")).foreach(a => Environment.sensors.put(a._1,a._2))
    mas.yellowPages.getAll().filter(n=>n._1.contains("car")).foreach(a => Environment.cars.put(a._1,Car(a._1)))

    mas.yellowPages.getAgent("car1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(150))),Environment.environmentActor)

    mas.yellowPages.getAgent("car2").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(130))),Environment.environmentActor)
//
    mas.yellowPages.getAgent("car3").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(140))),Environment.environmentActor)

    mas.yellowPages.getAgent("car4").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(180))),Environment.environmentActor)

    Thread.sleep(6000)

    Environment.print_to_file("all")
  }

}
