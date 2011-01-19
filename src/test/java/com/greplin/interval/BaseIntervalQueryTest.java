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

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.Set;

/**
 * Base class for interval query tests.
 */
public abstract class BaseIntervalQueryTest {
  private IndexWriter indexWriter;

  @Before
  public void setUp() throws IOException {
    RAMDirectory ramDirectory = new RAMDirectory();
    indexWriter = new IndexWriter(ramDirectory, new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
  }

  protected void addDocument(int id, Interval<Long> interval) throws IOException {
    addDocument(id, interval.getStart(), interval.getEnd());
  }

  protected void addDocument(int id, long start, long end) throws IOException {
    Document doc1 = new Document();
    doc1.add(new Field("id", String.valueOf(id), Field.Store.YES, Field.Index.NO));
    doc1.add(new NumericIntervalField("time", true, start, end));
    indexWriter.addDocument(doc1);
  }

  protected Searcher getSearcher() throws IOException {
    return new IndexSearcher(indexWriter.getReader());
  }

  protected void assertSearch(Searcher searcher, Query query, Integer... expectedResults)
      throws IOException {
    Set<Integer> expected = ImmutableSet.copyOf(expectedResults);

    TopDocs docs = searcher.search(query, 100);
    Set<Integer> actual = Sets.newHashSet();
    for (ScoreDoc scoreDoc : docs.scoreDocs) {
      Document doc = searcher.doc(scoreDoc.doc);
      actual.add(Integer.valueOf(doc.get("id")));
    }

    Assert.assertEquals(query + " should match " + expectedResults.toString(), expected, actual);
  }
}
