+!exit => #std.utils.exit().

+!start =>
    //#coms.achieve("car1",set_speed(70));
    //#Thread.sleep(#asInteger(500));
    //#coms.achieve("car1",set_speed(110));
    //#Thread.sleep(#asInteger(500));
    #coms.achieve("car1",set_speed(150));
    #Thread.sleep(#asInteger(500)).


+!print(File) =>
    #drivingdemo.Environment.print_to_file(#asString(File)).