/*
 * Copyright 2012 The Lucene Interval Field Authors.
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

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FilteredTermEnum;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.util.NumericUtils;

import java.io.IOException;

/**
 * Query that matches intervals partially contained by the given interval.
 */
public final class NumericIntervalIntersectionQuery extends MultiTermQuery {

  /**
   * The name of the field.
   */
  private final String name;

  /**
   * The start of the interval.
   */
  private final long start;

  /**
   * The end of the interval.
   */
  private final long end;

  /**
   * The precision step used when indexing the field.
   */
  private final int precisionStep;


  /**
   * Creates a new numeric sub-interval query.
   * @param name The name of the field.
   * @param start The start of the interval to find sub-intervals in.
   * @param end The end of the interval to find sub-intervals in.
   */
  public NumericIntervalIntersectionQuery(final String name,
                                          final long start,
                                          final long end) {
    this(name, start, end, NumericIntervalField.DEFAULT_PRECISION_STEP);
  }


  /**
   * Creates a new numeric sub-interval query.
   * @param name The name of the field.
   * @param start The start of the interval to find sub-intervals in.
   * @param end The end of the interval to find sub-intervals in.
   * @param precisionStep The precision step used when indexing the field.
   */
  public NumericIntervalIntersectionQuery(final String name,
                                          final long start,
                                          final long end,
                                          final int precisionStep) {
    this.name = name.intern();
    this.start = start;
    this.end = end;
    this.precisionStep = precisionStep;
    setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
  }


  @Override
  protected FilteredTermEnum getEnum(final IndexReader reader)
      throws IOException {
    return new NumericIntervalIntersectionTermEnum(reader);
  }


  @Override
  public String toString(final String field) {
    return String.format("containedInInterval(%d - %d, %s)",
        this.start, this.end, field);
  }


  /**
   * Filtered TermEnum that returns terms that intersect this interval.
   */
  private final class NumericIntervalIntersectionTermEnum
      extends FilteredTermEnum {

    /**
     * Whether to end the enumeration at the next opportunity.
     */
    private boolean endEnum = false;


    /**
     * Whether to skip to the next precision value next time.
     */
    private boolean skip = false;


    /**
     * The current shift value.
     */
    private int shift = 0;


    /**
     * The Index Reader we are reading terms from.
     */
    private final IndexReader reader;


    /**
     * Creates a term enum matching intersected interval segments.
     * @param reader the index to filter terms from
     * @throws IOException if IO issues occur
     */
    private NumericIntervalIntersectionTermEnum(final IndexReader reader)
        throws IOException {
      this.reader = reader;
      setEnum(reader.terms(new Term(
          NumericIntervalIntersectionQuery.this.name,
          NumericUtils.longToPrefixCoded(
              NumericIntervalIntersectionQuery.this.start))));
    }


    @Override
    public boolean next() throws IOException {
      if (this.skip) {
        this.skip = false;
        this.shift += NumericIntervalIntersectionQuery.this.precisionStep;

        this.currentTerm = null;
        setEnum(this.reader.terms(new Term(
            NumericIntervalIntersectionQuery.this.name,
            NumericUtils.longToPrefixCoded(
                NumericIntervalIntersectionQuery.this.start, this.shift)
        )));
        return this.currentTerm != null;
      } else {
        return super.next();
      }
    }


    @Override
    protected boolean termCompare(final Term term) {
      if (term.field() != NumericIntervalIntersectionQuery.this.name) {
        this.endEnum = true;
        return false;
      }

      // Taken from NumericUtils.prefixCodedToLong
      final int shift = term.text().charAt(0) - NumericUtils.SHIFT_START_LONG;
      final long startOfRange = NumericUtils.prefixCodedToLong(term.text());
      final long endOfRange = startOfRange + (1 << shift) - 1;
      final boolean result =
          // The query interval contains the start of the indexed.
          (startOfRange >= NumericIntervalIntersectionQuery.this.start
           && startOfRange <= NumericIntervalIntersectionQuery.this.end)

          ||

          // The query interval contains the start of the indexed.
          (endOfRange >= NumericIntervalIntersectionQuery.this.start
           && endOfRange <= NumericIntervalIntersectionQuery.this.end)


          ||

          // The query interval is contained in the indexed interval.
          (startOfRange <= NumericIntervalIntersectionQuery.this.start
           && endOfRange >= NumericIntervalIntersectionQuery.this.end);

      this.skip = !result;
      return result;
    }


    @Override
    public float difference() {
      return 1.0f; // Just used as a boost and we don't care about scores.
    }


    @Override
    protected boolean endEnum() {
      return this.endEnum;
    }
  }
}
