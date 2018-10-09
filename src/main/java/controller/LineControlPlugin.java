package controller;

import actuator.ActuatorRampMeter;
import control.AbstractController;
import dispatch.Dispatcher;
import dispatch.EventPoke;
import error.OTMException;
import jaxb.Controller;
import runner.Scenario;

public class LineControlPlugin extends AbstractController {

    public ActuatorRampMeter.Color current_color;

    public LineControlPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);
    }

    @Override
    public void initialize(Scenario scenario, float v) throws OTMException {
        current_color = ActuatorRampMeter.Color.green;
    }

    @Override
    public Object get_current_command() {
        return current_color;
    }

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {
        current_color = timestamp<500 ? ActuatorRampMeter.Color.green : ActuatorRampMeter.Color.red;
    }

    // returns the index of the next item. returns current index if it si the last.
    private void register_next_poke(Dispatcher dispatcher){
        dispatcher.register_event(new EventPoke(dispatcher,1,0f,this));
    }
}
