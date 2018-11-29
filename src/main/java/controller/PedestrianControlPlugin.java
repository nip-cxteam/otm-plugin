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
    int number_pedestrians = 0;
    FixedSensor first_sensor;

    public PedestrianControlPlugin(Scenario scenario, Controller jaxb_controller) throws OTMException {
        super(scenario, jaxb_controller);
        first_act_id = actuator_by_usage.get("first_actuator").getId();
        first_sensor = (FixedSensor) sensor_by_usage.get("first_sensor");
    }

    @Override
    public void initialize(Scenario scenario, float v)  {
        for (AbstractActuator x : actuator_by_usage.values()){
            ActuatorFD y = (ActuatorFD) x;
            System.out.println(y);
        }
        // this.command.put(first_act_id,ActuatorFD.Color.green);
        // this.command.put(second_act_id,ActuatorFD.Color.green);
    }

    @Override
    public void update_controller(Dispatcher dispatcher, float timestamp) throws OTMException {

        // keep adding pedestrians here
        this.number_pedestrians += getPoisson(3);
        // System.out.println("time\tnum_ped\tnum_veh");
        System.out.println(String.format("%f\t%d\t%f\t%f", 
                        timestamp,
                        number_pedestrians, 
                        first_sensor.get_vehicles(),
                        first_sensor.get_flow_vph()
                        ));

        ActuatorFD y = (ActuatorFD) actuator_by_usage.get("first_actuator");

        float vehicles = (float) first_sensor.get_link().get_veh();
        // float new_jam_density = 50f;
        // ActuatorFD.FDCommand z = y.new FDCommand(100f, 1000f, (vehicles>new_jam_density) ? vehicles : new_jam_density); //Set new FD to "half" of previous
        // System.out.println((vehicles>new_jam_density) ? vehicles : new_jam_density);

        ActuatorFD.FDCommand z = y.new FDCommand(100f, 2000f, 100f); //Set new FD to "half" of previous

        if(timestamp==540f)
            command.put(first_act_id, z);
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
