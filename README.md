Lucene Interval Fields
======================

Store and query intervals in Lucene.
------------------------------------

### Creating documents

    Document doc = new Document();
    doc.add(new Field("name", "George Washington", Field.Store.YES, Field.Index.NO));
    doc.add(new NumericIntervalField("term", true, 1789, 1793));
    doc.add(new NumericIntervalField("term", true, 1793, 1797));
    indexWriter.addDocument(doc);

    doc = new Document();
    doc.add(new Field("name", "John Adams", Field.Store.YES, Field.Index.NO));
    doc.add(new NumericIntervalField("term", true, 1797, 1801));
    indexWriter.addDocument(doc);

    doc = new Document();
    doc.add(new Field("name", "Thomas Jefferson", Field.Store.YES, Field.Index.NO));
    doc.add(new NumericIntervalField("term", true, 1801, 1805));
    doc.add(new NumericIntervalField("term", true, 1805, 1809));
    indexWriter.addDocument(doc);

    ...


### Queries

	// List all presidents in office in the 1700s.
	searcher.search(new NumericIntervalIntersectionQuery("term", 1700, 1799), collector)
	
	// Who was president in 1804?
	searcher.search(new InNumericIntervalQuery("term", 1804), collector)


### Known limitations

* Only supports long values.
