package org.middleheaven.aas;

import java.io.Serializable;

/**
 * Condition with with the ResourceAccessor must comply 
 * in order to access a resource.
 *
 */
public interface Permission extends Serializable{

    
	/**
	 * 
	 * @param required the required permission
	 * @return <true> if this permission implies the access required by <code>required</code>
	 */
    public boolean implies(Permission required);
    
    /**
     * 
     * @return <code>true</code> se o <code>ResourceAcessor</code> n�o
     * necessita de nenhuma permiss�o especial para satisfazer esta permiss�o.
     * Ou seja, todos os  <code>ResourceAcessor</code> t�m acesso ao recurso protegido
     * por esta permiss�o.
     */
    public boolean isLenient();
    
    /**
     * 
     * @return  <code>true</code> se nenhuma permiss�o do <code>ResourceAcessor</code> 
     *  satisfazer esta permiss�o independente de quais as permiss�es do <code>ResourceAcessor</code> 
     * Ou seja, nenhum <code>ResourceAcessor</code> t�m acesso ao recurso protegido
     * por esta permiss�o.
     */
    public boolean isStrict();
    
}
