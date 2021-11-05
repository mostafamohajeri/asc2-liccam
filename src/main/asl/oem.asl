car("car1").
car("car2").
car("car3").
car("car4").
valid_warrant_issuer("oracle").


+!register(Secret) : Sender = #executionContext.src.name && car(Sender) =>
    -secret(Sender,_);
    +secret(Sender,Secret).


+?intervention_access(Car,warrant(Car,Controller,Action,Requester,Issuer,Stamp)) :
    car(Car) &&
    #executionContext.name == Controller &&
    valid_warrant_issuer(Issuer) =>
        Response = #coms.ask(Issuer, validate_warrant(warrant(Car,Controller,Action,Requester,Issuer,Stamp)));
        !respond_to_intervention_access(Response,Car,Action).


+!respond_to_intervention_access(false,Car,Action) =>
    #coms.respond(false).

+!respond_to_intervention_access(true,Car,Action) : secret(Car,Secret) =>
    Result = #coms.ask(Car,execute_command(Action,Secret));
    #coms.respond(executed_command(Action,Result)).
    //#coms.respond(false).

