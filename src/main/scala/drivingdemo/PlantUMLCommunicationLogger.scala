package drivingdemo
import _root_.scala.concurrent.duration._
import _root_.akka.util.Timeout
import akka.actor.typed.Scheduler
import akka.util.Timeout
import bb.expstyla.exp.{BooleanTerm, GenericTerm, StructTerm}
import infrastructure.{AkkaMessageSource, AskMessage, BeliefMessage, ExecutionContext, GoalMessage, IMessage, IMessageSource}
import std.{CommunicationLogger, DefaultCommunications}
import _root_.akka.actor.typed.scaladsl.AskPattern._
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.io.FileOutputStream
import java.io.OutputStream

import guru.nidi.graphviz.parse.Parser

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, ExecutionContextExecutor, Future}


case class PlantUMLCommunicationLogger() extends CommunicationLogger {

  import java.io._

  var messages : ListBuffer[String] = new ListBuffer[String]()

  def addMsg(msg: String) = synchronized {
    println(msg)
    messages.addOne(msg)
  }

  def writeToFile(filename: String,pre:String,post:String): Unit = {
    val pw = new PrintWriter(new File(f"logs/$filename.txt" ))

    pw.println("@startuml")
    pw.println(pre)

    for(line <- this.messages) pw.println(line)

    pw.println(post)
    pw.println("@enduml")
    pw.close()

    writeToPNG(filename)

  }


  def writeToSVG(filename: String,seq: ListBuffer[String]): Unit = {
    import net.sourceforge.plantuml.FileFormat
    import net.sourceforge.plantuml.FileFormatOption
    import net.sourceforge.plantuml.SourceStringReader
    import java.io.ByteArrayOutputStream
    import java.nio.charset.Charset
    var source = "@startuml\n !theme sketchy\n"
    for(line <- seq) source += f"""$line\n"""
    source += "@enduml\n"

    val reader = new SourceStringReader(source)
    val os = new ByteArrayOutputStream
    val desc = reader.outputImage(os, new FileFormatOption(FileFormat.SVG))
    os.close()

    import java.io.FileOutputStream

    val outputStream = new FileOutputStream(f"logs/$filename.svg")

    os.writeTo(outputStream)
    outputStream.close()

  }

  def writeToPNG(fileName:String) : Unit = {
    for (lines <- messages.indices) {
      writeToPNG(fileName,messages.take(lines+1).toList)
    }
  }

  private def writeToPNG(filename: String,seq: List[String]): Unit = {

    var source = "@startuml\n !theme aws-orange\n skinparam backgroundColor white\n"
    for(line <- seq) source += f"""${line.replace("scenario","[")}\n"""
    source += "@enduml\n"

    val reader = new SourceStringReader(source)
    val os = new ByteArrayOutputStream
    // Write the first image to "os"
    val desc = reader.outputImage(os, new FileFormatOption(FileFormat.PNG))
    os.close()


    val outputStream = new FileOutputStream(f"logs/$filename.png")

    os.writeTo(outputStream)
    outputStream.close()
  }

  def logEvent(src:String, content: String): Unit = {
    addMsg(f"$src-->$src : ${content}")
  }

  def logActor(src:Seq[String]): Unit = {
    addMsg(src.map(a => f"participant $a").mkString("\n"))
  }


  override def logBeliefMessage(src: String, content: String, destination: String): Unit =
    {
      addMsg(f"$src->$destination : +$content")

    }

  override def logAskMessage(src: String, content: String, destination: String): Unit =
  {
    addMsg(f"$src->$destination : ?$content")
  }

  override def logRespondMessage(src: String, content: String, destination: String): Unit =
  {
    addMsg(f"""$src-->$destination : $content""")
  }

  override def logAchieveMessage(src: String, content: String, destination: String): Unit =
  {
    addMsg(f"$src->$destination : !$content")
  }
}