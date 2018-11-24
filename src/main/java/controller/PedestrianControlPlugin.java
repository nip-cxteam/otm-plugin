package controller;

import actuator.AbstractActuator;
import actuator.ActuatorFD;
import common.Link;
import sensor.AbstractSensor;

import control.AbstractController;
import dispatch.Dispatcher;
import error.OTMException;
import jaxb.Controller;
import runner.Scenario;
import sensor.FixedSensor;

public class PedestrianControlPlugin extends AbstractController {

    long first_act_id;
    long second_act_id;
    int number_pedestrians = 0;
    FixedSensor first_sensor;
    FixedSensor second_sensor;

    public PedestrianControlPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);
        first_act_id = actuator_by_usage.get("first_actuator").getId();
        // second_act_id = actuator_by_usage.get("second_actuator").getId();
        first_sensor = (FixedSensor) sensor_by_usage.get("first_sensor");
        // second_sensor = (FixedSensor) sensor_by_usage.get("second_sensor");
    }

    @Override
    public void initialize(Scenario scenario, float v)  {
        // for (AbstractActuator x : actuator_by_usage.values()){
        //     System.out.println(x);
        //     ActuatorFD y = (ActuatorFD) x;
        // }
        // this.command.put(first_act_id,ActuatorFD.Color.green);
        // this.command.put(second_act_id,ActuatorFD.Color.green);
    }

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {
        for (AbstractSensor sens : sensor_by_usage.values()){
            FixedSensor sensy = (FixedSensor) sens;
            // System.out.println(sensy.get_vehicles());  //for my buses
        }
        // for (Link link : dispatcher.scenario.network.links.values()){
        //     link.get_veh_for_commodity(0L);
        // }

        // keep adding pedestrains here
        this.number_pedestrians += getPoisson(3);
        System.out.println(String.format("%d, vehicles commodity= %f, sensor measurement= %f", 
                        this.number_pedestrians, 
                        this.first_sensor.get_link().get_veh(),
                        this.first_sensor.get_vehicles()));
                                        
        // double first_veh = first_sensor.get_vehicles();
        // double second_veh = first_sensor.get_vehicles();

        // double first_flw = first_sensor.get_flow_vph();
        // double second_flw = first_sensor.get_flow_vph();

        // if(timestamp>1200d)
        //     command.put(first_act_id,ActuatorFD.Color.red);
        // if(timestamp>2400d)
        //     command.put(first_act_id,ActuatorFD.Color.green);

        // if(timestamp>1200d)
        //     command.put(second_act_id,ActuatorFD.Color.red);
        // if(timestamp>2400d)
        //     command.put(second_act_id,ActuatorFD.Color.green);

        // System.out.println(String.format("%.1f\t%d\t%f\t%f\t%d\t%f\t%f",timestamp,
        //         command.get(first_act_id)==ActuatorFD.Color.green?1:0,first_flw,first_veh,
        //         command.get(second_act_id)==ActuatorFD.Color.green?1:0,second_flw,second_veh));

        // this.first_sensor.get_link().
    }

    private static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
      
        do {
          k++;
          p *= Math.random();
        } while (p > L);
      
        return k - 1;
      }
}
