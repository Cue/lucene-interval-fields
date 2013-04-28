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
 * Class to represent a numeric interval.
 */
class IntervalSegment {
  /**
   * The start of the interval segment.
   */
  private final long start;

  /**
   * The number of bits the segment covers.
   */
  private final int shift;


  /**
   * Creates a new segment.
   * @param start the start of the segment
   * @param shift the number of bits
   */
  public IntervalSegment(final long start, final int shift) {
    this.start = start;
    this.shift = shift;
  }


  /**
   * Gets the start of the segment.
   * @return the start of the segment
   */
  public final long getStart() {
    return this.start;
  }


  /**
   * Gets the number of bits the segment covers.
   * @return the number of bits
   */
  public final int getShift() {
    return this.shift;
  }
}
