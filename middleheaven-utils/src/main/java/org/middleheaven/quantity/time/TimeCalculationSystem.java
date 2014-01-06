/*
 * Created on 2007/01/06
 *
 */
package org.middleheaven.quantity.time;

/**
 * There are two distinct forms for time difference calculation:
 * <ol>
 * <li> <b>Milisecounds Accumulation</b> - MAC - Time passes by acummulation of continuous units of time.
 * In the case, by acummulation of milliseconds. So a day as passed only when 24 complete hours have passed.
 * Un hour as passed only when complete 60 minutes have passed, and so on.
 * In this system 23h equals the passage of zero days, and 25h equals the passage of one day.
 * </li>
 * <li> <b>Time Unit Change</b> - TUC - Time passes by acummulation of discrete units of time.
 * In this system, a day as passed if the difference between two dates differ from 1 in the day field.
 * Example: one day as passed between 2000-12-31 and 2001-01-01. 
 * The difference is calculated looking for own many units of a specific filed have passed.
 * </li>
 * 
 * @author Sergio M. M. Taborda 
 *
 */
public enum TimeCalculationSystem {

    TUC,
    MAC
}
