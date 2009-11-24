package de.ilimitado.smartspace;

public class LoadSensorException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoadSensorException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}

}
