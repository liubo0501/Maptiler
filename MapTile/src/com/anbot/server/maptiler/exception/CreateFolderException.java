package com.anbot.server.maptiler.exception;

/**
 * @author suwan
 *
 */
public class CreateFolderException extends Exception {
	private static final long serialVersionUID = 1L;
	    
	public CreateFolderException(String msg) {
		super(msg);
	}
	    
	public CreateFolderException(String msg, Exception e) {
	    super(msg, e);
	}

}
