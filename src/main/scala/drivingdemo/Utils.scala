package drivingdemo

import java.time.Instant

import bb.expstyla.exp.{IntTerm, StringTerm, StructTerm}
import infrastructure.ExecutionContext

object Utils {
    def generate_stamp (implicit executionContext: ExecutionContext): StringTerm = StringTerm("stamp_"+Instant.now().getEpochSecond)

    def decide_intervention (implicit executionContext: ExecutionContext): StructTerm = StructTerm("set_speed", Seq(IntTerm(90)))
    def stop (implicit executionContext: ExecutionContext): Unit = println(f"Agent ${executionContext.name} breaking")
}
