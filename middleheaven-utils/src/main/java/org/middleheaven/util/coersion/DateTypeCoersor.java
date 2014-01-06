package org.middleheaven.util.coersion;

import java.sql.Timestamp;

public class DateTypeCoersor <N extends java.util.Date> extends AbstractTypeCoersor<N, java.util.Date> {

		private Class<? extends java.util.Date> dateClass;

		public static <T extends java.util.Date> DateTypeCoersor<T> newInstance(Class<T> dateClass){
			return new DateTypeCoersor<T>(dateClass);
		}
		
		private DateTypeCoersor(Class<? extends java.util.Date> dateClass) {
			this.dateClass= dateClass;
		}

		@Override
		public <T extends java.util.Date> T coerceForward(N value, Class<T> type) {
			if (value ==null){
				return null;
			}
			
			return (T)new java.util.Date(value.getTime());
		}

		@Override
		public <T extends N> T coerceReverse(java.util.Date value, Class<T> type) {
			if (value == null){
				return null;
			}
			
			if (type.isAssignableFrom(Timestamp.class)){
				return type.cast(new Timestamp(value.getTime()));
			} else if (type.isAssignableFrom(java.sql.Date.class)) {
				return type.cast(new java.sql.Date(value.getTime()));
			} else if (type.isAssignableFrom(java.sql.Time.class)) {
				return type.cast(new java.sql.Time(value.getTime()));
			}
			return null;
		}



	}
