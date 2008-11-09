package org.middleheaven.global.text.writeout;

public class BRptNumberWriteoutFormat extends PTptNumberWriteoutFormat{

	@Override
	public String getGroupSufix(int value, int group, int indice) {
		
			switch (group){
			case 0: return "zero";
			case 2: return "mil";
			case 3: return value == 1 ? "milh�o" : "milh�es";
			case 4: return value == 1 ? "bilh�o" : "bilh�es";
			case 5: return value == 1 ? "trilh�o" : "trilh�es";
			case 6: return value == 1 ? "quadrilh�o" : "quadrilh�es";
			case 7: return value == 1 ? "quintilh�o" : "quintilh�es";
			case 8: return value == 1 ? "sextilh�o"  : "sextilh�es";
			default :
				throw new NumberOutOfRangeException();
			}
		
	}



}
