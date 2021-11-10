package drivingdemo

import akka.actor.typed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, Scheduler}
import akka.util.Timeout
import asl.sensor
import bb.IBeliefBase
import bb.expstyla.exp.{GenericTerm, IntTerm, StructTerm}
import infrastructure.{AgentRequest, AgentRequestMessage, AkkaMessageSource, DummyMessageSource, ExecutionContext, GoalMessage, IMessage, ISubGoalMessage, IYellowPages, InitEndMessage, IntentionDoneMessage, MAS, Parameters, ReadyMessage, StartMessage, SubGoalMessage}
import std.DefaultCommunications
import akka.actor.typed.scaladsl.AskPattern._
import _root_.scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object LICCAM {


  def main(args: Array[String]): Unit = {
    run_success
  }

  def run_success = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()

    /*
    Create the communication layer
     */
    val logger = PlantUMLCommunicationLogger()
    val loggableComs = new DefaultCommunications(logger)
    Environment.comsLogger = logger
    Environment.logActors(Seq("car1","sensor1","monitor","enforcer","oem1","oracle"))

    // Create System
    val mas = MAS()
    val system: ActorSystem[IMessage] = typed.ActorSystem(mas(createHTTPServer = true, HTTPPort = 8585), "MAS")

    implicit val timeout: Timeout = 5000.milliseconds
    implicit val ec: ExecutionContextExecutor = system.executionContext
    implicit val scheduler: Scheduler = system.scheduler

    // Ask the system to create agents
    val result: Future[IMessage] = system.ask(ref => AgentRequestMessage(
      Seq(
        AgentRequest(new asl.sensor(coms=loggableComs).agentBuilder, "sensor1", 1),
        AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car1", 1),
        AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
        AgentRequest(new asl.monitor(coms=loggableComs).agentBuilder, "monitor", 1),
        AgentRequest(new asl.enforcer(coms=loggableComs).agentBuilder, "enforcer", 1),
        AgentRequest(new asl.oracle(coms=loggableComs).agentBuilder, "oracle", 1),
        AgentRequest(new asl.environment(coms=loggableComs).agentBuilder, "scenario", 1)
      ),ref))

    //wait for response
    val system_ready : Boolean = try {
      val response = Await.result(result, timeout.duration).asInstanceOf[ReadyMessage]
      true
    }
    catch {
      case _ =>
        false
    }

    if(system_ready) {

      // extract the scenario agent ref
      val environment = mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address()
      Environment.environmentActor = AkkaMessageSource(environment)

      //extract cars and sensors for ethe environment
      mas.yellowPages.getAll().filter(n => n._1.contains("sensor")).foreach(a => Environment.sensors.put(a._1, a._2))
      mas.yellowPages.getAll().filter(n => n._1.contains("car")).foreach(a => Environment.cars.put(a._1, Car(a._1)))

      // tell scenario to start
      mas.yellowPages.getAgent("scenario").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("start", Seq()), Environment.environmentActor)

      // wait for finish
      Thread.sleep(6000)

      // write the message to file "./logs/<name>.png"
      Environment.print_to_file("all")
    }
  }



}
