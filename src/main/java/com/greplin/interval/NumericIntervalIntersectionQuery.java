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

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;

/**
 * Query that finds intervals that intersect with the given interval.
 */
public class NumericIntervalIntersectionQuery extends BooleanQuery {
  private final long start;
  private final long end;

  public NumericIntervalIntersectionQuery(String name, long start, long end) {
    this(name, start, end, NumericIntervalField.DEFAULT_PRECISION_STEP);
  }

  public NumericIntervalIntersectionQuery(String name, long start, long end, int precisionStep) {
    super(true);
    this.start = start;
    this.end = end;

    this.add(
        NumericRangeQuery.newLongRange(name, precisionStep, start, end, true, true),
        BooleanClause.Occur.SHOULD);
    this.add(
        new InNumericIntervalQuery(name, end, precisionStep),
        BooleanClause.Occur.SHOULD);
    this.add(
        new InNumericIntervalQuery(name, start, precisionStep),
        BooleanClause.Occur.SHOULD);
  }

  @Override
  public String toString(String field) {
    return String.format("intersectsInterval(%d - %d, %s)", start, end, field);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(start).append(end).hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return o != null && o.getClass() == this.getClass() &&
        ((NumericIntervalIntersectionQuery) o).start == start &&
        ((NumericIntervalIntersectionQuery) o).end == end;
  }
}
