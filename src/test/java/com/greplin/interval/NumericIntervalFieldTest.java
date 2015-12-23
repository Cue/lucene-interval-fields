/*
 * Copyright 2010 The Lucene Interval Field Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greplin.interval;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the NumericIntervalField class.
 */
public class NumericIntervalFieldTest {
  @Test
  public void testSplitParts() {
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts("1-2"));
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts(" 1-2"));
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts("1 -2"));
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts("1- 2"));
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts("1- 2"));
    Assert.assertArrayEquals(new long[]{1, 2}, NumericIntervalField.splitParts(" 1 - 2 "));

    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts("-1-2"));
    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts(" -1-2"));
    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts("-1 -2"));
    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts("-1- 2"));
    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts("-1-2 "));
    Assert.assertArrayEquals(new long[]{-1, 2}, NumericIntervalField.splitParts(" -1 -2 "));

    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts("1--2"));
    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts(" 1--2"));
    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts("1 --2"));
    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts("1- -2"));
    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts("1--2 "));
    Assert.assertArrayEquals(new long[]{1, -2}, NumericIntervalField.splitParts(" 1 - -2 "));

    Assert.assertArrayEquals(new long[]{-1, -2}, NumericIntervalField.splitParts(" -1--2"));
    Assert.assertArrayEquals(new long[]{-1, -2}, NumericIntervalField.splitParts("-1 --2"));
    Assert.assertArrayEquals(new long[]{-1, -2}, NumericIntervalField.splitParts("-1- -2"));
    Assert.assertArrayEquals(new long[]{-1, -2}, NumericIntervalField.splitParts("-1--2 "));
    Assert.assertArrayEquals(new long[]{-1, -2}, NumericIntervalField.splitParts(" -1 - -2 "));

    Assert.assertArrayEquals(new long[]{-100, 21253456}, NumericIntervalField.splitParts("-100-21253456"));
  }

  @Test (expected=NumberFormatException.class)
  public void testEmptyNumbers() throws Exception {
    long[] result = NumericIntervalField.splitParts("null-null");
  }
}
