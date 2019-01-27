package com.ymmihw.core.java9;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.junit.Test;

public class VariableHandlesTest {

  public int publicTestVariable = 1;
  private int privateTestVariable = 1;
  public int variableToSet = 1;
  public int variableToCompareAndSet = 1;
  public int variableToGetAndAdd = 0;
  public byte variableToBitwiseOr = 0;

  @Test
  public void whenVariableHandleForPublicVariableIsCreated_ThenItIsInitializedProperly()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().in(VariableHandlesTest.class)
        .findVarHandle(VariableHandlesTest.class, "publicTestVariable", int.class);

    assertThat(publicIntHandle.coordinateTypes().size()).isEqualTo(1);
    assertThat(publicIntHandle.coordinateTypes().get(0)).isEqualTo(VariableHandlesTest.class);

  }

  @Test
  public void whenVariableHandleForPrivateVariableIsCreated_ThenItIsInitializedProperly()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle privateIntHandle =
        MethodHandles.privateLookupIn(VariableHandlesTest.class, MethodHandles.lookup())
            .findVarHandle(VariableHandlesTest.class, "privateTestVariable", int.class);

    assertThat(privateIntHandle.coordinateTypes().size()).isEqualTo(1);;
    assertThat(privateIntHandle.coordinateTypes().get(0)).isEqualTo(VariableHandlesTest.class);

  }

  @Test
  public void whenVariableHandleForArrayVariableIsCreated_ThenItIsInitializedProperly()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle arrayVarHandle = MethodHandles.arrayElementVarHandle(int[].class);

    assertThat(arrayVarHandle.coordinateTypes().size()).isEqualTo(2);
    assertThat(arrayVarHandle.coordinateTypes().get(0)).isEqualTo(int[].class);
  }

  @Test
  public void givenVarHandle_whenGetIsInvoked_ThenValueOfVariableIsReturned()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().in(VariableHandlesTest.class)
        .findVarHandle(VariableHandlesTest.class, "publicTestVariable", int.class);

    assertThat((int) publicIntHandle.get(this)).isEqualTo(1);
  }

  @Test
  public void givenVarHandle_whenSetIsInvoked_ThenValueOfVariableIsChanged()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().in(VariableHandlesTest.class)
        .findVarHandle(VariableHandlesTest.class, "variableToSet", int.class);
    publicIntHandle.set(this, 15);

    assertThat((int) publicIntHandle.get(this)).isEqualTo(15);
  }


  @Test
  public void givenVarHandle_whenCompareAndSetIsInvoked_ThenValueOfVariableIsChanged()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().findVarHandle(VariableHandlesTest.class,
        "variableToCompareAndSet", int.class);
    boolean succeeded = publicIntHandle.compareAndSet(this, 1, 100);
    assertThat(succeeded).isTrue();
    assertThat((int) publicIntHandle.get(this)).isEqualTo(100);
  }

  @Test
  public void givenVarHandle_whenGetAndAddIsInvoked_ThenValueOfVariableIsChanged()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().in(VariableHandlesTest.class)
        .findVarHandle(VariableHandlesTest.class, "variableToGetAndAdd", int.class);
    int before = (int) publicIntHandle.getAndAdd(this, 200);

    assertThat(before).isEqualTo(0);
    assertThat((int) publicIntHandle.get(this)).isEqualTo(200);
  }

  @Test
  public void givenVarHandle_whenGetAndBitwiseOrIsInvoked_ThenValueOfVariableIsChanged()
      throws NoSuchFieldException, IllegalAccessException {
    VarHandle publicIntHandle = MethodHandles.lookup().in(VariableHandlesTest.class)
        .findVarHandle(VariableHandlesTest.class, "variableToBitwiseOr", byte.class);
    byte before = (byte) publicIntHandle.getAndBitwiseOr(this, (byte) 127);

    assertThat(before).isEqualTo((byte) 0);
    assertThat(variableToBitwiseOr).isEqualTo((byte) 127);
  }
}
