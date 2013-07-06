package org.middleheaven.logging.writters;

import java.io.PrintWriter;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;

public interface LogFormat {

    public void setWriter(LogBookWriter writer);
    public void writerHeader(PrintWriter writer) throws LogWritingIOExcepiton;
    public void format(LoggingEvent event, PrintWriter writer) throws LogWritingIOExcepiton;
    public void writerFooter(PrintWriter writer) throws LogWritingIOExcepiton;
    public String getContentType();
}
