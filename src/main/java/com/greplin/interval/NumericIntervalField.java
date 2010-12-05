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
import com.sun.tools.javac.util.Pair;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.AbstractField;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.NumericUtils;

import java.io.Reader;

/**
 * Field that stores a numeric interval.
 */
public class NumericIntervalField extends AbstractField {
  static final int DEFAULT_PRECISION_STEP = 4;

  private final NumericIntervalTokenStream tokenStream;

  public NumericIntervalField(String name, boolean index, long min, long max) {
    this(name, index, min, max, DEFAULT_PRECISION_STEP);
  }

  public NumericIntervalField(String name, boolean index, long min, long max, int precisionStep) {
    super(name,
        Field.Store.NO,
        index ? Field.Index.ANALYZED_NO_NORMS : Field.Index.NO,
        Field.TermVector.NO);
    setOmitTermFreqAndPositions(true);

    final ImmutableList.Builder<Pair<Long, Integer>> segmentsBuilder = ImmutableList.builder();
    NumericUtils.splitLongRange(new NumericUtils.LongRangeBuilder() {
      @Override
      public void addRange(long min, long max, int shift) {
        while (min <= max) {
          segmentsBuilder.add(Pair.of(min, shift));
          min += 1 << shift;
        }
      }
    }, precisionStep, min, max);
    ImmutableList<Pair<Long, Integer>> segments = segmentsBuilder.build();

    tokenStream = new NumericIntervalTokenStream(segments);
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
    return this.isIndexed() ? tokenStream : null;
  }

  public static NumericIntervalField fromString(String name, boolean index, String rangeString) {
    String[] parts = rangeString.split("-");
    return new NumericIntervalField(name, index, Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim()));
  }
}
