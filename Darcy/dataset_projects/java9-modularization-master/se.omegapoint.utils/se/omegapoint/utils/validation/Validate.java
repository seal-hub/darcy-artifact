package se.omegapoint.utils.validation;

public class Validate {

  public static <T> T notNull(final T object) {
    if (object == null) {
      throw new IllegalArgumentException("Null object!");
    }
    return object;
  }

  public static String notBlank(final String object) {
    notNull(object);
    if (object.trim().length() == 0) {
      throw new IllegalArgumentException("Blank value!");
    }
    return object;
  }
}
