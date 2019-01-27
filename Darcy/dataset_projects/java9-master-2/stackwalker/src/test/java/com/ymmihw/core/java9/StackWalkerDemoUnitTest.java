package com.ymmihw.core.java9;

import org.junit.Test;

public class StackWalkerDemoUnitTest {

  @Test
  public void giveStalkWalker_whenWalkingTheStack_thenShowStackFrames() {
    new StackWalkerDemo().methodOne();
  }

  @Test
  public void giveStalkWalker_whenInvokingFindCaller_thenFindCallingClass() {
    new StackWalkerDemo().findCaller();
  }
}
