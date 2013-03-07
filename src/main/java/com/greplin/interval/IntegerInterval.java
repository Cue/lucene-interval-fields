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

import com.google.common.primitives.Ints;

/**
 * Represents an interval with integer values.
 */
public class IntegerInterval
    extends NumericInterval<Integer>
    implements Comparable<IntegerInterval> {

  /**
   * The start of the interval.
   */
  private final int start;

  /**
   * The end of the interval.
   */
  private final int end;


  /**
   * Constructs an interval.
   * @param start the start of the interval
   * @param end the end of the interval
   */
  public IntegerInterval(final int start, final int end) {
    this.start = start;
    this.end = end;
  }


  /**
   * Gets the start of the interval.
   * @return the start of the interval
   */
  public final int getStart() {
    return this.start;
  }

  /**
   * Gets the end of the interval.
   * @return the end of the interval
   */
  public final int getEnd() {
    return this.end;
  }


  @Override
  public Integer getBoxedStart() {
    return this.start;
  }


  @Override
  public Integer getBoxedEnd() {
    return this.end;
  }


  /**
   * Returns whether this interval contains the given point.
   * @param point the point to check.
   * @return whether this interval contains the given point.
   */
  public boolean contains(final int point) {
    return this.start <= point && this.end >= point;
  }


  /**
   * Returns whether this interval intersects the given interval.
   * @param other the interval to check.
   * @return whether this interval intersects the given interval.
   */
  public boolean intersects(final IntegerInterval other) {
    return other.contains(this.start)
        || other.contains(this.end)
        || this.contains(other.start);
  }


  /**
   * Parses a string into an interval.
   * @param value the string.
   * @return the interval.
   */
  public static IntegerInterval valueOf(final String value) {
    String trimmed = value.trim();
    int middle = trimmed.indexOf('-', 1);
    int start = Integer.parseInt(trimmed.substring(0, middle).trim());
    int end = Integer.parseInt(trimmed.substring(middle + 1).trim());
    return new IntegerInterval(start, end);
  }


  @Override
  public int compareTo(final IntegerInterval other) {
    return Ints.compare(this.start, other.start);
  }

}
