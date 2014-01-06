package org.middleheaven.quantity.time;


public interface DateHolder{

	/**
	 * The corresponding month day. The day ordinal is bound between 1 and 31
	 *  @return The corresponding month day. 
	 *
	 */
    public DayOfMonth dayOfMonth();
    
    /**
     * The corresponding year day. The day ordinal is bound between 1 and 7
     * @return The corresponding week day
     */
    public DayOfWeek dayOfWeek();
    
    public int dayOfYear();
    
    public Month month();
    
    public Year year();

	public DateHolder nextDate();
    
	public DateHolder previousDate();

	public boolean isBefore(DateHolder end);

	public boolean isAfter(DateHolder other);

	public DateHolder minus(ElapsedTime elapsed);

	public DateHolder plus(ElapsedTime elapsed);

	/**
	 * Finds the <code>weekDay</code> equal of before to this date
	 * @param weekDay weekday to find
	 * @return the nearest day that is equal of before this and is a <code>weekDay</code>
	 */
	public DateHolder nearestBefore(DayOfWeek weekDay);
    
	/**
	 * Finds the <code>weekDay</code> equal of after this date
	 * @param weekDay weekday to find
	 * @return the nearest day that is equal of after <code>this</code> and is a <code>weekDay</code>
	 */
	public DateHolder nearestAfter(DayOfWeek weekDay);

	/**
	 * Finds the nearest <code>weekDay</code> after of before this date
	 * @param weekDay weekday to find
	 * @return the nearest day to <code>this</code> that is a <code>weekDay</code>
	 */
	public DateHolder nearest(DayOfWeek weekDay);

	public int compareTo(DateHolder date);


}
