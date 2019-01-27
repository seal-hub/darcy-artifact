package se.omegapoint.utils.internal;

public class StringUtils {

  public static int integerAtPosition(final String value, final int i) {
    return Integer.parseInt(value.substring(i, i + 1));
  }
}
