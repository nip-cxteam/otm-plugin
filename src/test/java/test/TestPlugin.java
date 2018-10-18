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
            API api = OTM.load("config/line_controller.xml",sim_dt,true,"ctm");
            api.run("plugin", out_request_file,output_folder,start_time,duration);
        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

    @Test
    public void run_no_plugin(){
        try {
            API api = OTM.load("config/intersection.xml",sim_dt,true,"ctm");
            api.run("no_plugin", out_request_file,output_folder,start_time,duration);
        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

    @Test
    public void run_line_plugin(){
        try {
            API api = OTM.load("C:\\Users\\gomes\\Desktop\\chester\\line.xml",sim_dt,true,"ctm");

            // Output requests .....................
            api.request_links_flow(null, api.get_link_ids(), sim_dt);
            api.request_links_veh(null, api.get_link_ids(), sim_dt);

            api.request_controller(1L);
            api.request_actuator(1L);

            // Run .................................
            api.run(0,duration);

            // Print output .........................
            String outfolder = "C:/Users/gomes/Desktop/chester/";
            for(AbstractOutput output :  api.get_output_data()){

//                if (output instanceof EventsActuator)
//                    ((EventsActuator) output).plot(String.format("%sactuator%d.png",outfolder,((EventsActuator) output).actuator_id));
//
//                if (output instanceof EventsController)
//                    ((EventsController) output).plot(String.format("%scontroller%d.png",outfolder,((EventsController) output).controller_id));

                if (output instanceof LinkFlow)
                    ((LinkFlow) output).plot_for_links(null,String.format("%sflow.png",outfolder));

                if (output instanceof LinkVehicles)
                    ((LinkVehicles) output).plot_for_links(null,String.format("%sveh.png",outfolder));

            }

        } catch (OTMException e) {
            System.out.print(e);
            fail();
        }
    }

}
