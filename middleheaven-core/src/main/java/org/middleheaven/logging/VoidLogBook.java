
package org.middleheaven.logging;


/**
 * This LogBook consumes all events without registering then anywhere.
 * It`s a NO-OP LogBook
 *
 *
 */
final class VoidLogBook extends LogBook{

    private static final VoidLogBook ME = new VoidLogBook();
    
    protected static synchronized VoidLogBook getInstance(){
        return ME;
    }
    
    private VoidLogBook() {
        super("voidbook", LoggingLevel.NONE);
    }
    
    @Override
    public void doLog(LoggingEvent event) {}


    public boolean isEnabled(LoggingLevel testlevel){
        return false;
    }
    
}
