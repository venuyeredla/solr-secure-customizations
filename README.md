"# Implementing document level security within solr".

It's well known that Full-text search library Lucene written in java and Solr search server built on top of Lucene not designed for document level security. In many organization there is for documents to be searchable with acls from orginal data source.

With recent versions of GraphQueryParser and JoinQueryParser it is possible to create inverted index for documents with attached ACL's.
