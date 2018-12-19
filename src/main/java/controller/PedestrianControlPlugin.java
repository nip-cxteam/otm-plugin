package controller;

import actuator.AbstractActuator;
import actuator.ActuatorFD;
// import actuator.ActuatorFD.FDCommand;
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
    float number_pedestrians = 0;
    FixedSensor first_sensor;

    public PedestrianControlPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);
        first_act_id = actuator_by_usage.get("first_actuator").getId();
        first_sensor = (FixedSensor) sensor_by_usage.get("first_sensor");
    }

    @Override
    public void initialize(Scenario scenario, float v)  {
    }

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {

        // keep adding pedestrians here
        this.number_pedestrians += getPoisson(3);
        if (timestamp==0)
            System.out.println("time\tnum_veh\tflow");
        System.out.println(String.format("%.0f\t%.2f\t%.2f", 
                        timestamp,
                        // number_pedestrians, 
                        first_sensor.get_vehicles(),
                        first_sensor.get_flow_vph()//3600*dt
                        ));

        ActuatorFD y = (ActuatorFD) actuator_by_usage.get("first_actuator");

        float vehicles = (float) first_sensor.get_link().get_veh();
        // float new_jam_density = 50f;
        // ActuatorFD.FDCommand z = y.new FDCommand(100f, 1000f, (vehicles>new_jam_density) ? vehicles : new_jam_density); //Set new FD to "half" of previous
        // System.out.println((vehicles>new_jam_density) ? vehicles : new_jam_density);

        ActuatorFD.FDCommand z = y.new FDCommand(50f, 1000f, 100f); //Set new FD to "half" of previous

        if(timestamp==1200f){
            command.put(first_act_id, z);
            System.out.println("=============================");
        }
        // if(timestamp>2400d)
        //     command.put(first_act_id,ActuatorFD.Color.green);

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
