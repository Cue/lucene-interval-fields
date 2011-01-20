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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.AbstractField;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.NumericUtils;

import java.io.Reader;

/**
 * Field that stores a numeric interval.
 */
public final class NumericIntervalField extends AbstractField {
  /**
   * The default precision step.
   */
  static final int DEFAULT_PRECISION_STEP = 4;

  /**
   * The stream of tokens used for the index.
   */
  private final NumericIntervalTokenStream tokenStreamValue;


  /**
   * Creates a new numeric interval field.
   * @param name The name of the field to index.
   * @param index Whether to index the field.
   * @param min The start of the interval.
   * @param max The end of the interval.
   */
  public NumericIntervalField(final String name,
                              final boolean index,
                              final long min,
                              final long max) {
    this(name, index, min, max, DEFAULT_PRECISION_STEP);
  }


  /**
   * Creates a new numeric interval field.
   * @param name The name of the field to index.
   * @param index Whether to index the field.
   * @param min The start of the interval.
   * @param max The end of the interval.
   * @param precisionStep The precision step used to control
   *                      index size vs query speed.
   */
  public NumericIntervalField(final String name,
                              final boolean index,
                              final long min,
                              final long max,
                              final int precisionStep) {
    super(name,
        Field.Store.NO,
        index ? Field.Index.ANALYZED_NO_NORMS : Field.Index.NO,
        Field.TermVector.NO);
    setOmitTermFreqAndPositions(true);

    final ImmutableList.Builder<IntervalSegment> segmentsBuilder =
        ImmutableList.builder();
    NumericUtils.splitLongRange(new NumericUtils.LongRangeBuilder() {
      @Override
      public void addRange(final long min, final long max, final int shift) {
        long currentMinimum = min;
        while (currentMinimum <= max) {
          segmentsBuilder.add(new IntervalSegment(currentMinimum, shift));
          currentMinimum += 1L << shift;
        }
      }
    }, precisionStep, min, max);
    ImmutableList<IntervalSegment> segments = segmentsBuilder.build();

    tokenStreamValue = new NumericIntervalTokenStream(segments);
  }

  @Override
  public String stringValue() {
    return null;
  }

  @Override
  public Reader readerValue() {
    return null;
  }

  @Override
  public TokenStream tokenStreamValue() {
    return this.isIndexed() ? tokenStreamValue : null;
  }


  /**
   * Splits a numeric range string in to the two numbers comprising it.
   * @param rangeString the range string
   * @return array of two numbers
   */
  @VisibleForTesting
  static long[] splitParts(final String rangeString) {
    String trimmed = rangeString.trim();
    int middle = trimmed.indexOf('-', 1);
    long start = Long.parseLong(trimmed.substring(0, middle).trim());
    long end = Long.parseLong(trimmed.substring(middle + 1).trim());
    return new long[]{start, end};
  }

  /**
   * Creates a numeric interval field from a string format.
   * @param name The name of the field to index.
   * @param index Whether to index the field.
   * @param rangeString The string of the form "a-b" to parse.
   * @return a new numeric interval field
   */
  public static NumericIntervalField fromString(final String name,
                                                final boolean index,
                                                final String rangeString) {
    long[] parts = splitParts(rangeString);
    return new NumericIntervalField(name, index, parts[0], parts[1]);
  }
}
