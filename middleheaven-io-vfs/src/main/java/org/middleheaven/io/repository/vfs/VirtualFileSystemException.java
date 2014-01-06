package org.middleheaven.io.repository.vfs;

import org.middleheaven.io.ManagedIOException;

public class VirtualFileSystemException extends ManagedIOException {

	private static final long serialVersionUID = -7093211791723687318L;

	protected VirtualFileSystemException(Exception cause) {
		super(cause);
	}

}
