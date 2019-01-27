package se.omegapoint.utils.calculations;

import static se.omegapoint.utils.internal.StringUtils.integerAtPosition;

public class ChecksumCalculator {

  public static Integer calculateChecksum(String value) {
    int multiplier = 2;
    int sum = 0;
    for(int i = 2; i < value.length() - 1; ++i) {
      int multipliedValue = multiplier * integerAtPosition(value, i);
      sum += multipliedValue % 10 + multipliedValue / 10;
      multiplier = 3 - multiplier;
    }
    return 10 - (sum % 10);
  }
}
