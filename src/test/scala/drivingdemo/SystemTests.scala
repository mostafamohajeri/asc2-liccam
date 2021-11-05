package drivingdemo

import java.util.concurrent.TimeUnit

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.expstyla.exp.{IntTerm, StringTerm, StructTerm}
import infrastructure._
import org.scalatest.wordspec.AnyWordSpecLike
import std.DefaultCommunications

import scala.concurrent.duration.FiniteDuration

class SystemTests extends ScalaTestWithActorTestKit with AnyWordSpecLike {
    val mas = MAS()
    val m = testKit.spawn(mas(), "MAS")
  val loggableComs = new DefaultCommunications(logger = PlantUMLCommunicationLogger())
  val environment = testKit.createTestProbe[IMessage]()
  Environment.environmentActor = AkkaMessageSource(environment.ref)

    override def beforeAll(): Unit = {


    }



    "A car agent" should {

      "init" in {
        val prob = testKit.createTestProbe[IMessage]()
        m ! AgentRequestMessage(
          Seq(
            AgentRequest(new asl.sensor(coms=loggableComs).agentBuilder, "sensor1", 1),
            AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car", 2),
            AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
            AgentRequest(new asl.monitor(coms=loggableComs).agentBuilder, "monitor", 1),
            AgentRequest(new asl.enforcer(coms=loggableComs).agentBuilder, "enforcer", 1),
            AgentRequest(new asl.oracle(coms=loggableComs).agentBuilder, "oracle", 1)
          ),
          prob.ref)
          Thread.sleep(10000)
        val messages = prob.receiveMessages(3)
        assert(messages.size equals 3)
      }

      "setting sensors" in {
        mas.yellowPages.getAll().filter(n=>n._1.contains("sensor")).foreach(a => Environment.sensors.put(a._1,a._2))
        assert(Environment.sensors.size equals 1)
      }

      "alert through sensor" in {

        val mockedMonitor = testKit.createTestProbe[IMessage]()
        mas.yellowPages.putOne("scenario",AkkaMessageSource(mockedMonitor.ref))
        val prob = testKit.createTestProbe[IMessage]()
        mas.yellowPages.getAgent("car1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(140))),AkkaMessageSource(prob.ref))
        assert(prob.receiveMessage().isInstanceOf[IntentionDoneMessage])
        assert(Environment.cars("car1").getSpeed() equals 140)


        mockedMonitor.receiveMessage(FiniteDuration(10,TimeUnit.SECONDS))

      }

    }
}
