/**
 * 
 */
package org.middleheaven.domain.store;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.domain.model.EnumModel;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;

/**
 * 
 */
public class EnumType implements UserType { 
	    
		private final EnumModel model;
		private final int[] sqlTypes;
		
	    protected EnumType(EnumModel model) { 
	        this.model = model; 
	        
	        if (String.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.VARCHAR};
	        } else if (Integer.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.INTEGER};
	        } else if (Long.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.BIGINT};
	        } else if (Boolean.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.BIT};
	        } else if (Double.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.DECIMAL};
	        } else if (Float.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.FLOAT};
	        } else if (Byte.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.SMALLINT};
	        } else if (Character.class.equals(model.getPersistableType())){
	        	sqlTypes = new int[]{Types.CHAR};
	        } else {
	        	sqlTypes = new int[0];
	        }
	    } 
	 

	    public int[] sqlTypes() { 
	        return CollectionUtils.duplicateArray(sqlTypes);
	    } 
	 
	    public Class returnedClass() { 
	        return model.getEnumType(); 
	    } 
	 
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object nullSafeGet(ResultSet rs, String[] names,
				SessionImplementor session, Object owner)
				throws HibernateException, SQLException {
  
			Object persitableValue = rs.getObject(names[0]); 
	    	
	        Object result = null; 
	        if (!rs.wasNull()) { 
	            model.getEnumFromValue(persitableValue);
	        } 
	        return result; 
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index,
				SessionImplementor session) throws HibernateException,
				SQLException {
			
			  if (null == value) { 
		            preparedStatement.setNull(index, sqlTypes[0]); 
		        } else if (String.class.equals(model.getPersistableType())){
		        	 preparedStatement.setString(index, model.getPersistableValue(value).toString() ); 
		        } else if (Integer.class.equals(model.getPersistableType())){
		        	 preparedStatement.setInt(index, ((Integer) model.getPersistableValue(value)).intValue()); 
		        } else { 
		            throw new IllegalModelStateException("No safe set for type" + model.getPersistableType());
		        } 
		}

	    public Object deepCopy(Object value) throws HibernateException{ 
	        return value; 
	    } 
	 
	    public boolean isMutable() { 
	        return false; 
	    } 
	 
	    public Object assemble(Serializable cached, Object owner) throws HibernateException {
	         return cached;
	    } 
	 
	    public Serializable disassemble(Object value) throws HibernateException { 
	        return (Serializable)value; 
	    } 
	 
	    public Object replace(Object original, Object target, Object owner) throws HibernateException { 
	        return original; 
	    } 
	    public int hashCode(Object x) throws HibernateException { 
	        return x.hashCode(); 
	    } 
	    public boolean equals(Object x, Object y) throws HibernateException { 
	        if (x == y) {
	            return true;
	        }
	        if (null == x || null == y){
	        	return false;
	        }
	        
	        return x.equals(y); 
	    }


	

}
