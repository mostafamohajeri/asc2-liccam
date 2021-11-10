sensors("sensor1").
sensors("sensor2").
sensors("sensor3").
cases(0).

@atomic
+!alert(Car,Speed,Confidence) : Sender = #executionContext.src.name && sensors(Sender) && cases(C)  =>
    T = #java.time.Instant.now().getEpochSecond;
    Id = #asString(Car) + #asString("_") + #asString(C+1);
    +log(Id,Car,Speed,Confidence,T);
    !try_intervention(Id,Car,Speed,Confidence).

+!try_intervention(Id,Car,Speed,Confidence) : Speed > 90 && Speed < 120 && Confidence < 0.75 =>
    #drivingdemo.Environment.logEvent("Operator Intervention Required").

+!try_intervention(Id,Car,Speed,Confidence) : Speed > 90 && Speed < 120 && Confidence > 0.75 =>
     #drivingdemo.Environment.logEvent("Speed < 120: No intervention").

+!try_intervention(Id,Car,Speed,Confidence) : Speed >= 120 =>
    T = #java.time.Instant.now().getEpochSecond;
    +case(Id,Car,Speed,Confidence,T);
    #drivingdemo.Environment.logEvent("Speed > 120: Intervention Required");
    #coms.achieve("enforcer",intervene(Id,Car,Speed,Confidence)).


+?confirm_alert(CaseID,Car) : case(CaseID,Car,Speed,Confidence,T) =>
    #coms.respond(true).

+?confirm_alert(CaseID,Car) : not case(CaseID,Car,Speed,Confidence,T) =>
    #coms.respond(false).
