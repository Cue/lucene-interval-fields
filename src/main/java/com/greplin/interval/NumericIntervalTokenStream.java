/*
 * Copyright 2010 The Lucene Interval Field Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greplin.interval;

import com.google.common.collect.ImmutableList;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.NumericUtils;

import java.io.Serializable;

/**
 * Token stream for a numeric interval.
 */
final class NumericIntervalTokenStream
    extends TokenStream
    implements Serializable {
  /**
   * Segments that make up the interval.
   */
  private final ImmutableList<IntervalSegment> segments;

  /**
   * The term attribute.
   */
  private final TermAttribute termAtt = addAttribute(TermAttribute.class);

  /**
   * Current segment number.
   */
  private int i = 0;

  /**
   * Creates a token stream for a numeric interval.
   * @param segments The segments.
   */
  public NumericIntervalTokenStream(
      final ImmutableList<IntervalSegment> segments) {
    super();
    this.segments = segments;
  }

  @Override
  public void reset() {
    this.i = 0;
  }

  @Override
  public boolean incrementToken() {
    if (this.i >= this.segments.size()) {
      return false;
    }

    clearAttributes();
    IntervalSegment interval = this.segments.get(this.i);
    long value = interval.getStart();
    int shift = interval.getShift();
    final char[] buffer =
        this.termAtt.resizeTermBuffer(NumericUtils.BUF_SIZE_LONG);
    this.termAtt.setTermLength(
        NumericUtils.longToPrefixCoded(value, shift, buffer));
    this.i++;

    return true;
  }

  @Override
  public String toString() {
    return "(interval,parts=" + this.segments + ')';
  }

  @Override
  public int hashCode() {
    return this.segments.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    return obj != null && obj.getClass() == this.getClass()
        && this.segments.equals(((NumericIntervalTokenStream) obj).segments);
  }
}
