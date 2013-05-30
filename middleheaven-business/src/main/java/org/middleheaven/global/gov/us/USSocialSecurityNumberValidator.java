package org.middleheaven.global.gov.us;

import org.middleheaven.global.gov.NDI;
import org.middleheaven.validation.DefaultValidationResult;
import org.middleheaven.validation.MessageInvalidationReason;
import org.middleheaven.validation.ValidationResult;
import org.middleheaven.validation.Validator;

// http://en.wikipedia.org/wiki/Social_Security_Number#Valid_SSNs
public class USSocialSecurityNumberValidator implements Validator<NDI> {

	private boolean acceptAd= false;
	
	@Override
	public ValidationResult validate(NDI ndi) {
		DefaultValidationResult result = new DefaultValidationResult();
		
		if (ndi.length()!=9){
			result.add(MessageInvalidationReason.invalid(ndi));
		}else if (ndi.subGroup(0, 3).equals("000") || 
				ndi.subGroup(4, 2).equals("00") || 
				ndi.subGroup(6, 3).equals("000")){
			result.add(MessageInvalidationReason.invalid(ndi));
		} else if (ndi.subGroup(0, 8).equals("98765432")){
			// 987-65-4320 to 987-65-4329
			if (!acceptAd){
				result.add(MessageInvalidationReason.invalid(ndi));
			}
		} else if (ndi.subGroup(0, 3).asNumber()==null || ndi.subGroup(0, 3).asNumber().longValue()>772){
			result.add(MessageInvalidationReason.invalid(ndi));
		}
		
		return result;
	}
	
	public void setAcceptAdvertisementReserved(boolean acceptAdvertisement){
		this.acceptAd = acceptAdvertisement;
	}
	
	public boolean acceptAdvertisementReserved(){
		return acceptAd;
	}

}
