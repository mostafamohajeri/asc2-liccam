valid_stamps(0).

+!give_warrant(CaseID,Car,Controller,Monitor,Action) : Enforcer = #executionContext.src.name =>
    MonitorResponse = #coms.ask(Monitor,confirm_alert(CaseID,Car));
    !validate(CaseID,Car,Controller,MonitorResponse,Enforcer,Action).

+!validate(CaseID,Car,Controller,false,Enforcer,Action) =>
    T = #java.time.Instant.now().getEpochSecond;
    +log(invalid,Enforcer,T);
    #coms.achieve(Enforcer,drop_case(CaseID)).

@atomic
+!validate(CaseID,Car,Controller,true,Enforcer,Action) :
    T = #java.time.Instant.now().getEpochSecond &&
    Issuer = #executionContext.name &&
    valid_stamps(S) &&
    StampCode is S+1 =>
        Stamp = #executionContext.name + "_" + #asString(#asInteger(StampCode));
        +log(valid,Sender,T);
        Warrant = warrant(Car,Controller,Action,Enforcer,Issuer,Stamp);
        +warrants(CaseID,Warrant,T);
        #coms.achieve(Enforcer,serve_warrant(CaseID,Warrant)).


+?validate_warrant(warrant(Car,Controller,Action,Requester,Issuer,Stamp)) :
    warrants(CaseId,warrant(Car,Controller,Action,Requester,Issuer,Stamp),T)
    =>
    #coms.respond(true).

+?validate_warrant(warrant(Car,Controller,Action,Requester,Issuer,Stamp)) =>
    #coms.respond(false).

+!warrant_not_successful(CaseID,Car, Reason) : warrants(CaseId,warrant(Car,Controller,Action,Requester,Issuer,Stamp),T) =>
    #coms.inform(Controller,fine(CaseId,Reason)).


+!close_case(CaseID,Result) =>
    #println("case:" + CaseID + " closed").