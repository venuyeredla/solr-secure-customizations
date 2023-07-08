package org.apache.solr.update.processor;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class PrincipalLocalUpdateProcessorFactory extends UpdateRequestProcessorFactory 
implements DistributingUpdateProcessorFactory {
	
	
	  /**
	   * By default, the {@link DistributedUpdateProcessor} is extremely conservative in the list of request 
	   * params that will be copied/included when updates are forwarded to other nodes.  This method may be 
	   * used by any {@link UpdateRequestProcessorFactory#getInstance} call to annotate a 
	   * SolrQueryRequest with the names of parameters that should also be forwarded.
	   */
	  @SuppressWarnings("unchecked")
	  public static void addParamToDistributedRequestWhitelist(final SolrQueryRequest req, final String... paramNames) {
	    Set<String> whitelist = (Set<String>) req.getContext()
	        .computeIfAbsent(PrincipalLocalUpdateProcessor.PARAM_WHITELIST_CTX_KEY, key -> new TreeSet<>());
	    Collections.addAll(whitelist, paramNames);
	  }
	  

	  @Override
	  public UpdateRequestProcessor getInstance(SolrQueryRequest req,SolrQueryResponse rsp, UpdateRequestProcessor next) {
	    // note: will sometimes return DURP (no overhead) instead of wrapping
		  System.out.println("Create Qualcomupdated processor");
	    return TimeRoutedAliasUpdateProcessor.wrap(req, rsp, new PrincipalLocalUpdateProcessor(req, rsp, next));
	  }
	
}
