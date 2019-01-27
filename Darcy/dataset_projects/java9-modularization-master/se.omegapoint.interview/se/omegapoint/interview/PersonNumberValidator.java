package se.omegapoint.interview;

import java.util.Arrays;
import java.util.Optional;
import se.omegapoint.interview.validations.ChecksumValidation;
import se.omegapoint.interview.validations.DigitValidation;
import se.omegapoint.interview.validations.LengthValidation;
import se.omegapoint.interview.validations.NotNullValidation;

public class PersonNumberValidator {

  private static Validator validator = new Validator(Arrays.asList(
      new NotNullValidation(),
      new LengthValidation(),
      new DigitValidation(),
      new ChecksumValidation()
  ));

  public static void main(String... args) {
    System.out.println("Welcome to Omegapoint Person Number Validator!");

    if (args.length == 0) {
      System.err.println("At least one person number must be supplied.");
      return;
    }

    for (String arg : args) {
      checkPersonNumber(arg);
    }
  }

  private static void checkPersonNumber(final String personNumber) {
    Optional<ValidationFailure> failure = validator.validate(personNumber);

    if (failure.isPresent()) {
      System.err.println(
          "Invalid person number " + personNumber + ". Reason: " + failure.get().message);
    } else {
      System.out.println("Valid person number: " + personNumber);
    }
  }
}