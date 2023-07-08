package org.apache.solr.client.solrj.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.DocRouter;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.params.ModifiableSolrParams;


/**
 * 
 * Instances of this class is used by {@code PrincipalCloudSolrClient} to route principal solr documents(user/group)
 *  to the <b>/principalupdate</b> handler configured in solrconfig.xml file.
 * 
 * @author venuyeredla
 *
 */
public class PrincipalUpdateRequest extends UpdateRequest{
	 private static String PRINCIPAL_UPDATE_HANDLER="/principalupdate";

	 private static final long serialVersionUID = 1L;
	 
	 
   	 public PrincipalUpdateRequest() {
		    super(PRINCIPAL_UPDATE_HANDLER);
	  }
		  
	  public PrincipalUpdateRequest(String url) {
		    super(url);
	  }
	  
	  /**
	   * This method overwrites getRoutes method in UpdateRequest. The main purpose of this method is
	   * to send a copy of each principal document(user/group) to each shard leader in the cloud.
	   * 
	   * @param router to route updates with
	   * @param col DocCollection for the updates
	   * @param urlMap of the cluster
	   * @param params params to use
	   * @param idField the id field
	   * @return a Map of urls to requests
	   */
	  @Override
	  public Map<String,LBHttpSolrClient.Req> getRoutes(DocRouter router,
	      DocCollection col, Map<String,List<String>> urlMap,
	      ModifiableSolrParams params, String idField) {
		  
		  
	    if ((getDocuments() == null || getDocuments().size() == 0)
	        && (getDeleteById() == null || getDeleteById().size() == 0)) {
	      return null;
	    }
	    
	    Map<String,LBHttpSolrClient.Req> routes = new HashMap<>();
	    if (getDocuments() != null) {
	    	Set<Entry<SolrInputDocument,Map<String,Object>>> entries = getDocumentsMap().entrySet();
			for (Entry<SolrInputDocument, Map<String, Object>> entry : entries) {
				SolrInputDocument doc = entry.getKey();
				Object id = doc.getFieldValue(idField);
				if (id == null) {
					return null;
				}

				for (Entry<String, List<String>> urlMapEntrry : urlMap.entrySet()) {
					List<String> urls = urlMapEntrry.getValue();
					if (urls == null) {
						return null;
					}
					String leaderUrl = urls.get(0);
					LBHttpSolrClient.Req request = routes.get(leaderUrl);
					if (request == null) {
						UpdateRequest updateRequest = new UpdateRequest();
						updateRequest.setMethod(getMethod());
						updateRequest.setCommitWithin(getCommitWithin());
						updateRequest.setParams(params);
						updateRequest.setPath(getPath());
						updateRequest.setBasicAuthCredentials(getBasicAuthUser(), getBasicAuthPassword());
						updateRequest.setResponseParser(getResponseParser());
						request = new LBHttpSolrClient.Req(updateRequest, urls);
						routes.put(leaderUrl, request);
					}
					UpdateRequest urequest = (UpdateRequest) request.getRequest();
					Map<String, Object> value = entry.getValue();
					Boolean ow = null;
					if (value != null) {
						ow = (Boolean) value.get(OVERWRITE);
					}
					if (ow != null) {
						urequest.add(doc, ow);
					} else {
						urequest.add(doc);
					}
				}

			}
	    }
	    
	    // Route the deleteById's
	    
	    if (getDeleteByIdMap() != null) {
	      
	      Iterator<Map.Entry<String,Map<String,Object>>> entries = getDeleteByIdMap().entrySet()
	          .iterator();
	      while (entries.hasNext()) {
	        
	        Map.Entry<String,Map<String,Object>> entry = entries.next();
	        
	        String deleteId = entry.getKey();
	        Map<String,Object> map = entry.getValue();
	        Long version = null;
	        if (map != null) {
	          version = (Long) map.get(VER);
	        }
	        Slice slice = router.getTargetSlice(deleteId, null, null, null, col);
	        if (slice == null) {
	          return null;
	        }
	        List<String> urls = urlMap.get(slice.getName());
	        if (urls == null) {
	          return null;
	        }
	        String leaderUrl = urls.get(0);
	        LBHttpSolrClient.Req request = routes.get(leaderUrl);
	        if (request != null) {
	          UpdateRequest urequest = (UpdateRequest) request.getRequest();
	          urequest.deleteById(deleteId, version);
	        } else {
	          UpdateRequest urequest = new UpdateRequest();
	          urequest.setParams(params);
	          urequest.deleteById(deleteId, version);
	          urequest.setCommitWithin(getCommitWithin());
	          request = new LBHttpSolrClient.Req(urequest, urls);
	          routes.put(leaderUrl, request);
	        }
	      }
	    }
	    return routes;
	  }

}
