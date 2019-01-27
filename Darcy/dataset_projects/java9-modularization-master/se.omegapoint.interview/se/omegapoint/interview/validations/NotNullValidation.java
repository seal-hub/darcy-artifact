package se.omegapoint.interview.validations;

import java.util.Optional;
import se.omegapoint.interview.ValidationFailure;

public class NotNullValidation implements Validation {

  @Override
  public Optional<ValidationFailure> validate(final String personNumber) {
    return personNumber == null ? Optional.of(new ValidationFailure("Number was null!")) : Optional.empty();
  }
}
