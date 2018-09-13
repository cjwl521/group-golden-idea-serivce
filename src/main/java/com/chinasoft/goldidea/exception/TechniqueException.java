package com.chinasoft.goldidea.exception;

/**
 * 
 * @author Mango
 *
 */
public class TechniqueException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3100173208468380013L;

	public TechniqueException() {
		super();
	}

	public TechniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TechniqueException(String message, Throwable cause) {
		super(message, cause);
	}

	public TechniqueException(String message) {
		super(message);
	}

	public TechniqueException(Throwable cause) {
		super(cause);
	}
	
}
