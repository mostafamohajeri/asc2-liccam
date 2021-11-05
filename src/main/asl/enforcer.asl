controller("car1","oem1").
controller("car2","oem1").
controller("car3","oem1").
controller("car4","oem1").

+!intervene(CaseID,Car,Speed,Confidence) :
    Monitor = #executionContext.src.name =>
        T = #java.time.Instant.now().getEpochSecond;
        +case(CaseID,Car,Speed,Confidence,Monitor,T);
        !try_access(CaseID,Car,Monitor).


+!try_access(CaseID,Car,Monitor) : controller(Car,Controller) =>
    Intervention = #drivingdemo.Utils.decide_intervention;
    #drivingdemo.Environment.logEvent("Intervention: " + Intervention);
    #drivingdemo.Environment.logEvent("OEM:" + Controller);
    #coms.achieve("oracle",give_warrant(CaseID,Car,Controller,Monitor,Intervention)).


+!try_access(CaseID,Car,Monitor) =>
    #println("No controller found for car:" + Car);
    #coms.inform(Monitor,no_controller(Car)).


+!serve_warrant(CaseID,Warrant) :
    case(CaseID,Car,Speed,Confidence,Monitor,T) &&
    controller(Car,Controller) =>
        #coms.inform(Monitor,intervention_validated(CaseID));
        Response = #coms.ask(Controller,intervention_access(Car,Warrant));
        !try_intervene(CaseID,Car,Response).


+!try_intervene(CaseID,Car,executed_command(Action,Result)) =>
        //Result = #coms.ask(Car,execute_command(Action,Secret));
        #coms.achieve("oracle",close_case(CaseID,Result));
        !inform_all_parties(CaseID,Result).


+!try_intervene(CaseID,Car,Response) =>
    #coms.achieve("oracle", warrant_not_successful(CaseID,Car, Response)).


+!inform_all_parties(CaseID,Result) => #println("inform all").