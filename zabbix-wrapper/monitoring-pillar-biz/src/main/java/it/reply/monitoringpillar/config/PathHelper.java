package it.reply.monitoringpillar.config;

import java.io.File;

/**
 * This classes is an helper to get the paths to the folder of various kind of resources.
 * 
 */
public class PathHelper {

  public enum ResourceType {
    CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES("var-configs-profiles/");

    private final String subPath;

    private ResourceType(String subPath) {
      this.subPath = subPath;
    }

    public String getSubPath() {
      return subPath;
    }

    @Override
    public String toString() {
      return subPath;
    }
  }

  private PathHelper() {
  }

  /**
   * Get base Path.
   * 
   * @return the path of the folder containing all the resources (Unix encoded).
   */
  public static String getBaseResourcesPath() {
    String resourcePath =
        PathHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "/";

    // Fix for windows filesystem
    if (File.separatorChar == '\\') {
      resourcePath = resourcePath.substring(1);
    }

    return resourcePath;
  }

  /**
   * 
   * @param type
   *          the resource type to get the path for.
   * @return the path of the folder containing the given resource type.
   */
  public static String getResourcesPath(ResourceType type) {
    return getBaseResourcesPath() + type.getSubPath();
  }
}
