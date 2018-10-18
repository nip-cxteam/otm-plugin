package controller;

import actuator.ActuatorRampMeter;
import control.AbstractController;
import dispatch.Dispatcher;
import error.OTMException;
import jaxb.Controller;
import runner.Scenario;
import sensor.FixedSensor;

public class LineControlPlugin extends AbstractController {

    long first_act_id;
    long second_act_id;
    FixedSensor first_sensor;
    FixedSensor second_sensor;

    public LineControlPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);
        first_act_id = actuator_by_usage.get("first_actuator").getId();
        second_act_id = actuator_by_usage.get("second_actuator").getId();
        first_sensor = (FixedSensor) sensor_by_usage.get("first_sensor");
        second_sensor = (FixedSensor) sensor_by_usage.get("second_sensor");
    }

    @Override
    public void initialize(Scenario scenario, float v)  {
        this.command.put(first_act_id,ActuatorRampMeter.Color.green);
        this.command.put(second_act_id,ActuatorRampMeter.Color.green);
    }

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {

        double first_veh = first_sensor.get_vehicles();
        double second_veh = first_sensor.get_vehicles();

        double first_flw = first_sensor.get_flow_vph();
        double second_flw = first_sensor.get_flow_vph();

        if(timestamp>1200d)
            command.put(first_act_id,ActuatorRampMeter.Color.red);
        if(timestamp>2400d)
            command.put(first_act_id,ActuatorRampMeter.Color.green);

        if(timestamp>1200d)
            command.put(second_act_id,ActuatorRampMeter.Color.red);
        if(timestamp>2400d)
            command.put(second_act_id,ActuatorRampMeter.Color.green);

        System.out.println(String.format("%.1f\t%d\t%f\t%f\t%d\t%f\t%f",timestamp,
                command.get(first_act_id)==ActuatorRampMeter.Color.green?1:0,first_flw,first_veh,
                command.get(second_act_id)==ActuatorRampMeter.Color.green?1:0,second_flw,second_veh));

    }

}
