package org.apache.solr.client.solrj.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;


/**
 *  Overrides necessary methods from {@code SolrClient} for creating PrincipalUpdateRequest instances and route the documents
 *  to the shard leaders in the solr cloud.
 *   
 * @author venuyeredla
 *
 */
public abstract class PrincipalSolrClient extends SolrClient {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public UpdateResponse add(String collection, Collection<SolrInputDocument> docs, int commitWithinMs) throws SolrServerException, IOException {
		    PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.add(docs);
		    req.setCommitWithin(commitWithinMs);
		    return req.process(this, collection);
		  }
	
	  @Override
	  public UpdateResponse add(String collection, SolrInputDocument doc, int commitWithinMs) throws SolrServerException, IOException {
		PrincipalUpdateRequest req = new PrincipalUpdateRequest();
	    req.add(doc);
	    req.setCommitWithin(commitWithinMs);
	    return req.process(this, collection);
	  }
	  
	  @Override
	  public UpdateResponse add(String collection, Iterator<SolrInputDocument> docIterator)
		      throws SolrServerException, IOException {
		  PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.setDocIterator(docIterator);
		    return req.process(this, collection);
		  }
	
	  @Override
	  public UpdateResponse addBeans(String collection, final Iterator<?> beanIterator)
		      throws SolrServerException, IOException {
		  	PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.setDocIterator(new Iterator<SolrInputDocument>() {

		      @Override
		      public boolean hasNext() {
		        return beanIterator.hasNext();
		      }

		      @Override
		      public SolrInputDocument next() {
		        Object o = beanIterator.next();
		        if (o == null) return null;
		        return getBinder().toSolrInputDocument(o);
		      }

		      @Override
		      public void remove() {
		        beanIterator.remove();
		      }
		    });
		    return req.process(this, collection);
		  }
	  
	  @Override
	  public UpdateResponse commit(String collection, boolean waitFlush, boolean waitSearcher)
		      throws SolrServerException, IOException {
		    return new PrincipalUpdateRequest()
		        .setAction(PrincipalUpdateRequest.ACTION.COMMIT, waitFlush, waitSearcher)
		        .process(this, collection);
		  }
	  @Override
	  public UpdateResponse commit(String collection, boolean waitFlush, boolean waitSearcher, boolean softCommit)
		      throws SolrServerException, IOException {
		    return new PrincipalUpdateRequest()
		        .setAction(PrincipalUpdateRequest.ACTION.COMMIT, waitFlush, waitSearcher, softCommit)
		        .process(this, collection);
		  }
	  
	  @Override
	  public UpdateResponse optimize(String collection, boolean waitFlush, boolean waitSearcher, int maxSegments)
		      throws SolrServerException, IOException {
		    return new PrincipalUpdateRequest()
		        .setAction(PrincipalUpdateRequest.ACTION.OPTIMIZE, waitFlush, waitSearcher, maxSegments)
		        .process(this, collection);
		  }
	  
	  @Override
	  public UpdateResponse rollback(String collection) throws SolrServerException, IOException {
		    return new PrincipalUpdateRequest().rollback().process(this, collection);
		  }
	  
	  @Override
	  public UpdateResponse deleteById(String collection, String id, int commitWithinMs) throws SolrServerException, IOException {
		  PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.deleteById(id);
		    req.setCommitWithin(commitWithinMs);
		    return req.process(this, collection);
		  }

	  @Override
	  public UpdateResponse deleteById(String collection, List<String> ids, int commitWithinMs) throws SolrServerException, IOException {
		  	PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.deleteById(ids);
		    req.setCommitWithin(commitWithinMs);
		    return req.process(this, collection);
		  }
	  
	  @Override
	  public UpdateResponse deleteByQuery(String collection, String query, int commitWithinMs) throws SolrServerException, IOException {
		  	PrincipalUpdateRequest req = new PrincipalUpdateRequest();
		    req.deleteByQuery(query);
		    req.setCommitWithin(commitWithinMs);
		    return req.process(this, collection);
		  }
	  
}
