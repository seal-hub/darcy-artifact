package se.omegapoint.interview.validations;

import java.util.Optional;
import se.omegapoint.interview.ValidationFailure;

public interface Validation {

  Optional<ValidationFailure> validate(String personNumber);
}
