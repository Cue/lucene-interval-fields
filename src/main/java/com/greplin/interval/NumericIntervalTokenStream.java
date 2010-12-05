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
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.NumericUtils;

import java.io.Serializable;

/**
 * Token stream for a numeric interval.
 */
class NumericIntervalTokenStream extends TokenStream implements Serializable {
  private final ImmutableList<Pair<Long, Integer>> segments;

  private final TermAttribute termAtt = addAttribute(TermAttribute.class);

  private int i = 0;

  /**
   * Creates a token stream for a numeric interval.
   * @param segments The segments.
   */
  public NumericIntervalTokenStream(ImmutableList<Pair<Long, Integer>> segments) {
    super();
    this.segments = segments;
  }

  @Override
  public void reset() {
    i = 0;
  }

  @Override
  public boolean incrementToken() {
    if (i >= segments.size()) {
      return false;
    }

    clearAttributes();
    Pair<Long, Integer> interval = segments.get(i);
    long value = interval.fst;
    int shift = interval.snd;
    final char[] buffer = termAtt.resizeTermBuffer(NumericUtils.BUF_SIZE_LONG);
    termAtt.setTermLength(NumericUtils.longToPrefixCoded(value, shift, buffer));
    i++;

    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder("(interval,parts=").append(segments).append(')').toString();
  }

  @Override
  public int hashCode() {
    return segments.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && obj.getClass() == this.getClass() &&
        segments.equals(((NumericIntervalTokenStream) obj).segments);
  }
}
