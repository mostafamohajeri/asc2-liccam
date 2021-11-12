package drivingdemo

//#import
import scala.concurrent.Future
import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.BroadcastHub
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.MergeHub
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import bb.expstyla.exp.{DoubleTerm, IntTerm, StringTerm, StructTerm}
import infrastructure.{AkkaMessageSource, GoalMessage, IMessageSource}

//#import

//#service-request-reply
//#service-stream
class MonitorServiceImpl(system: ActorSystem[_],iMessageSource: IMessageSource) extends MonitorService {
  private implicit val sys: ActorSystem[_] = system


  //#service-request-reply

  override def alert(request: AlertRequest): Future[AlertReply] = {
    iMessageSource.asInstanceOf[AkkaMessageSource].address() !
      GoalMessage(StructTerm("alert", Seq(StringTerm(request.car),IntTerm(request.speed),DoubleTerm(request.confidence))), Environment.environmentActor)
    Future.successful(AlertReply(s"Hello, ${request.car}"))
  }


  //#service-request-reply
}
//#service-stream
//#service-request-reply