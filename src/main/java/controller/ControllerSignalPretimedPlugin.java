package controller;

import actuator.AbstractActuator;
import actuator.sigint.ActuatorSignal;
import api.events.EventControllerScheduleTransition;
import control.AbstractController;
import control.sigint.ScheduleItem;
import dispatch.Dispatcher;
import dispatch.EventPoke;
import error.OTMErrorLog;
import error.OTMException;
import jaxb.Controller;
import runner.Scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerSignalPretimedPlugin extends AbstractController {

    public List<ScheduleItem> schedule;
    public Integer current_schedule_item_index;

    ///////////////////////////////////////////////////
    // construction
    ///////////////////////////////////////////////////

    public ControllerSignalPretimedPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);

        System.out.println("ControllerSignalPretimedPlugin: constructor");

        if(jaxb_controller.getSchedule()==null)
            return;

        // create schedule
        schedule = new ArrayList<>();
        for(jaxb.ScheduleItem si : jaxb_controller.getSchedule().getScheduleItem())
            schedule.add(new ScheduleItem(si));
        Collections.sort(schedule);

    }

    ///////////////////////////////////////////////////
    // initialize
    ///////////////////////////////////////////////////

    @Override
    public void validate(OTMErrorLog errorLog) {
        super.validate(errorLog);

        System.out.println("ControllerSignalPretimedPlugin: validate");

        // single target
        if(actuators.size()!=1)
            errorLog.addError("actuators.size()!=1");

        // target is a signal
        if(actuators.iterator().next().type!=AbstractActuator.Type.signal)
            errorLog.addError("actuator is not a signal");

        // validate schedule items
        for(ScheduleItem item : schedule)
            item.validate(errorLog);

    }

    @Override
    public void initialize(Scenario scenario,float now) throws OTMException {
        System.out.println("ControllerSignalPretimedPlugin: initialize");

        current_schedule_item_index = get_item_index_for_time(now);

        // not reached first item, set all to dark
        if(current_schedule_item_index ==null) {
            ((ActuatorSignal) actuators.iterator().next()).turn_off(now);
            return;
        }

        // inform output writer
        if(event_listener !=null)
            event_listener.write(now, new EventControllerScheduleTransition(now,id,current_schedule_item_index));
    }

    @Override
    public void register_initial_events(Dispatcher dispatcher) {
        System.out.println("ControllerSignalPretimedPlugin: register_initial_events");

        // register next schedule item change
        register_next_poke(dispatcher);

    }

    @Override
    public Object get_current_command() {
        System.out.println("ControllerSignalPretimedPlugin: get_current_command");
        return schedule.get(current_schedule_item_index);
    }

    ///////////////////////////////////////////////////
    // update
    ///////////////////////////////////////////////////

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {
        System.out.println("ControllerSignalPretimedPlugin: ("+timestamp+") update_controller");

        // advance current schedule item index
        if(advance_schedule_item_index()) {

            // send current item to actuator
            get_signal().process_controller_command(get_current_command(),dispatcher,timestamp);

            // register next change schedule change
            register_next_poke(dispatcher);

            // inform output writer
            if(event_listener !=null)
                event_listener.write(timestamp, new EventControllerScheduleTransition(timestamp,id,current_schedule_item_index));
        }

    }

    ///////////////////////////////////////////////////
    // getters
    ///////////////////////////////////////////////////

    public ActuatorSignal get_signal(){
        return (ActuatorSignal) actuators.iterator().next();
    }

    public Integer get_item_index_for_time(float time){
        if(schedule.isEmpty())
            return null;
        int s = 0;
        if(time<schedule.get(0).start_time)
            return null;
        for(int e=1;e<schedule.size();e++){
            if(time < schedule.get(e).start_time)
                break;
            s = e;
        }
        return s;
    }

    ///////////////////////////////////////////////////
    // private
    ///////////////////////////////////////////////////

    private boolean advance_schedule_item_index(){
        if(current_schedule_item_index ==null) {
            current_schedule_item_index = 0;
            return true;
        }
        else if(current_schedule_item_index <schedule.size()-1){
            current_schedule_item_index++;
            return true;
        } else {
            return false;
        }
    }

    // returns the index of the next item. returns current index if it si the last.
    private void register_next_poke(Dispatcher dispatcher){

        if(current_schedule_item_index !=null && current_schedule_item_index >=schedule.size()-1)
            return;

        int next_item_index = current_schedule_item_index ==null ? 0 : current_schedule_item_index +1;
        ScheduleItem next_item = schedule.get(next_item_index);
        dispatcher.register_event(new EventPoke(dispatcher,1,next_item.start_time,this));
    }

}
