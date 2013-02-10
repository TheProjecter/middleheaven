/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.util.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.middleheaven.util.function.Predicate;

/**
 * Logic filter. Determines the acceptance of an object  by logically concatenate the results of filter terms
 * @author  Sergio M. M. Taborda
 */
public class LogicComposedPredicate<T> implements Predicate<T>{

    protected final List<Predicate<T>> filters = new ArrayList<Predicate<T>>();
	protected LogicOperator logicOperator = LogicOperator.AND;

    public LogicComposedPredicate(LogicComposedPredicate<T> other){
        this.filters.addAll(other.filters);
        this.logicOperator = other.logicOperator;
    }

    public LogicComposedPredicate(LogicOperator operator){
        setOperator(operator);
    }

    public LogicComposedPredicate( Predicate<T> a ,LogicOperator operator, Predicate<T> b){
        setOperator(operator);
        filters.add(a);
        filters.add(b);
    }

    public int filterCount(){
        return filters.size();
    }

    public LogicComposedPredicate<T> add(Predicate<T> filter){
        filters.add(filter);
        return this;
    }

    public List<Predicate<T>> getFilters(){
        return Collections.unmodifiableList(filters);
    }
    
    public void setOperator(LogicOperator operator){
        this.logicOperator = operator;
    }
    
    public LogicOperator getOperator(){
        return this.logicOperator;
    }
    
    @Override
    public Boolean apply(T object) {
        if (filters.isEmpty()){
            return true;
        } else {
            boolean accept = filters.get(0).apply(object);
            int size = filters.size();
            for (int i = 1 ; !this.logicOperator.isBreakValue(accept) && i < size; i++){
                accept = this.logicOperator.operate(accept, filters.get(i).apply(object));
            }
            return accept;

        }
    }

}
