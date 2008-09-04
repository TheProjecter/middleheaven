package org.middleheaven.global.text;

class PTptNumberWriteoutFormat extends NumberWriteoutFormat{

	public PTptNumberWriteoutFormat(){}
	
	@Override
	public String getNegativeSufix() {
		return "negativo";
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
		case 1: return "um";
		case 2: return "dois";
		case 3: return "tr�s";
		case 4: return "quatro";
		case 5: return "cinco";
		case 6: return "seis";
		case 7: return "sete";
		case 8: return "oito";
		case 9: return "nove";
		case 10: return "dez";
		case 11: return "onze";
		case 12: return "doze";
		case 13: return "treze";
		case 14: return "quatorze";
		case 15: return "quinze";
		case 16: return "dezaseis";
		case 17: return "dezasete";
		case 18: return "dezoito";
		case 19: return "dezanove";
		case 100: return "cem";
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
				case 2: return "vinte";
				case 3: return "trinta";
				case 4: return "quarenta";
				case 5: return "cinquenta";
				case 6: return "sessenta";
				case 7: return "setenta";
				case 8: return "oitenta";
				case 9: return "noventa";
				default : return "";
				}
			}
		} else if (indice == 2){
			switch (value/100){
			case 1: return "cento";
			case 2: return "duzentos";
			case 3: return "trezentos";
			case 4: return "quatrocentos";
			case 5: return "quinhentos";
			case 6: return "seissentos";
			case 7: return "setesentos";
			case 8: return "oitocentos";
			case 9: return "novecentos";
			default : return "";
			}
		} else {
			return "";
		}
	}

	@Override
	public String getFractionUnitName(int exponent) {
		switch (exponent){
		case 1: return "d�cimos";
		case 2: return "cent�simos";
		case 3: return "mil�simos";
		case 4: return "d�cimos mil�simos";
		case 5: return "cent�simos mil�simos";
		case 6: return "milion�simos";
		case 7: return "d�cimos milion�simos";
		case 8: return "cent�simos milion�simos";
		case 9: return "bilion�simos";
		case 10: return "d�cimos bilion�simos";
		case 11: return "cent�simos bilion�simos";
		case 12: return "trilion�simos";
		case 13: return "d�cimos trilion�simos";
		case 14: return "cent�simos trilion�simos";
		default : 
			throw new NumberOutOfRangeException();
		}
	}

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "zero";
			case 2: return "mil";
			case 3: return value == 1 ? "milh�o" : "milh�es";
			case 4: return value == 1 ? "milhar de milh�o" : "milhares de milh�o";
			case 5: return value == 1 ? "bilh�o" : "bilh�es";
			case 6: return value == 1 ? "milhar de bilh�o" : "milhares de bilh�o";
			case 7: return value == 1 ? "trilh�o" : "trilh�es";
			case 8: return value == 1 ? "milhar de trilh�o"  : "milhares de trilh�o";
			case 9: return value == 1 ? "quadrilh�o"  : "quadrilh�es";
			case 10: return value == 1 ? "milhar de quadrilh�o"  : "milhares de quadrilh�o";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}

	@Override
	public String getInnerGroupConcatenator(int value,int group, int indice) {
		return value <100 && (value%10==0 || value <20) ? null : "e";
	}

	@Override
	public String getInterGroupConcatenator(int previousGroupValue,int previousGroup, int nextGroup) {
		return nextGroup==0 ? "e" : previousGroup > 1 ? "," : "";
	}











}
