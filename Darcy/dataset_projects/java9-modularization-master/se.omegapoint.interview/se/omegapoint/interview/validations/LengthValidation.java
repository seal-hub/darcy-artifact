package se.omegapoint.interview.validations;

import java.util.Optional;
import se.omegapoint.interview.ValidationFailure;

public class LengthValidation implements Validation {

  @Override
  public Optional<ValidationFailure> validate(final String personNumber) {
    return personNumber.length() != 12 ? Optional.of(new ValidationFailure("Length must be 12!"))
                                       : Optional.empty();
  }
}
