import java.net.URL;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class RunJetty
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        String dburl = System.getenv("DATABASE_URL");
        if(dburl!=null) {
        	String[] s = dburl.split("://");
        	String[] s2 = s[1].split("@");
        	String[] s3 = s2[0].split(":");
        	System.setProperty("database.url","jdbc:postgresql://"+s2[1]+"?user="+s3[0]+"&password="+s3[1]);
        	System.out.println("Setting system property database.url = "+System.getProperty("database.url"));
        }
        Connector connector = new SelectChannelConnector();
        connector.setPort(Integer.getInteger("jetty.port",8080).intValue());
        server.setConnectors(new Connector[] { connector });
	if(args.length<1) {
            System.out.println("Missing required argument: path_to_war_file");
            System.exit(1);
        }

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(args[0]);

        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
