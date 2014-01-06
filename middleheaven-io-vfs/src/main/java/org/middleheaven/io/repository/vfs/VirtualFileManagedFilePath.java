package org.middleheaven.io.repository.vfs;

import org.apache.commons.vfs.FileName;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;

public class VirtualFileManagedFilePath extends ArrayManagedFilePath {

	public VirtualFileManagedFilePath(ManagedFileRepository repository, FileName name) {
		super(repository, name.getRootURI(), name.getPath());
	}

	

}
