# opt-plugin
Open Transportation Models - Sample plugins

**Run without plugin**
1. java -jar <otm-sim jar file> -run config/intersection.xml noplugin config/output_request.xml output 0 2 100 ctm 

**Run with plugin**
1. mvn install
2. java -jar <otm-sim jar file> -run config/intersection_plugin.xml noplugin config/output_request.xml output 0 2 100 ctm 
