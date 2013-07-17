package org.middleheaven.logging.writters;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LoggingConfiguration;
import org.middleheaven.logging.LoggingEvent;
import org.middleheaven.logging.LoggingLevel;

/**
 * This writer delegates its writing ability to one or more attached 
 * <code>LogBookWriter</code> asynchronously. It implements <code>WriterAttachable</code>
 * so that one or more real <code>LogBookWriter</code>s can be attached. 
 * You can use this writer any time you which to decouple the writing functionality from the 
 * main stream execution. 
 * 
 *
 * @author Sergio M.M. Taborda
 *
 */
public class AsynchronousWriter extends AbstractWriterAttachable{


    protected BlockingQueue<LoggingEvent> queue;
    protected boolean running;
    protected int bufferSize = 10;


    public AsynchronousWriter(){
        this.level = LoggingLevel.ALL;
    }

    public void config(Map params, LoggingConfiguration configuration) {

        String bufferSizeParam = (String)params.get("buffer.size");

        try {
            bufferSize = Integer.parseInt(bufferSizeParam);
        } catch (NumberFormatException e){
            bufferSize = 10;
        }
        if (bufferSize<=0){
            bufferSize = 10;
        }
        queue = new LinkedBlockingQueue<LoggingEvent>(bufferSize);
        LogThread thread = new LogThread();
        thread.start();

    }

    public void write(LoggingEvent event){
        try {
            this.queue.put(event);
        } catch (InterruptedException e) {
            // exist thread
        }
    }

    protected void realWriteEvent(LoggingEvent event){
        Object[] writersArr = this.writers.toArray();
        for (int i = 0; i < writersArr.length;i++){
            ((LogBookWriter)writersArr[i]).log(event);
        }
    }

    public class LogThread extends Thread {

        public LogThread(){
            this.setDaemon(false);
            this.setName("Asynchronous Logging Thread");
        }

        public void run() {

                while (true) {
                     // take the next event in the pool
                     LoggingEvent event;
                    try {
                        event = (LoggingEvent)queue.take();
//                      sends it to the real writers
                        realWriteEvent(event);
                    } catch (InterruptedException e) {
                        break;
                    }
                     
                }

        }

    }



}
