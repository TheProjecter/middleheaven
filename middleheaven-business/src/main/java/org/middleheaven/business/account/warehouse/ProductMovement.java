package org.middleheaven.business.account.warehouse;

import org.middleheaven.business.account.AccountMovement;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.Measurable;

public class ProductMovement<E extends Measurable> extends AccountMovement<DecimalMeasure<E>>{

}
