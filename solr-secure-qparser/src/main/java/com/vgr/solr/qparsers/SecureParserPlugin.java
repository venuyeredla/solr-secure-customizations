package com.vgr.solr.qparsers;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;

public class SecureParserPlugin extends QParserPlugin{

	@Override
	public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {

		QParser secQParser=new QParser(qstr, localParams, params, req) {
			@Override
			public Query parse() throws SyntaxError {
				
				String user=null;
				if(localParams!=null) {
					user=params.get("user");
				}
				if(params!=null) {
					user=params.get("user");
				}
               if(StringUtils.isNotEmpty(user)) {
            	  
            	   
               }else {
            	   
               }
					System.out.println("From localparams user is :"+user );
				
				SecurePostQuery accessQuery=new SecurePostQuery(user);
				return accessQuery;
			}
		};
		return secQParser;
	}
}
