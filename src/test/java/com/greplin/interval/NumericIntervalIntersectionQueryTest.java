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

import org.apache.lucene.search.IndexSearcher;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the interval intersection query.
 */
public class NumericIntervalIntersectionQueryTest extends BaseIntervalQueryTest {

  @Test
  public void testBasics() throws IOException {
    addDocument(1, 1000, 2000);
    addDocument(2, 900, 1100);

    IndexSearcher searcher = getSearcher();

    assertSearch(searcher, 800, 2200, 1, 2);
    assertSearch(searcher, 1000, 2200, 1, 2);
    assertSearch(searcher, 1200, 2200, 1);
    assertSearch(searcher, 2000, 2001, 1);
    assertSearch(searcher, 2001, 2002);
    assertSearch(searcher, 2001, 2200);
    assertSearch(searcher, 899, 900, 2);
    assertSearch(searcher, 900, 999, 2);
    assertSearch(searcher, 1000, 1001, 1, 2);
    assertSearch(searcher, 1, 899);
    assertSearch(searcher, 898, 899);
    assertSearch(searcher, 1500, 1550, 1);
  }

  @Test
  public void testLowPrecisionIntervals() throws IOException {
    // Regression test: this previously failed because 0 - 4096 and other
    // large ranges can skip over smaller contained ranges.  For example,
    // 0 - 4096 => (start:0, shift:12)
    // and
    // 900 - 110 => (start:16, shift: 4)

    addDocument(10, 0, 16);
    addDocument(11, 16, 32);
    addDocument(12, 4064, 4080);
    addDocument(13, 4080, 4096);

    addDocument(20, 0, 256);
    addDocument(21, 256, 512);
    addDocument(22, 3584, 3840);
    addDocument(23, 3840, 4096);

    for (int i = 0; i < 100; i++) {
      // Add a bunch of garbage.
      addDocument(100 + i, 10000 + 11 * i, 10000 + 12 * i);
      addDocument(200 + i, -10000 + 11 * i, -10000 + 12 * i);
    }

    assertSearch(getSearcher(), 0, 4096, 10, 11, 12, 13, 20, 21, 22, 23);
    assertSearch(getSearcher(), 0, 256, 10, 11, 20, 21);
    assertSearch(getSearcher(), 0, 16, 10, 11, 20);

    assertSearch(getSearcher(), 3840, 4096, 12, 13, 22, 23);
    assertSearch(getSearcher(), 4080, 4096, 12, 13, 23);
  }

  protected void assertSearch(IndexSearcher searcher, long start, long end, Integer... expectedResults)
      throws IOException {
    assertSearch(searcher, new NumericIntervalIntersectionQuery("time", start, end), expectedResults);
  }

}
