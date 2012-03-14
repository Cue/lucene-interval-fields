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

import org.apache.lucene.analysis.NumericTokenStream;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import java.io.IOException;

/**
 * Query that finds intervals containing a given value.
 */
public final class InNumericIntervalQuery extends BooleanQuery {
  /**
   * The search value.
   */
  private final long value;

  /**
   * Creates a query to find intervals a number is in.
   * @param name The name of the field to search.
   * @param value The value to find containing intervals.
   */
  public InNumericIntervalQuery(final String name,
                                final long value) {
    this(name, value, NumericIntervalField.DEFAULT_PRECISION_STEP);
  }


  /**
   * Creates a query to find intervals a number is in.
   * @param name The name of the field to search.
   * @param value The search value.
   * @param precisionStep The precision step used when indexing the field.
   */
  public InNumericIntervalQuery(final String name,
                                final long value,
                                final int precisionStep) {
    super(true);
    this.value = value;

    TokenStream stream = new NumericTokenStream(precisionStep)
        .setLongValue(value);

    try {
      stream.reset();
      while (stream.incrementToken()) {
        this.add(
            new TermQuery(
                new Term(name,
                         stream.getAttribute(TermAttribute.class).term())),
            BooleanClause.Occur.SHOULD);
      }
    } catch (IOException e) {
      throw new IllegalStateException(
          "This should never happen - NumericTokenStream does no IO.");
    }
  }

  @Override
  public String toString(final String field) {
    return String.format("inInterval(%d, %s)", this.value, field);
  }

  @Override
  public int hashCode() {
    return (int) this.value;
  }

  @Override
  public boolean equals(final Object o) {
    return o != null
        && o.getClass() == this.getClass()
        && ((InNumericIntervalQuery) o).value == this.value;
  }
}
