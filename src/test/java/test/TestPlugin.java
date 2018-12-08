package test;

import api.API;
import error.OTMException;
import org.junit.Test;
import output.*;
import runner.OTM;

import static org.junit.Assert.fail;

public class TestPlugin {

    String out_request_file = "config/output_request.xml";
    String output_folder = "output/";
    float start_time = 0f;
    float duration = 3600f;
    float sim_dt = 1f;

    @Test
    public void run_plugin(){
        try {
            API api = OTM.load("config/line_controller.xml",true);
            api.run("plugin", out_request_file,output_folder,start_time,duration);
        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

    @Test
    public void run_pedestrian_plugin(){
        try {
            API api = OTM.load("config/line_controller_pedestrian.xml",true);
            api.run("plugin", out_request_file,output_folder,start_time,duration);
        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

    @Test
    public void run_no_plugin(){
        try {
            API api = OTM.load("config/intersection.xml",true);
            api.run("no_plugin", out_request_file,output_folder,start_time,duration);
        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

}
