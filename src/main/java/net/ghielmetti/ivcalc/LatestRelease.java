package net.ghielmetti.ivcalc;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class retrieves the latest release of ivCalc from the GitHub repository.
 *
 * @author Leopoldo Ghielmetti
 */
public class LatestRelease {
  private static final Logger LOGGER = LoggerFactory.getLogger(LatestRelease.class);

  private LatestRelease() {
  }

  /**
   * Returns the latest version of the program.
   *
   * @return the version tag name.
   */
  public static String getLatestVersion() {
    String version = null;

    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet("https://api.github.com/repos/leopoldog/ivCalc/releases/latest");

      ResponseHandler<JSONObject> handler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status <= 300 && response.getEntity() != null) {
          return new JSONObject(EntityUtils.toString(response.getEntity()));
        }
        throw new ClientProtocolException("Unexpected response status: " + status);
      };

      JSONObject responseBody = client.execute(httpGet, handler);
      version = responseBody.getString("tag_name");
    } catch (IOException e) {
      LOGGER.warn("Unable to check for updates!");
    }

    return version;
  }
}
