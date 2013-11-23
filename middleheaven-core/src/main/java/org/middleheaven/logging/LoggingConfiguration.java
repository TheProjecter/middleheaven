package org.middleheaven.logging;

import org.middleheaven.io.repository.ManagedFile;



public class LoggingConfiguration {

	private ManagedFile baseRepository;
	
	public LoggingConfiguration(ManagedFile baseRepository) {
		super();
		this.baseRepository = baseRepository;
	}
	
	public ManagedFile getBaseRepository(){
		return baseRepository;
	}
	
	/**
	 * Informs if log information can be persisted locally.
	 * If not, log writers like FileWriter cannot be used but 
	 * writers like EmailWriter or ConsoleWriter can.
	 * @return
	 */
	public boolean isLocalPersistanceAllowed(){
		return baseRepository!=null && baseRepository.isWriteable();
	}


}
