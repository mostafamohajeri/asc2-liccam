package drivingdemo

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import bb.{BeliefBaseStyla, BeliefBaseStylaConcurrent, IBeliefBase, IBeliefBaseFactory}
import bb.expstyla.exp.{GenericTerm, IntTerm, StringTerm, StructTerm}
import infrastructure.{AgentRequest, AgentRequestMessage, AkkaMessageSource, ExecutionContext, GoalMessage, IMessage, IMessageSource, IntentionDoneMessage, MAS}
import org.scalatest.wordspec.AnyWordSpecLike
import std.DefaultCommunications

import scala.collection.mutable.ListBuffer

class CarTests extends ScalaTestWithActorTestKit with AnyWordSpecLike {
    val mas = MAS()
    val m = testKit.spawn(mas(), "MAS")
  val loggableComs = new DefaultCommunications(logger = PlantUMLCommunicationLogger())





    override def beforeAll(): Unit = {

      val prob = testKit.createTestProbe[IMessage]()
      m ! AgentRequestMessage(
        Seq(
          AgentRequest(new asl.car(coms=loggableComs).agentBuilder, "car", 2),
          AgentRequest(new asl.oem(coms=loggableComs).agentBuilder, "oem1", 1),
        ),
        prob.ref)
      Thread.sleep(3000)
    }

    "A car agent" should {


      "set speed" in {
        val prob = testKit.createTestProbe[IMessage]()
        mas.yellowPages.getAgent("car1").get.asInstanceOf[AkkaMessageSource].address() ! GoalMessage(StructTerm("set_speed",Seq(IntTerm(100))),AkkaMessageSource(prob.ref))
        assert(prob.receiveMessage().isInstanceOf[IntentionDoneMessage])
        assert(Environment.cars("car1").getSpeed() equals 100)
      }
    }
}
