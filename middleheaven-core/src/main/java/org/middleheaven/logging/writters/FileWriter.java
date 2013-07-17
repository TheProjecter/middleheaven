package org.middleheaven.logging.writters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.LoggingConfiguration;
import org.middleheaven.util.StringUtils;

public class FileWriter extends StreamLogBookWriter {


    protected String filename;
	private BufferedOutputStream out;

    public FileWriter(){
        this.format = new HTMLFormat();
    }

    /**
     * Accepted Parameters
     * <ul>
     *  <li>file.name - log file name.</li>
     *  <li>file.append - logic value indicating if a new event must be aapended to the file, instead of overriding n existing file.</li>
     *  <li>file.path - path where to log the file. This pathe is relative to the path present in  LoggingConfiguration
     *  Writing permission is required for this path. </li>
     * </ul>
     */
    @Override 
    public void config(Map<String,String> params, LoggingConfiguration configuration) {
    	
    	
    	// determine if events must be appended to the file
    	 Boolean append = StringUtils.booleanValueOf(params.get("file.append"),true);

    	// determine file name   
        filename = params.get("file.name");
        if (filename==null || filename.trim().isEmpty()){
        	throw new IllegalArgumentException("file.name is required for FileWriter objetcs");
        }
        
        // determine file path, if any
        String filePath = params.get("file.path");
        if (filePath!=null && !filePath.trim().isEmpty()){
        	filename = filePath + "/" + filename;
        }
        
     
        ManagedFile rep = configuration.getBaseRepository();
        
        ManagedFile logFile = rep.retrive(filename);
        
        if (!logFile.exists()){
        	logFile.createFile();
        }

        File file = new File(logFile.getURI().toString());

        setFile(file,append.booleanValue());
    }


    protected void setFile(File file, boolean append){
        try {
            boolean exists = file.exists();
            this.out = new BufferedOutputStream(new FileOutputStream(file,append));
            if (!exists){
                this.format.writerHeader(new PrintWriter(out));
            }
        } catch (FileNotFoundException e) {
            // TODO
        }
    }

    public void finalized(){
        try {
            this.format.writerFooter(new PrintWriter(out));
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected OutputStream getStream() {
		return out;
	}

}
