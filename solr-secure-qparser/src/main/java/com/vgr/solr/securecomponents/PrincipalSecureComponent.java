package com.vgr.solr.securecomponents;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This component can be configured in solrconfig.xml file. 
 * Reads user parameter from query and adds Graph query filter for security trimming.
 * @author venugopal
 *
 */
public class PrincipalSecureComponent extends SearchComponent{
	
	public static final String COMPONENT_NAME = "principal";
	private String ADMIN="qsadmin";
	private String USER="user";
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
/*
	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
		  	SolrQueryRequest req = rb.req;
		    SolrParams params = req.getParams();
			if (!params.getBool(COMPONENT_NAME, true)) {
		      return;
		    }
		    String user=params.get(USER);
		    System.out.println("Query from user : "+user );
		    LOG.info("Query from user : "+user);
		    if(user!=null && user.equals(ADMIN)) {
		    	return ;
		    }else if(user!=null && !user.equals("")) {
		    	LOG.info("Query string : "+rb.getQueryString());
		    	String secFq="{!join from=docid to=id}{!join from=name to=accessList}{!graph from=name to=parents returnRoot=true }name:\""+user+"\"";
		    	ModifiableSolrParams modifiableSolrParams=new ModifiableSolrParams(params);
		    	String existedFq=modifiableSolrParams.get(CommonParams.FQ);
		    	modifiableSolrParams.add(CommonParams.FQ, existedFq,secFq);
		    }else {
		    	throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,"user parameter must be supplied in query to get the search results. ");
		    }
	}*/

	
	@Override
	public void prepare(ResponseBuilder rb) throws IOException {
		  	SolrQueryRequest req = rb.req;
		    SolrParams params = req.getParams();
			if (!params.getBool(COMPONENT_NAME, true)) {
		      return;
		    }
		    String user=params.get(USER);
		    System.out.println("Query from user : "+user );
		    LOG.info("Query from user : "+user);
		    if(user!=null && user.equals(ADMIN)) {
		    	return ;
		    }else if(user!=null && !user.equals("")) {
		    	LOG.info("Query string : "+rb.getQueryString());
		    	String secFq="{!join from=docid to=id}{!join from=name to=accessList}{!graph from=name to=parents returnRoot=true }name:\""+user+"\"";
		    	  try {
		    	  List<Query> filters=rb.getFilters();
		    	  if(filters==null) {
		    		  filters=new ArrayList<>();
		    	  }
		    	  QParser fqp = QParser.getParser(secFq, req);
		          fqp.setIsFilter(true);
		          filters.add(fqp.getQuery());
		          rb.setFilters(filters);
		          for (Query query : filters) {
		        	  LOG.info("Filter Query  : "+query.toString());
				  }
		          
		    	  } catch (SyntaxError e) {
		    	      throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e);
		    	    }
		    	
		    }else {
		    	throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,"user parameter must be supplied in query to get the search results. ");
		    }
	}

	
	
	@Override
	public void process(ResponseBuilder rb) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Adds security filter for each query except for admin";
	}

}
