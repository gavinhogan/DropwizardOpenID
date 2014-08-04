package uk.co.froot.demo.openid;


//import io.dropwizard.configuration;
//import io.dropwizard.assets.AssetsBundle;
//import io.dropwizard.config.Bootstrap;
//import io.dropwizard.config.Environment;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewMessageBodyWriter;
import org.eclipse.jetty.server.session.SessionHandler;
import uk.co.froot.demo.openid.auth.openid.OpenIDAuthenticator;
import uk.co.froot.demo.openid.auth.openid.OpenIDRestrictedToProvider;
import uk.co.froot.demo.openid.model.security.User;
import uk.co.froot.demo.openid.resources.PublicHomeResource;

/**
 * <p>Service to provide the following to application:</p>
 * <ul>
 * <li>Provision of access to resources</li>
 * </ul>
 * <p>Use <code>java -jar mbm-develop-SNAPSHOT.jar server openid-demo.yml</code> to start the demo</p>
 *
 * @since 0.0.1
 *         
 */
public class OpenIDDemoService extends Application<OpenIDDemoConfiguration> {

  /**
   * Main entry point to the application
   *
   * @param args CLI arguments
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new OpenIDDemoService().run(args);
  }

  private OpenIDDemoService() {

  }

  @Override
  public void initialize(Bootstrap<OpenIDDemoConfiguration> openIDDemoConfigurationBootstrap) {

    // Bundles
    openIDDemoConfigurationBootstrap.addBundle(new AssetsBundle("/assets/images", "/images"));
    openIDDemoConfigurationBootstrap.addBundle(new AssetsBundle("/assets/jquery", "/jquery"));
    openIDDemoConfigurationBootstrap.addBundle(new ViewBundle());
  }

  @Override
  public void run(OpenIDDemoConfiguration openIDDemoConfiguration, Environment environment) throws Exception {
    // Configure authenticator
    OpenIDAuthenticator authenticator = new OpenIDAuthenticator();

    // Configure environment
    environment.jersey().packages("uk.co.froot.demo.openid.resources");

    // Health checks
    environment.healthChecks().register("open-id-service", new uk.co.froot.demo.openid.health.OpenIdDemoHealthCheck());

    // Providers
    //environment.jersey().register(new ViewMessageBodyWriter());
    environment.jersey().register(new OpenIDRestrictedToProvider<User>(authenticator, "OpenID"));

    // Session handler
    environment.servlets().setSessionHandler(new SessionHandler());  }
}
