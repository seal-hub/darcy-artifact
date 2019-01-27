package se.omegapoint.interview;

import static se.omegapoint.utils.validation.Validate.notNull;

import java.util.List;
import java.util.Optional;
import se.omegapoint.interview.validations.Validation;

public class Validator {

  private final List<Validation> validations;

  public Validator(List<Validation> validations) {
    this.validations = notNull(validations);
  }

  public Optional<ValidationFailure> validate(final String personNumber) {
    for (Validation validation : validations) {
      Optional<ValidationFailure> failure = validation.validate(personNumber);
      if (failure.isPresent()) {
        return failure;
      }
    }
    return Optional.empty();
  }
}
