package net.ghielmetti.pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionReader {
  private Properties p = new Properties();

  public VersionReader() {
    try (InputStream is = getClass().getResourceAsStream("/META-INF/maven/net.ghielmetti/pokemon/pom.properties")) {
      p.load(is);
    } catch (IOException e) {
      p.setProperty("version", "Unknown");
    }
  }

  public String getVersion() {
    return p.getProperty("version");
  }
}
