package org.occideas.security.model;

/*
 * Paths that need to be read only for READ_ONLY_USERS
 */
public enum ReadOnlyPaths {

  DEFAULT_CREATE("/create"),
  DEFAULT_UPDATE("/update"),
  DEFAULT_ADD("/add"),
  DEFAULT_DELETE("/delete"),
  DEFAULT_SAVE("/save");

  private String path;

  ReadOnlyPaths(String path) {
    this.path = path;
  }

  public static String[] paths() {
    ReadOnlyPaths[] readOnlyPath = values();
    String[] path = new String[readOnlyPath.length];

    for (int i = 0; i < readOnlyPath.length; i++) {
      path[i] = readOnlyPath[i].getPath();
    }

    return path;
  }

  public String getPath() {
    return path;
  }

}
