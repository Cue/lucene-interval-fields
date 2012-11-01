package com.greplin.interval;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the long interval class.
 */
public class LongIntervalTest {

  private void assertParse(String string, long start, long end) {
    LongInterval result = LongInterval.valueOf(string);
    Assert.assertEquals(start, result.getStart());
    Assert.assertEquals(end, result.getEnd());
  }

  @Test
  public void testNegativeNumbers() throws Exception {
    assertParse("-174502800--174416400", -174502800, -174416400);
    assertParse("-174502800-150", -174502800, 150);
    assertParse("-2--1", -2, -1);
  }

}
