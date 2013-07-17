package org.middleheaven.text.indexing.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.FieldCriterion;
import org.middleheaven.util.criteria.FieldInSetCriterion;
import org.middleheaven.util.criteria.JunctionCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.text.TextSearchCriteria;

public class CriteriaInterpreter {

	
	public Query interpret(TextSearchCriteria<IndexableDocument> criteria, Analyzer analyzer){
		BooleanQuery q = new BooleanQuery();
		
		this.interpret(q, criteria.constraints(),analyzer);
		
		return q;
	}
	
	private void interpret( BooleanQuery masterQuery , LogicCriterion lc, Analyzer analyzer ){
		for (Criterion criterion : lc){
			if (criterion instanceof LogicCriterion){
				BooleanQuery q = new BooleanQuery();
				
				this.interpret(q, (LogicCriterion)criterion,analyzer);
				
				masterQuery.add(q , BooleanClause.Occur.SHOULD);
				
			} else if (criterion instanceof FieldCriterion){
				interpret(masterQuery , (FieldCriterion) criterion,analyzer);
			} /*else if (criterion instanceof FieldInSetCriterion){
				
			} else if (criterion instanceof JunctionCriterion){
				// ignore. these are not supported yet
			}*/
		}
	}
	
	private void interpret( BooleanQuery masterQuery , FieldCriterion fc,Analyzer analyzer ){
		
		final CriterionOperator op = fc.getOperator();
		
		if(CriterionOperator.CONTAINS.equals(op)){
			
			Enumerable<String> words = Splitter.on(" ").split(fc.valueHolder().getValue().toString());
			if (words.size() > 1){
				// more than one word. use phrasequery
				PhraseQuery q = new PhraseQuery();
			
				for (String word : words){
					q.add(new Term(fc.getFieldName().getDesignation(), word));
				}
				
				masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD);
			} else if (words.size() == 1){
				Query q = new TermQuery(new Term(fc.getFieldName().getDesignation(), words.getFirst() ));
				masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD);
			}

		} else if (CriterionOperator.EQUAL.equals(op)){
			
			Query q = new TermQuery(new Term(fc.getFieldName().getDesignation(), fc.valueHolder().getValue().toString()));
			masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT :  BooleanClause.Occur.MUST);
			
		} else if (CriterionOperator.ENDS_WITH.equals(op)){
			
			Query q = new WildcardQuery(new Term(fc.getFieldName().getDesignation(), "*" + fc.valueHolder().getValue().toString()));
			masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD);
			
		} else if (CriterionOperator.STARTS_WITH.equals(op)){
			
			Query q = new PrefixQuery(new Term(fc.getFieldName().getDesignation(),  fc.valueHolder().getValue().toString()));
			masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD);
			
		} else if (CriterionOperator.NEAR.equals(op)){
			
			Query q = new FuzzyQuery(new Term(fc.getFieldName().getDesignation(),  fc.valueHolder().getValue().toString()));
			masterQuery.add(q, op.isNegated() ? BooleanClause.Occur.MUST_NOT : BooleanClause.Occur.SHOULD);
			
		} 

	}
	

}
