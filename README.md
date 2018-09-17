# opt-plugin
Open Transportation Models - Sample plugins

**Run without plugin**
1. java -jar <otm-sim jar file> -run config/intersection.xml noplugin config/output_request.xml output 0 2 100 ctm 

**Run with plugin**
1. In config/intersection_plugin.xml, replace <your otm-plugin-1.0-SNAPSHOT.jar> with the location of your otm plugin jar file.
2. mvn install
3. java -jar <otm-sim jar file> -run config/intersection_plugin.xml plugin config/output_request.xml output 0 2 100 ctm 
