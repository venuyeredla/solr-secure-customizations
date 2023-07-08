package com.vgr.solr.qparsers;

import java.io.IOException;
import java.util.HashSet;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.search.DelegatingCollector;
import org.apache.solr.search.ExtendedQueryBase;
import org.apache.solr.search.PostFilter;

public class SecurePostQuery extends ExtendedQueryBase implements PostFilter {

	 private String user;
	 
	 public SecurePostQuery(String user) {
	   this.user=user;
	  }
	 
	 public static boolean isAllowed(String userid) {
		 
		 return true;
	 }
	 
    public DelegatingCollector getFilterCollector(IndexSearcher searcher) {
		   return new SecureDelegateCollector(searcher, user);
      }

		@Override
		public boolean equals(Object arg0) {
			return false;
		}
		@Override
		public int hashCode() {
			return 0;
		}
		
		@Override
		public int getCost() {
		  // We make sure that the cost is at least 100 to be a post filter
		  return Math.max(super.getCost(), 100);
		}
		
		@Override
		public boolean getCache() {
		  return false;
		}
	
}



class SecureDelegateCollector extends DelegatingCollector {
	IndexSearcher qSearcher = null;

	public SecureDelegateCollector(IndexSearcher searcher,String user) {
		super();
		this.qSearcher = searcher;
	}

	boolean created = false;
	//LeafReader leafReader = null;
	//LongBitSet fixedBitSet = new LongBitSet(32);
	HashSet<Integer> acceptedSet=new HashSet<Integer>();

	@Override
	protected void doSetNextReader(LeafReaderContext context) throws IOException {
		// acls = context.reader().getSortedDocValues("acl");
		//leafReader = context.reader();
		super.doSetNextReader(context);
	}

	@Override
	public void collect(int doc) throws IOException {
		//String documentid = leafReader.document(doc).get("id");
		//System.out.println(documentid);
		if (!created) {
			this.creatAccptedBitSet();
		}
/*		if (fixedBitSet.get(doc)) {
			System.out.println("Matched Doc id " + doc);
			super.collect(doc);
		}*/
		if (acceptedSet.contains(doc)) {
			System.out.println("Matched Doc id " + doc);
			super.collect(doc);
		}
		
	}
	public void creatAccptedBitSet() {
		for (int i = 0; i < 3000; i++) {
				int accdocid=(int)(Math.random()*100000);
				System.out.println(accdocid);
				acceptedSet.add(i);
				//fixedBitSet.set(accdocid);;
		     }
		
	 /* for(long l:fixedBitSet.getBits()) {
		      System.out.println("bit set: "+l);
	        }*/
		   created = true;
	}
}


class SolrSecSearcher{
	public void test() {
		  
		/*SolrIndexSearcher solrIndexSearcher=new SolrIndexSearcher(core, path, schema, config, name, enableCache, directoryFactory);
		solrIndexSearcher.search(qr, cmd)*/
		
	}
}




