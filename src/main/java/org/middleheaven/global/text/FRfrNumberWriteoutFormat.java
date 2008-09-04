package org.middleheaven.global.text;

class FRfrNumberWriteoutFormat extends NumberWriteoutFormat{

	public FRfrNumberWriteoutFormat(){}
	
	@Override
	public String getNegativeSufix() {
		return "negative";
	}

	@Override
	public String getUnitName() {
		return "";
	}

	@Override
	public boolean isNotable(int value) {
		return (value==100 || value < 20);
	}

	@Override
	public String getWordsForNotable(int value, int group) {
		switch (value){
		case 1: return "un";
		case 2: return "deux";
		case 3: return "troi";
		case 4: return "quatre";
		case 5: return "cinq";
		case 6: return "six";
		case 7: return "sept";
		case 8: return "huit";
		case 9: return "noeuf";
		case 10: return "dix";
		case 11: return "onze";
		case 12: return "douze";
		case 13: return "treize";
		case 14: return "quatorze";
		case 15: return "quinze";
		case 16: return "seize";
		case 17: return "dix-sept";
		case 18: return "dix-huit";
		case 19: return "dix-neuf";
		case 100: return "cent";
		default : return "";
		}
	}

	@Override
	public String getWordsFor(int value, int group, int indice) {
		
		if (indice == 0){
			return getWordsForNotable(value,group);
		} else if (indice == 1){
			int rvalue = value%100;
			if (rvalue<20){
				return getWordsForNotable(rvalue,group);
			} else {
				switch (rvalue/10){
				case 2: return "vignt";
				case 3: return "trente";
				case 4: return "quarante";
				case 5: return "cinquante";
				case 6: return "soixante";
				case 7: return "soixante-dix";
				case 8: return "quatre-vignt";
				case 9: return "quatre-vignt-dix";
				default : return "";
				}
			}
		} else if (indice == 2){
			if (value/100==1){
				return "cent";
			}
			return getWordsFor(value, group, 1) + " cent";
		} else {
			return "";
		}
	}

	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "decime";
		case 2: return "centesime";
		case 3: return "milinesime";
		case 4: return "decime milinesime";
		case 5: return "centesime milinesime";
		default: 
			throw new NumberOutOfRangeException();
		}
	}

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "z�ro";
			case 2: return "mile";
			case 3: return "million";
			case 4: return "billion";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getInnerGroupConcatenator(int value,int group, int indice) {
		return (indice > 0 && value%100 <=10) ? "et" :(  value<20 || value %10 ==0 ?  null : "");
	}

	@Override
	public String getInterGroupConcatenator(int previousGroupValue,int previousGroup, int nextGroup) {
		return "et";
	}











}
