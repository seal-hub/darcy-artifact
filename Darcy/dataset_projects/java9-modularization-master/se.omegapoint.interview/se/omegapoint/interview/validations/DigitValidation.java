package se.omegapoint.interview.validations;

import java.util.Optional;
import se.omegapoint.interview.ValidationFailure;

public class DigitValidation implements Validation {

  private static final String DIGIT_PATTERN = "[0-9]+";

  @Override
  public Optional<ValidationFailure> validate(final String personNumber) {
    return personNumber.matches(DIGIT_PATTERN) ? Optional.empty()
                                               : Optional.of(new ValidationFailure("Only digits allowed"));
  }
}
