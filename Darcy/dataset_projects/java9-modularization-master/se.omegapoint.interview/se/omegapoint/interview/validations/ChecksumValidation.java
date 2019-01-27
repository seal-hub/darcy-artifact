package se.omegapoint.interview.validations;

import java.util.Optional;
import se.omegapoint.interview.ValidationFailure;
import se.omegapoint.utils.calculations.ChecksumCalculator;

public class ChecksumValidation implements Validation {

  private static final Integer CHECKSUM_POS = 11;

  @Override
  public Optional<ValidationFailure> validate(final String personNumber) {
    final Integer checksum = ChecksumCalculator.calculateChecksum(personNumber);
    return checksum == integerAtPosition(personNumber, CHECKSUM_POS)
           ? Optional.empty()
           : Optional.of(new ValidationFailure("Invalid checksum, expected " + checksum));
  }

  /**
   * Question: Why can't we access se.omegapoint.utils.internal.StringUtils?
   * The functionality is already there!
   */
  private static int integerAtPosition(final String value, final int i) {
    return Integer.parseInt(value.substring(i, i + 1));
  }
}