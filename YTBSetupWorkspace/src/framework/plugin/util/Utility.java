package framework.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.runtime.CoreException;

import framework.plugin.exceptions.PluginException;

public class Utility {
	
	
	/**
	 * This method copy the content of a directory to a different directory
	 * @param newDir the final path of the files
	 * @param oldDir the original path of the files
	 */
	public static void copyDirectoryContent(String newDir, String oldDir) {
		UtilityEclipse.pluginLog("copyDirectoryContent+ newDir:"+newDir +" oldDir:"+oldDir);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("newDir",newDir);
		parameterMap.put("oldDir",oldDir);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("copyDirectoryContent+:"+parameters);
		try{
			String[] antTasks = {"CopyDirContent"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Error copying configuration files",e);
		}
	}
	
	/**
	 * This method copy the jars of a directory to a different directory
	 * @param newDir the final path of the jars
	 * @param oldDir the original path of the jars
	 */
	public static void copyJarContent(String newDir, String oldDir) {
		UtilityEclipse.pluginLog("copyJarContent+ newDir:"+newDir +" oldDir:"+oldDir);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("newDir",newDir);
		parameterMap.put("oldDir",oldDir);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("copyDirectoryContent+:"+parameters);
		try{
			String[] antTasks = {"CopyJarContent"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Error copying jar files",e);
		}
		
	}
	
	/**
	 * Add a record to a file indicating that the plugin was used
	 * @param environment
	 */
	public static void addCounter(String environment) {
		UtilityEclipse.pluginLog("addCounter+ environment:"+environment);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("environment",environment+"v2");
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("addCounter+:"+parameters);
		try{
			String[] antTasks = {"AddCounter"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Error addCounter",e);
		}
		
	}
		
	/**
	 * This method loads the information of one of the properties in the configuration file
	 * @param propertyName the name of the property in the properties file
	 * @return
	 */
	public static String loadProperty(String propertyName) {
		UtilityEclipse.pluginLog("loadProperty+ propertyName:"+propertyName);
		Properties prop = new Properties();
		InputStream input = null;
		String property = null;
	 
		try{
			input = new FileInputStream(UtilityEclipse.getPluginLocationInternal()+"/environment.properties");
			prop.load(input);
			property = prop.getProperty(propertyName);
		} catch (FileNotFoundException ex) {
			throw new PluginException("Error loading properties: File not found",ex);
		} catch (IOException e) {
			throw new PluginException("Error loading properties: Reading file",e);
		}
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return property;
	}

	/**
	 * This method loads external properties
	 * @param propertyName the name of the property to be loaded
	 * @return
	 */
	public static String loadExternalProperty(String propertyName) {
		UtilityEclipse.pluginLog("loadExternalProperty+ propertyName:"+propertyName);
		Properties prop = new Properties();
		InputStream input = null;
		String property = null;
	 
		try{
			input = new FileInputStream(Utility.loadProperty("build.path")+"/environment.properties");
			prop.load(input);
			property = prop.getProperty(propertyName);
		} catch (FileNotFoundException ex) {
			throw new PluginException("Error loading properties: File not found",ex);
		} catch (IOException e) {
			throw new PluginException("Error loading properties: Reading file",e);
		}
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return property;
	}
	
	/**
	 * This method configures the profile 
	 * @param profileName the name of the profile to be configured
	 * @param wasPath the was path for the profile
	 * @param viewFwk the path to be used during the configuration
	 */
	public static void configureProfileUser(String profileName, String wasPath, String viewFwk) {
		UtilityEclipse.pluginLog("configureProfileUser+profileName:"+profileName+ " wasPath:"+wasPath + " viewFwk:"+viewFwk);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("was.path",wasPath);
		parameterMap.put("profile.name",profileName);
		parameterMap.put("fwk.view",viewFwk);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("configureProfileUser+:"+parameters);
		try{
			String[] antTasks = {"ConfigProfile"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Error configuring User "+profileName,e);
		} 
	}
	
	/**
	 * Executes a script using user and password
	 * @param scriptName the name of the script to be used
	 * @param profileName the name of the profile to be used
	 * @param wasInstallationPath the path of the was server
	 * @param viewFwk  the was path for the profile
	 * @param user the user to be used for the authentication
	 * @param pwd the password to be used for the authentication
	 */
	public static void executePhythonCredential(String scriptName, String profileName, String wasInstallationPath, String viewFwk, String user, String pwd) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("was.Install.Portal", wasInstallationPath);
		parameterMap.put("profile.name", profileName);
		parameterMap.put("use.view",viewFwk);
		parameterMap.put("script.name",scriptName);
		parameterMap.put("credential.user",user);
		parameterMap.put("credential.pwd",pwd);
		
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("ExecutePhythonCredentials+:"+parameters);
		try{
			String[] antTasks = {"ExecutePhythonCredentials"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();	
		}catch (CoreException e) {
			throw new PluginException("Error running script (Review if the server is running): "+profileName + " Script: "+scriptName,e);
		} 
	}
	
	/**
	 * This method transforms a map to a string with the parameters used on ant
	 * @param parameterMap the map of parameters
	 * @return
	 */
	public static String transformMapToParameters(Map<String, String> parameterMap){
		Set<String> keyset =   parameterMap.keySet();
		StringBuilder parameters = new StringBuilder();
		for (String value : keyset) {
			parameters.append("-D"+value+"=\""+parameterMap.get(value).replace("\\", "/")+"\" ");
		}
		return parameters.toString();
	}
	

	/**
	 * This method creates a profile 
	 * @param profileName the name of the profile to be created
	 * @param wasPath the path to the was server to be used
	 */
	public static void createProfileUser(String profileName, String wasPath) {
		UtilityEclipse.pluginLog("createProfileUser+ profileName:"+profileName);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("profile.name", profileName );
		parameterMap.put("was.path", wasPath );
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("createProfileUser+:"+parameters);
		try{
			String[] antTasks = {"CreateProfile"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();	
		}catch (CoreException e) {
			throw new PluginException("Error creating User "+profileName,e);
		}
	}
	
	/**
	 * This method retrieves the jar version to be used for TIERS
	 * @param view the name of the current YTB view
	 * @return
	 */
	public static String getJarVersion(String view){
		UtilityEclipse.pluginLog("getJarVersion+ view:"+view);
		File fileJava = new File(view);
		String simpleView = fileJava.getName();
		List<String> jarDirectories = Utility.getDirectoriesInside(Utility.loadExternalProperty("TIERS.jars.version"));
		
		String tiersPrefix  = simpleView.substring(simpleView.indexOf("YTB_") + 4);
		tiersPrefix = "TIERS_" + tiersPrefix.substring(0,11);
		
		for (String folder : jarDirectories) {
			if(folder.startsWith(tiersPrefix)){
				return folder;
			}
		}
		throw new PluginException("The view:"+simpleView +"doesn't match with a TIERS jar directory");
	}
	
	
	/**
	 * This method obtains the list of directories inside of a directory
	 * @param directory the name of the directory to be scanned
	 * @return  list of the names of the directories
	 */
	public static List<String> getDirectoriesInside(String directory){
		UtilityEclipse.pluginLog("getDirectoriesInside+ directory:"+directory);
		File file = new File(directory);
		List<String> directories = new ArrayList<String>();
		String[] names = file.list();
		for(String name : names)
		{
			directories.add(name);
		}
		
		return directories;
	}
	
	/**
	 * This method retrieves the directories inside of a directory that starts with a string
	 * @param directory the directory to be used for the search
	 * @param startWith the string to be used as start 
	 * @return
	 */
	public static List<String> getDirectoriesInsideStartsWith(String directory, String startWith){
		UtilityEclipse.pluginLog("getDirectoriesInsideStartsWith+ directory:"+directory+ " + directory:"+startWith);
		File file = new File(directory);
		List<String> directories = new ArrayList<String>();
		String[] names = file.list();
		for(String name : names)
		{
			if(name.startsWith(startWith))
				directories.add(name);
		}
		return directories;
	}
	
	/**
	 * This method retrieves the file names inside of a directory that starts with a string
	 * @param directory directory the directory to be used for the search
	 * @param startWith the string to be used as start 
	 * @return
	 */
	public static List<String> getFileInsideFilterStartsWith(String directory, String startWith){
		UtilityEclipse.pluginLog("getDirectoriesInside+ directory:"+directory);
		File file = new File(directory);
		List<String> directories = new ArrayList<String>();
		String[] names = file.list();
		for(String name : names)
		{
			if(name.startsWith(startWith))
				directories.add(name);
		}
		
		return directories;
	}
	
	/**
	 * This method executes a phyton Script
	 * @param scriptName the name of the script to be executed
	 * @param profileName the profile name used to run the script
	 * @param wasPath the path of the was to be used in the script
	 * @param viewFwk the name of the view to be used for the configuration
	 */
	public static void executePhython(String scriptName, String profileName, String wasPath, String viewFwk) {
		UtilityEclipse.pluginLog("ExecutePhython+scriptName:"+scriptName+" profileName:"+profileName+ " wasPath:"+wasPath + " viewFwk:"+viewFwk);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("was.path",wasPath);
		parameterMap.put("profile.name", profileName);
		parameterMap.put("use.view",viewFwk);
		parameterMap.put("script.name", scriptName);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("executePhython+:"+parameters);
		try{
			String[] antTasks = {"ExecutePhython"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments( parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();	
		}catch (CoreException e) {
			throw new PluginException("Error configuring User "+profileName,e);
		} 
	}
	
	
	/**
	 * This method updates a jar file using a token and replacing by other value
	 * @param file the name of the file to be modified
	 * @param oldLine the String to be removed
	 */
	public static void updateTokenProperties(String jarFile, String newToken, String oldToken) {
		UtilityEclipse.pluginLog("replaceInFile+ jarFile:"+jarFile+ " newToken:"+newToken+ " oldToken:"+oldToken);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("jar.path",jarFile);
		parameterMap.put("replaced.token", oldToken);
		parameterMap.put("new.value", newToken);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("updateTokenProperties+:"+parameters);
		try{
			String[] antTasks = {"UpdateTokenProperties"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
			
		}catch (CoreException e) {
			throw new PluginException("Updating Jar file "+jarFile,e);
		} 
	}
	
	/**
	 * This method regenerated the jar file based on the Fw Annotation  Processor
	 * @param newDir
	 */
	public static void regenerateFwAnnotation(String newDir) {
		UtilityEclipse.pluginLog("regenerateFwAnnotation+ newDir:"+newDir);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("newDir",newDir);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("regenerateFwAnnotation+:"+parameters);
		try{
			String[] antTasks = {"RegenerateFwAnnotation"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("regenerateFwAnnotation Jar file "+newDir,e);
		} 
	}
	
	public static void regenerateSelfGeneratedJars(String ytbView,String fwkView , String sharesDir) {
		UtilityEclipse.pluginLog("regenerateSelfGeneratedJars+ ytbView:"+ytbView+" sharesDir:"+sharesDir+" fwkView:"+fwkView);
		File fileValidation = new File(ytbView+"/DC_YTB/YTBQueueRules/bin/us");
		if(!fileValidation.exists()){
			throw new PluginException("First compile the project:"+fileValidation.getAbsolutePath()+ " does not exists");
		}
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("sharesDir",sharesDir);
		parameterMap.put("ytbView",ytbView);
		parameterMap.put("fwkView",fwkView);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("RegenerateSelfGeneratedJars+:"+parameters);
		try{
			String[] antTasks = {"RegenerateSelfGeneratedJars"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("regenerateSelfGeneratedJars Jar ytbView:"+ytbView+" sharesDir:"+sharesDir,e);
		} 
	}

	public static void runOnPortlet(String wasPortalPath, String user, String pwd, String viewFwk, String xmlNamePath, String port) {
		UtilityEclipse.pluginLog("runOnPortlet+ wasPortalPath:"+wasPortalPath);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("was.path",wasPortalPath);
		parameterMap.put("credential.user",user);
		parameterMap.put("credential.pwd",pwd);
		parameterMap.put("fwk.view",viewFwk);
		parameterMap.put("xmlNamePath",xmlNamePath);
		parameterMap.put("port",port);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("runOnPortlet+:"+parameters);
		try{
			String[] antTasks = {"runOnPortlet"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("portletDeploy failure "+xmlNamePath,e);
		} 
	}
	
	public static void replaceOnFile(String file, String oldLine, String newLine){
		UtilityEclipse.pluginLog("replaceOnFile");
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("file",file);
		parameterMap.put("oldLine",oldLine);
		parameterMap.put("newLine",newLine);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("replaceOnFile+:"+parameters);
		try{
			String[] antTasks = {"FileModification"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Replace on file failed "+file,e);
		} 
	}
	
	public static void copyFile(String file, String newDir){
		UtilityEclipse.pluginLog("CopyFile");
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("file",file);
		parameterMap.put("newDir",newDir);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("CopyFile+:"+parameters);
		try{
			String[] antTasks = {"CopyFile"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Replace on file failed "+file,e);
		} 
	}
	
	public static void copyRcXmlFiles(String newDir, String oldDir) {
		UtilityEclipse.pluginLog("copyRcXmlFiles+ newDir:"+newDir +" oldDir:"+oldDir);
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("newDir",newDir);
		parameterMap.put("oldDir",oldDir);
		String parameters = transformMapToParameters(parameterMap);
		UtilityEclipse.pluginLog("CopyRcXmlFiles+:"+parameters);
		try{
			String[] antTasks = {"CopyRcXmlFiles"};
			String path = Utility.loadProperty("build.path")+"/PluginBuild.xml";
			AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(path);
			runner.setArguments(parameters);
			runner.setExecutionTargets(antTasks);
			runner.run();
		}catch (CoreException e) {
			throw new PluginException("Error copying RC configuration files",e);
		}
	}
	
	
	

}
