package se.omegapoint.interview;

import static se.omegapoint.utils.validation.Validate.notBlank;

public class ValidationFailure {

  public final String message;

  public ValidationFailure(final String message) {
    this.message = notBlank(message);
  }
}
