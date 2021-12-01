package liccam

//#import
import akka.actor.typed.ActorSystem
import bb.expstyla.exp.{BooleanTerm, DoubleTerm, IntTerm, ListTerm, StringTerm, StructTerm}
import infrastructure.{AkkaMessageSource, GoalMessage, IMessageSource}
import liccam.StatusResponse.State
import liccam.{Empty, Monitor, Status, StatusResponse}

import scala.concurrent.Future

//#import
/*
./grpcurl -plaintext -d '{"car_id":"car1","assessment":[{"probability":0.6,"risk":10,"intervention_risk":6.0,"intervention":{"set_speed":{"speed":100},"car_id":"car1"}}]}' localhost:8181 liccam.Monitor/SendStatus
 */
//#service-request-reply
//#service-stream
class MonitorImpl(system: ActorSystem[_], iMessageSource: IMessageSource) extends Monitor {
  private implicit val sys: ActorSystem[_] = system


  //#service-request-reply

  override def sendStatus(in: Status): Future[StatusResponse] = {

    iMessageSource.asInstanceOf[AkkaMessageSource].address() !
      GoalMessage(StructTerm("alert", Seq(StringTerm(in.carId),ListTerm(
        in.assessment.map(
          a => StructTerm("assessment",
            Seq(
              if (a.intervention.get.payload.isSetSpeed)
                StructTerm("set_speed",Seq(DoubleTerm(a.intervention.get.getSetSpeed.speed)))
              else
                BooleanTerm(false),
              DoubleTerm(a.probability),
              DoubleTerm(a.risk),
              DoubleTerm(a.interventionRisk)

            ))
        )
      ))), Environment.environmentActor)

    Future.successful(StatusResponse(State.STARTED))
  }



  //#service-request-reply
}
//#service-stream
//#service-request-reply