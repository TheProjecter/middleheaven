package org.middleheaven.ui.rendering;

import java.io.Serializable;

import org.middleheaven.ui.Displayable;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIDimensionUnit;
import org.middleheaven.ui.UISize;

/**
 * Provides conversion from the diferente {@link UIDimensionUnit}s to pixels.
 */
public abstract class UIUnitConverter implements Serializable{

  
	private static final long serialVersionUID = -7617520430966207065L;
	
	protected static final String TEST_STRING = "X";

	/**
	 * 
	 * Constructor.
	 */
    protected UIUnitConverter(){}

    /**
     * 
     * @param size
     * @param container
     * @return
     */
    public UISize toPixels(UISize size , Displayable container){
    	return toPixels(size, container, UISize.pixels(0, 0));
    }
    
    /**
     * 
     * @param size
     * @param container
     * @param availableSpace
     * @return
     */
    public UISize toPixels(UISize size , Displayable container ,UISize availableSpace ){
    	return UISize.valueOf(
    			toPixelsHorizontal(size.getWidth(), container, availableSpace.getWidth()),
    			toPixelsVertical(size.getHeight(), container, availableSpace.getHeight())
    	);
    }
    
    private UIDimension toPixelsVertical(UIDimension dim , Displayable container ,UIDimension availableSpace ){
    	
         int pixels;
         
         switch (dim.unit){
       
         case PERCENTAGE :
        	 pixels =(int) (availableSpace.getValue() *  dim.value / 100);
        	 break;
         case DIALOG:
        	 pixels = (int)Math.round(getDialogBaseUnits(container)[1] * dim.value);
        	 break;
         case RATIO_AVAILABLE:
        	 pixels = (int)(availableSpace.value * dim.value);
        	 break;
         case PIXELS :
         default:
        	 pixels = (int) dim.getValue(); 
        	 break;
        	 
         }
         
         return UIDimension.pixels(pixels);
         
    }
    
    private UIDimension toPixelsHorizontal(UIDimension dim , Displayable container ,UIDimension availableSpace ){
    	 
    	int pixels;
    	
		switch (dim.unit){
         case PERCENTAGE :
        	 pixels =(int) (availableSpace.getValue() *  dim.value / 100);
        	 break;
         case DIALOG:
        	 // different from vertical
        	 pixels = (int)Math.round(getDialogBaseUnits(container)[0] * dim.value);
        	 break;
         case RATIO_AVAILABLE:
        	 pixels = (int)(availableSpace.value * dim.value);
        	 break;
         case PIXELS :
         default:
        	 pixels = (int) dim.getValue(); 
        	 break;
         }
		
		return UIDimension.pixels(pixels);
    }
    
    
    public final int convertVertical(String unitValue, Displayable container ,int availableSpace){
        unitValue = unitValue.toLowerCase();
        int res=0;
        if (unitValue.startsWith("g")){
            res = (int)(availableSpace * Double.parseDouble(unitValue.substring(2,unitValue.length()-1)));
        } else if (unitValue.endsWith("px")){
            res = Integer.parseInt(unitValue.substring(0,unitValue.indexOf("px")));
        } else if (unitValue.endsWith("dlu")){
            res =  (int)Math.round(getDialogBaseUnits(container)[1] * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("dlu")).trim()));
        } else if (unitValue.endsWith("%")){
            res = (int) (availableSpace * 1d * Integer.parseInt(unitValue.substring(0,unitValue.indexOf('%'))) / 100);
        } else if (Character.isDigit(unitValue.charAt(unitValue.length()-1))) {
            res = Integer.parseInt(unitValue);
        }
        return res;
    }

    public final int convertHorizontal(String unitValue, Displayable container , int availableSpace){
        if (unitValue.trim().length()==0){
            throw new IllegalArgumentException("Unitvalue cannot be empty");
        }
        unitValue = unitValue.trim().toLowerCase();
        int res=0;

        if (unitValue.startsWith("g")){
            res = (int)(availableSpace * Double.parseDouble(unitValue.substring(2,unitValue.length()-1)));
        } else if (unitValue.endsWith("px")){
            res = Integer.parseInt(unitValue.substring(0,unitValue.indexOf("px")));
        } else if (unitValue.endsWith("dlu")){
            res = (int)Math.round(getDialogBaseUnits(container)[0] * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("dlu"))));
        } else if (unitValue.endsWith("%")){
            res = (int) (availableSpace * 1d * Integer.parseInt(unitValue.substring(0,unitValue.indexOf("%"))) / 100);
        } else if (unitValue.length()>0 && Character.isDigit(unitValue.charAt(unitValue.length()-1))) {
            res = Integer.parseInt(unitValue);
        }
        return res;
    }

    /**
     * Return the vertical and horizontal size of the reference font for dialog units
     * @param displayable
     * @return an array for the horizontal and vertical size in positions 0 and 1 respectivly
     */
    protected abstract double[] getDialogBaseUnits(Displayable displayable);

}
