monitor("monitor").



+!sense_car(Car,Speed) : Speed > 90 && monitor(M)  =>
    #drivingdemo.Environment.logEvent("Detected: " + Car + ", Speed:" + Speed);
    T = #java.time.Instant.now().getEpochSecond;
    +car(Car,Speed,T).
    //#coms.achieve(M,alert(Car,Speed,1.0))
    //.

+!sense_car(Car,Speed) : Speed =< 90 && monitor(M)  =>
    #drivingdemo.Environment.logEvent("Detected: " + Car + ", Speed:" + Speed).
