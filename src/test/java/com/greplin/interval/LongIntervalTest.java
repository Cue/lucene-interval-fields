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

  @Test
  public void testEquals() throws Exception {
    Assert.assertEquals(new LongInterval(1, 100), new LongInterval(1, 100));
    Assert.assertFalse(new LongInterval(1, 100).equals(new LongInterval(5, 100)));
    Assert.assertFalse(new LongInterval(1, 100).equals(new LongInterval(1, 500)));
    Assert.assertFalse(new LongInterval(1, 100).equals(new LongInterval(5, 500)));
  }

}
