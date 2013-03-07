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

import com.google.common.primitives.Longs;

/**
 * Represents an interval with long values.
 */
public class LongInterval
    extends NumericInterval<Long>
    implements Comparable<LongInterval> {
  /**
   * The start of the interval.
   */
  private final long start;

  /**
   * The end of the interval.
   */
  private final long end;


  /**
   * Constructs an interval.
   * @param start the start of the interval
   * @param end the end of the interval
   */
  public LongInterval(final long start, final long end) {
    this.start = start;
    this.end = end;
  }


  /**
   * Gets the start of the interval.
   * @return the start of the interval
   */
  public final long getStart() {
    return this.start;
  }

  /**
   * Gets the end of the interval.
   * @return the end of the interval
   */
  public final long getEnd() {
    return this.end;
  }


  @Override
  public Long getBoxedStart() {
    return this.start;
  }


  @Override
  public Long getBoxedEnd() {
    return this.end;
  }


  /**
   * Returns whether this interval contains the given point.
   * @param point the point to check.
   * @return whether this interval contains the given point.
   */
  public boolean contains(final long point) {
    return this.start <= point && this.end >= point;
  }


  /**
   * Returns whether this interval intersects the given interval.
   * @param other the interval to check.
   * @return whether this interval intersects the given interval.
   */
  public boolean intersects(final LongInterval other) {
    return other.contains(this.start)
        || other.contains(this.end)
        || this.contains(other.start);
  }


  /**
   * Parses a string into an interval.
   * @param value the string.
   * @return the interval.
   */
  public static LongInterval valueOf(final String value) {
    String trimmed = value.trim();
    int middle = trimmed.indexOf('-', 1);
    long start = Long.parseLong(trimmed.substring(0, middle).trim());
    long end = Long.parseLong(trimmed.substring(middle + 1).trim());
    return new LongInterval(start, end);
  }


  @Override
  public int compareTo(final LongInterval other) {
    return Longs.compare(this.start, other.start);
  }

}
