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

import com.google.common.base.Objects;
import com.google.common.primitives.Doubles;

/**
 * Represents an interval with double values.
 */
public class DoubleInterval implements Comparable<DoubleInterval> {
  /**
   * The start of the interval.
   */
  private final double start;

  /**
   * The end of the interval.
   */
  private final double end;


  /**
   * Constructs an interval.
   * @param start the start of the interval
   * @param end the end of the interval
   */
  public DoubleInterval(final double start, final double end) {
    this.start = start;
    this.end = end;
  }


  /**
   * Gets the start of the interval.
   * @return the start of the interval
   */
  public final double getStart() {
    return this.start;
  }

  /**
   * Gets the end of the interval.
   * @return the end of the interval
   */
  public final double getEnd() {
    return this.end;
  }


  /**
   * Returns whether this interval contains the given point.
   * @param point the point to check.
   * @return whether this interval contains the given point.
   */
  public boolean contains(final double point) {
    return this.start <= point && this.end >= point;
  }


  /**
   * Returns whether this interval intersects the given interval.
   * @param other the interval to check.
   * @return whether this interval intersects the given interval.
   */
  public boolean intersects(final DoubleInterval other) {
    return other.contains(this.start)
        || other.contains(this.end)
        || this.contains(other.start);
  }


  /**
   * Parses a string into an interval.
   * @param value the string.
   * @return the interval.
   */
  public static DoubleInterval valueOf(final String value) {
    String trimmed = value.trim();
    int middle = trimmed.indexOf('-', 1);
    double start = Double.parseDouble(trimmed.substring(0, middle).trim());
    double end = Double.parseDouble(trimmed.substring(middle + 1).trim());
    return new DoubleInterval(start, end);
  }


  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DoubleInterval interval = (DoubleInterval) o;
    return this.end == interval.end && this.start == interval.start;
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(this.start, this.end);
  }


  @Override
  public int compareTo(final DoubleInterval other) {
    return Doubles.compare(this.start, other.start);
  }


  @Override
  public String toString() {
    return "DoubleInterval{" + this.start + '-' + this.end + '}';
  }

}
