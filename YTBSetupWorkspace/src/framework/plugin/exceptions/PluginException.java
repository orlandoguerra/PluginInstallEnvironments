package framework.plugin.exceptions;

import framework.plugin.util.UtilityEclipse;

public class PluginException  extends RuntimeException {
	
	private static final long serialVersionUID = 07712;
	
	/**
	 * The constructor only receives the error message, creates the exception and log it
	 * @param message the message to be used as parameter to the super-class
	 */
	public PluginException(String message){
	      super(message);
	      UtilityEclipse.pluginLog(message);
	 }
	
	/**
	 * The constructor takes two arguments to create the super exception
	 * @param message  the message to be used as parameter to the super-class
	 * @param exception  the exception to be used as parameter to the super-class
	 */
	public PluginException(String message, Exception exception) { 
		super(message, exception); 
		UtilityEclipse.pluginLog(message,exception);
	}

}
