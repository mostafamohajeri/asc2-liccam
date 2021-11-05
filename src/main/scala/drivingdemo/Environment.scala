package drivingdemo

import java.time.Instant

import akka.actor.typed.ActorRef
import bb.expstyla.exp.{GenericTerm, IntTerm, StringTerm, StructTerm}
import infrastructure.{AkkaMessageSource, ExecutionContext, GoalMessage, IMessage, IMessageSource}
import javax.swing.text.AbstractDocument.Content

import scala.collection.mutable

object Environment {
  var cars = mutable.Map.empty[String,Car]
  var sensors = mutable.Map.empty[String,IMessageSource]
  var environmentActor : AkkaMessageSource = null
  var comsLogger : PlantUMLCommunicationLogger = null

  def print_to_file(caseId: String): Unit = {
      comsLogger.writeToFile(caseId,"","")
  }

  def set_speed(speed: Int)(implicit executionContext: ExecutionContext) = {
    val car = executionContext.name
    if(!cars.contains(car))
      println("ERRROOORR")
    cars(car).setSpeed(speed)
    sensors.foreach(f =>
      f._2.asInstanceOf[AkkaMessageSource].address() !
        GoalMessage(StructTerm("sense_car",Seq(StringTerm(car),IntTerm(speed))),environmentActor)
    )
  }


//  def logEvent(content:GenericTerm)(implicit executionContext: ExecutionContext): Unit = {
//    comsLogger.logEvent(f"note over ${executionContext.name} : ${content.getStringValue}")
//  }

  def logEvent(content:GenericTerm)(implicit executionContext: ExecutionContext): Unit = {
    comsLogger.logEvent(executionContext.name,content.getStringValue)
  }

  def logActors(actorNames:Seq[String]): Unit = {
    comsLogger.logActor(actorNames)
  }




}
