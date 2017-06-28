package com.anbot.server.maptiler.exception;

/**
 * @author suwan
 *
 */
public class ImageWriteException extends Exception {
	private static final long serialVersionUID = 1L;
    
	public ImageWriteException(String msg) {
		super(msg);
	}
	    
	public ImageWriteException(String msg, Exception e) {
	    super(msg, e);
	}

}
