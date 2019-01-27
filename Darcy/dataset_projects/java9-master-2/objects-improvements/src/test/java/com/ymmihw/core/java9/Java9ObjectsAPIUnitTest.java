package com.ymmihw.core.java9;

import static java.util.Collections.emptyList;
import static java.util.Objects.checkFromIndexSize;
import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.checkIndex;
import static java.util.Objects.requireNonNullElse;
import static java.util.Objects.requireNonNullElseGet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import java.util.List;
import org.junit.Test;

public class Java9ObjectsAPIUnitTest {

  private List<String> aMethodReturningNullList() {
    return null;
  }

  @Test
  public void givenNullObject_whenRequireNonNullElse_thenElse() {
    List<String> aList = requireNonNullElse(aMethodReturningNullList(), emptyList());
    assertThat(aList, equalTo(emptyList()));
  }

  private List<String> aMethodReturningNonNullList() {
    return List.of("item1", "item2");
  }

  @Test
  public void givenObject_whenRequireNonNullElse_thenObject() {
    List<String> aList = requireNonNullElse(aMethodReturningNonNullList(), emptyList());
    assertThat(aList, equalTo(List.of("item1", "item2")));
  }

  @Test(expected = NullPointerException.class)
  public void givenNull_whenRequireNonNullElse_thenException() {
    requireNonNullElse(null, null);
  }

  @Test
  public void givenObject_whenRequireNonNullElseGet_thenObject() {
    List<String> aList = requireNonNullElseGet(null, List::of);
    assertThat(aList, equalTo(List.of()));
  }

  @Test
  public void givenNumber_whenInvokeCheckIndex_thenNumber() {
    int length = 5;
    assertThat(checkIndex(4, length), equalTo(4));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void givenOutOfRangeNumber_whenInvokeCheckIndex_thenException() {
    int length = 5;
    checkIndex(5, length);
  }


  @Test
  public void givenSubRange_whenCheckFromToIndex_thenNumber() {
    int length = 6;
    assertThat(checkFromToIndex(2, length, length), equalTo(2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void givenInvalidSubRange_whenCheckFromToIndex_thenException() {
    int length = 6;
    checkFromToIndex(2, 7, length);
  }


  @Test
  public void givenSubRange_whenCheckFromIndexSize_thenNumber() {
    int length = 6;
    assertThat(checkFromIndexSize(2, 3, length), equalTo(2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void givenInvalidSubRange_whenCheckFromIndexSize_thenException() {
    int length = 6;
    checkFromIndexSize(2, 6, length);
  }
}
