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

import org.apache.lucene.search.Searcher;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the interval intersection query.
 */
public class NumericIntervalIntersectionQueryTest extends BaseIntervalQueryTest {

  @Before
  public void setUp() throws IOException {
    super.setUp();
    addDocument(1, 1000, 2000);
    addDocument(2, 900, 1100);
  }

  @Test
  public void testBasics() throws IOException {
    Searcher searcher = getSearcher();

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

  protected void assertSearch(Searcher searcher, long start, long end, Integer... expectedResults)
      throws IOException {
    assertSearch(searcher, new NumericIntervalIntersectionQuery("time", start, end), expectedResults);
  }

}
