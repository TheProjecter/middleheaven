package org.middleheaven.quantity.time.zones;

import org.middleheaven.quantity.time.TimeZone;
import org.middleheaven.quantity.time.TimeZoneTable;

public class JavaTimeZoneTable extends TimeZoneTable {

	@Override
	public TimeZone convertFromJavaTimeZone(java.util.TimeZone zone) {
		return new WrappedTimeZone(zone);
	}

	@Override
	public TimeZone getTimeZone(String reference) {
		return convertFromJavaTimeZone(java.util.TimeZone.getTimeZone(reference));
	}

	private static class WrappedTimeZone extends TimeZone{
		java.util.TimeZone zone;
		
		public WrappedTimeZone(java.util.TimeZone zone) {
			this.zone = zone;
		}

		@Override
		public long getRawOffset() {
			return zone.getRawOffset();
		}

		@Override
		public long getOffset(long universalTime) {
			return zone.getOffset(universalTime);
		}

		@Override
		public java.util.TimeZone toTimeZone() {
			return zone;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object other) {
			return (other instanceof WrappedTimeZone) && equalsOther((WrappedTimeZone) other);
		}
		
		/**
		 * @param other
		 * @return
		 */
		private boolean equalsOther(WrappedTimeZone other) {
			return this.zone.equals(other.zone);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
		
	}
}
