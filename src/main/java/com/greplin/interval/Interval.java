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

/**
 * Represents a numeric interval.
 * @param <T> the type of number in the interval
 */
public class Interval<T extends Number> {
  /**
   * The start of the interval.
   */
  private final T start;

  /**
   * The end of the interval.
   */
  private final T end;


  /**
   * Constructs an interval.
   * @param start the start of the interval
   * @param end the end of the interval
   */
  public Interval(final T start, final T end) {
    this.start = start;
    this.end = end;
  }


  /**
   * Gets the start of the interval.
   * @return the start of the interval
   */
  public final T getStart() {
    return start;
  }

  /**
   * Gets the end of the interval.
   * @return the end of the interval
   */
  public final T getEnd() {
    return end;
  }
}
