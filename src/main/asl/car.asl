
code(#Random.alphanumeric.take(10).mkString).
oem("oem1").

!register.

+!register : oem(OEM) && code(Code) => #coms.achieve(OEM,register(Code)).

+?execute_command(Action,Secret) : code(Secret) =>
    !execute(Action).

+!execute(set_speed(S)) =>
    !set_speed(S);
    !respond(set_speed(S)).

+!respond(set_speed(S)) : speed(Speed) => #coms.respond(speed_changed(S,Speed)).

+!set_speed(Speed) : N = #executionContext.name =>
    #drivingdemo.Environment.set_speed(#asInteger(Speed));
    #drivingdemo.Environment.logEvent("setting speed to:" + Speed);
    -speed(_);
    +speed(Speed).