package com.vgr.solr.qparsers;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;

public class SecureParser extends QParser {
	
	private String user=null; 
	

	public SecureParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
		user=null;
		if(localParams!=null) {
			user=params.get("user");
		}
		if(params!=null) {
			user=params.get("user");
		}
	}

	@Override
	public Query parse() throws SyntaxError {
		 Query query=null;
		 if(StringUtils.isNotEmpty(user)) {
			 new SyntaxError(" 'user' paramter is missing from query request ");
		 }else {
			 String secureFq="{!join from=docid to=id}{!join from=name to=accessList}{!graph from=name to=parents returnRoot=true }name:\""+user+"\"";
		 }
		return null;
	}



}
