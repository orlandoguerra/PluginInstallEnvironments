package framework.plugin.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.Activator;
import framework.plugin.exceptions.PluginException;

public class UtilityEclipse {
	
	/**
	 * The method returns the location of the plug-in
	 * @return a String with the location of the plug-in and it's resources
	 */
	public static String getPluginLocationInternal() {
		File checkDirectory;
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		String bundlelocation = bundle.getLocation();
		File file = new File(bundlelocation);
		int ind = file.getParent().indexOf("file:");  
		String location = file.toString().substring(ind + 6);
		File checkDir = new File(location);
		if (checkDir.exists()) {
			return location;
		} else {
			URL locationUrl = FileLocator.find(bundle, new Path("/"), null);  
			URL fileUrl = null;
			try{
				fileUrl = FileLocator.toFileURL(locationUrl);
			}catch (Exception e) {
				throw new PluginException("Error getting the plugin location: bundle"+bundle,e);
			}
			checkDirectory = new File(fileUrl.getFile());
			if (checkDirectory.exists()) {
				location = checkDirectory.toString();
			} 
		}

		return location;
	}
	
	/**
	 * this method log messages 
	 * @param message the message to be logged
	 */
	public static void pluginLog(String message) {
		framework.Activator.getDefault().getLog().log(
				new Status(Status.INFO, Activator.PLUGIN_ID,message));
	}
	
	/**
	 * This method log some messages and the exception
	 * @param message to be logged
	 * @param e the exception to be logged
	 */
	public static void pluginLog(String message, Exception e) {
		framework.Activator.getDefault().getLog().log(
				new Status(Status.ERROR, Activator.PLUGIN_ID,  
				Status.OK, message, e));
	}
	
	/**
	 * This method creates a variable in the project
	 * @param varName the name of the variable
	 * @param  path of the variable
	 */
	public static void creatPreferenceVariables(String varName, String path) {
		pluginLog("creatPreferenceVariables+ varName: "+varName+ " path: "+path);
		IPath p = new Path(path);
		try {
		JavaCore.setClasspathVariable(varName, p, null);
		} catch (JavaModelException e) {
			e.printStackTrace();
			throw new PluginException("Error creating variable: "+varName,e);
		} 
	}
	
	
	/**
	 * This method gets a project into the workspace based on its .project file
	 * @param projectPath the path to the .project file
	 */
	public static void getProjectIntoWorkspace(String projectPath){
		pluginLog("getProjectIntoWorkspace+ projectPath: "+projectPath);
		try{
			IPath projectDotProjectFile = new Path(projectPath);
			IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription( projectDotProjectFile );
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, null);
			project.open(null);
		}catch (CoreException e) {
			throw new PluginException("Error getting the project in the workspace:"+projectPath,e);
		}
		
	}
	
	/**
	 * This method sets the a value in the preferences
	 * @param key of the value to be saved
	 * @param value to be saved on the preferences
	 */
	public static void setPreferences(String key, String value){
		pluginLog("setPreferences+ key: "+key+ " value:"+value);
		Plugin plugin =  framework.Activator.getDefault();
		Preferences prefs = plugin.getPluginPreferences();
		prefs.setValue(key, value);
		plugin.savePluginPreferences();
	}
	
	/**
	 * This method returns a value from the preferences file 
	 * @param key of the property to be retrieved
	 * @return the String value of the property
	 */
	public static String getPreferences(String key){
		pluginLog("getPreferences+ key: "+key);
		Plugin plugin =  framework.Activator.getDefault();
		Preferences prefs = plugin.getPluginPreferences();
		String value = prefs.getString(key);
		if("".equals(value)){
			return null;
		}
		return value;
	}
	
	/**
	 * This method creates a link source in the project
	 * @param varName the name of the link source
	 * @param  path of the link
	 */
	public static void creatPreferenceLink(String varName, String path) {
		pluginLog("creatPreferenceLink+ varName: "+varName+ " path: "+path);
		IPath p = new Path(path);
		IPathVariableManager linkPath = ResourcesPlugin.getWorkspace().getPathVariableManager();
		try {
			linkPath.setValue(varName, p); 
		}catch (CoreException e) {
			e.printStackTrace();
			throw new PluginException("Error creating link "+varName,e);
		} 
	}

	/**
	 * This method returns the location of the workspace
	 * @return
	 */
	public static String getWorkspaceLocation(){
		pluginLog("getWorkspaceLocation ");
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String folder = workspace.getRoot().getLocation().toFile().getPath().toString(); 
		return folder;
	}
	
	
	/**
	 * Retrieves the current WAS location
	 * @return
	 */
	public static String getWASCurrentConfiguration(){	
		pluginLog("getWASCurrentConfiguration");
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		String pathWASString = null;

		String fileLocation = UtilityEclipse.getWorkspaceLocation()
	            + "\\.metadata\\.plugins\\com.ibm.ws.ast.st.core\\wasInstallConfigCache.xml";
		pluginLog(fileLocation);
		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(new File(fileLocation));
			document.getDocumentElement ().normalize ();
			 NodeList nodelist = document.getElementsByTagName("wasInstalls");
			 Element fstNmElmnt = (Element) nodelist.item(0);
			 pathWASString = fstNmElmnt.getAttribute("location");
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);

		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}		
		
		if (pathWASString == null || pathWASString.equals("")){
			throw new PluginException("Error getting the WAS Location (Empty)");
		}
		return pathWASString;
		
	}
	
	/**
	 * This method retrieves the server.xml file
	 * @return
	 */
	public static File getServerFile(){
		pluginLog("getServerFile");
		String fileLocation = UtilityEclipse.getWorkspaceLocation()
	            + "\\.metadata\\.plugins\\org.eclipse.wst.server.core\\servers.xml";
		pluginLog(fileLocation);
		File file = new File(fileLocation);
		if(!file.exists()){
			throw new PluginException("First add a Portal Server to the workspace");
		}
		return file;
	}
	
	
	/**
	 * This method retrieves the configuration of the server.xml
	 * @param file the original file server.xml
	 * @return
	 */
	public static Map<String, String> getServerConfiguration(File file){
		pluginLog("getServerConfiguration:"+file.getAbsolutePath());
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		Map<String, String> serverValues = null;
		
		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement ().normalize ();
			NodeList nodelist = document.getElementsByTagName("server");
			
			for(int i=0; i< nodelist.getLength(); i++){
				Element elElmnt = (Element) nodelist.item(i);
				String serverType = elElmnt.getAttribute("baseServerName");
				 if("WebSphere_Portal".equals(serverType)){
					 serverValues = new HashMap<String,String>();
					 serverValues.put("webSphereProfileName", elElmnt.getAttribute("webSphereProfileName"));
					 serverValues.put("portal.installLocation", elElmnt.getAttribute("portal.installLocation"));
					 return serverValues;
				 }
			}
			throw new PluginException("First add a Portal Server to the workspace (missing configuration)");
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}	
		
	}
	
	/**
	 * This method retrieves the serverindex.xml file
	 * @param wasParentPath the path of the parent path of the was installed
	 * @param profileName the name of the portal profile used in the workspace
	 * @return
	 */
	public static File getServerIndex(String wasParentPath, String profileName){
		pluginLog("getServerIndex");
		String directory = new File(wasParentPath).getAbsoluteFile().getParentFile().getAbsolutePath();
		String path  = directory+"\\"+profileName+"\\config\\cells";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Cell")+"\\nodes";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Node")+"\\serverindex.xml";
		pluginLog("getServerIndex Final: "+path);
		File file = new File(path);
		if(!file.exists()){
			throw new PluginException("Server Index was not found");
		}
		return file;
	}
	
	/**
	 * This method retrieves the file variables.xml
	 * @param wasParentPath the path of the parent path of the was installed
	 * @param profileName the name of the portal profile used in the workspace
	 * @return
	 */
	public static File getProfileVariable(String wasParentPath, String profileName){
		pluginLog("getProfileVariable");
		String path  = wasParentPath+"\\"+profileName+"\\config\\cells";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Cell")+"\\nodes";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Node")+"\\variables.xml";
		
		File file = new File(path);
		if(!file.exists()){
			throw new PluginException("Server Variable was not found");
		}
		return file;
	}
	
	/**
	 * This method retrieves the next directory inside of the provided path that 
	 * finished with the provided string
	 * @param path the path of the directory to be scanned
	 * @param finishWith the string 
	 * @return
	 */
	public static String getNextDirectory(String path, String finishWith){
		pluginLog("getNextDirectory:"+path+","+finishWith);
		File file = new File(path);
		File[] fList = file.listFiles();
		for (File fileName : fList) {
			if(fileName.isDirectory()&& fileName.getName().endsWith(finishWith)){
				return fileName.getName();
			}
		}
		throw new PluginException("The directory configuration was not found.");
	}
	
	/**
	 * This method retrieves the name of the portal server used in the profile name
	 * @param wasParentPath the path of the was root path
	 * @param profileName the name of the profile
	 * @return
	 */
	public static String getWasInstallRoot(String wasParentPath, String profileName){
		pluginLog("getWasInstallRoot:"+wasParentPath+","+profileName);
		File file = UtilityEclipse.getProfileVariable(wasParentPath, profileName);
		pluginLog("get variable  file:"+file.getAbsolutePath());
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		
		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement ().normalize ();
			NodeList nodelist = document.getElementsByTagName("entries");
			
			for(int i=0; i< nodelist.getLength(); i++){
				Element elElmnt = (Element) nodelist.item(i);
				String endPoint = elElmnt.getAttribute("symbolicName");
				 if("WAS_INSTALL_ROOT".equals(endPoint)){
					String wasInstallation = elElmnt.getAttribute("value");
					if(wasInstallation == null || "".equals(wasInstallation)){
						throw new PluginException("The directory configuration was not found.");
					}
					return wasInstallation;
				 }
			}
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}	
		throw new PluginException("The was intallation root is not available");
	}
	
	/**
	 * The name of the websphere parent directory
	 * @return
	 */
	private static String getWebShpereGeneralDirectory(){
		String webShepere = getWASCurrentConfiguration();
		File file = new File(webShepere);
		String generalDirectory = file.getParent();
		return generalDirectory;
	}
	
	/**
	 * Retrieves a map  with the current configuration of the portal server
	 * @return
	 */
	public static Map<String, String> getServerConfigurationMap(){
		File serverFile = UtilityEclipse.getServerFile();
		Map<String, String> serverConfig = UtilityEclipse.getServerConfiguration(serverFile);
		serverConfig.put("general.directory",UtilityEclipse.getWebShpereGeneralDirectory());
		String wasInstallPortal = UtilityEclipse.getWasInstallRoot(serverConfig.get("general.directory"), serverConfig.get("webSphereProfileName"));
		serverConfig.put("wasInstallPortal",wasInstallPortal);
		serverConfig.put("was.server",UtilityEclipse.getWASCurrentConfiguration());
		serverConfig.put("portal.port",UtilityEclipse.getPortalPort(serverConfig.get("webSphereProfileName"), serverConfig.get("general.directory")));
		pluginLog("Configuration:"+serverConfig);
		return serverConfig;
	}
	
	
	
	public static String getShareAppLocation(){
		pluginLog("getShareAppLocation");
		File file = UtilityEclipse.getServerFile();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;

		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement ().normalize ();
			NodeList nodelist = document.getElementsByTagName("list");
			
			for(int i=0; i< nodelist.getLength(); i++){
				Element elElmnt = (Element) nodelist.item(i);
				String serverType = elElmnt.getAttribute("key");
				 if("rft_connection_data".equals(serverType)){
					String rftData = elElmnt.getAttribute("value0");
					if(rftData!=null){
						pluginLog(rftData);
						String result = rftData.replace("||", "@");
						String[] values = result.split("@");
						for (String string : values) {
							if(string.startsWith("C:")){
								return string;
							}
						}
					}
				 }
			}
			throw new PluginException("First add a Portal Server to the workspace (missing RFT configuration)");
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}	
		
	}
	
	
	public static String getWASCurrentProfile(File file){	
		pluginLog("getWASCurrentProfile");
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		
		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement ().normalize ();
			NodeList nodelist = document.getElementsByTagName("server");
			
			for(int i=0; i< nodelist.getLength(); i++){
				Element elElmnt = (Element) nodelist.item(i);
				String serverType = elElmnt.getAttribute("baseServerName");
				 if("server1".equals(serverType)){
					 return elElmnt.getAttribute("webSphereProfileName");
				 }
			}
			return null;
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}	
		
	}
	
	public static String getRestPort(){	
		pluginLog("getRestPort");
		File serverFile = UtilityEclipse.getServerFile();
		String profile = getWASCurrentProfile(serverFile);
		if(profile==null){
			return "<Port>";
		}
		File file = getServerIndex(profile);
		return returnDefaultPort(file);
	}
	
	public static String getPortalPort(String profileName, String generalDirectory){
		pluginLog("getPortalPort");
		String path  = generalDirectory+"\\"+profileName+"\\config\\cells";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Cell")+"\\nodes";
		path = path + "\\" + UtilityEclipse.getNextDirectory(path, "Node")+"\\serverindex.xml";
		File file = new File(path);
		if(!file.exists()){
			throw new PluginException("Portal Server Index was not found");
		}
		
		return returnDefaultPort(file);
	
	}
	
	
	public static String returnDefaultPort(File serverFile){
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder;
		
		try {
			builder = domFactory.newDocumentBuilder();
			Document document = builder.parse(serverFile);
			document.getDocumentElement ().normalize ();
			NodeList nodelist = document.getElementsByTagName("specialEndpoints");
			 
			 for(int i=0; i< nodelist.getLength(); i++){
					Element elElmnt = (Element) nodelist.item(i);
					String hostType = elElmnt.getAttribute("endPointName");
					 if("WC_defaulthost".equals(hostType)){
						 NodeList nodelist1 = elElmnt.getElementsByTagName("endPoint");
						 String port =((Element)nodelist1.item(0)).getAttribute("port");
						 return port;
					 }
				}
			 
		} catch (SAXException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (ParserConfigurationException e) {
			throw new PluginException("Error getting the WAS Location",e);
		} catch (IOException e) {
			throw new PluginException("Error getting the WAS Location",e);
		}
		
		
		return null;
	}
		
	
	public static File getServerIndex(String profile){
		pluginLog("getServerIndex");
		
		if(profile == null){
			return null;
		}
		
		String pathWAS = UtilityEclipse.getWASCurrentConfiguration();
		pluginLog("getServerIndex");
		String path  = pathWAS+"/profiles/"+profile+"/config/cells/YTBCell/nodes/YTB/serverindex.xml";
		System.out.println(path);
		File file = new File(path);
		if(!file.exists()){
			return null;
		}
		return file;
	}
	
}
