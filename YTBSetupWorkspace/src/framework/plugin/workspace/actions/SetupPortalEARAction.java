package framework.plugin.workspace.actions;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import framework.plugin.exceptions.PluginException;
import framework.plugin.util.Constants;
import framework.plugin.util.Utility;
import framework.plugin.util.UtilityEclipse;
import framework.plugin.workspace.dialogs.SetupPortalEARDialog;

public class SetupPortalEARAction  implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	
	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {

		
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		try{
		String bipDirectory =  Utility.loadExternalProperty("bip.developement");
		String buildDirectory =  Utility.loadProperty("build.path");
		Map<String, String> serverConfig = UtilityEclipse.getServerConfigurationMap();
		String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW) == null?"":UtilityEclipse.getPreferences(Constants.FWK_VIEW);
		String userPortal = UtilityEclipse.getPreferences(Constants.PORTAL_USER) == null?"":UtilityEclipse.getPreferences(Constants.PORTAL_USER);
		String pwdPortal = UtilityEclipse.getPreferences(Constants.PORTAL_PWD) == null?"":UtilityEclipse.getPreferences(Constants.PORTAL_PWD);
		List<String> bipDirectories=  Utility.getDirectoriesInsideStartsWith(bipDirectory,"PFA_");
		String shareLocation = UtilityEclipse.getShareAppLocation();
		SetupPortalEARDialog dialog = new SetupPortalEARDialog(shell, bipDirectories, fwkView, userPortal,pwdPortal,shareLocation);
		dialog.create();
		if (dialog.open() == Window.OK) {
			String deployEarsLocation = dialog.getValueFolder()+"/deployEars";
			
			Utility.copyDirectoryContent(deployEarsLocation, Utility.loadExternalProperty("cache.monitor.path"));
			Utility.copyDirectoryContent(deployEarsLocation, bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Portal");
			Utility.copyDirectoryContent(deployEarsLocation, bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Services");
			Utility.copyDirectoryContent(deployEarsLocation, bipDirectory+"/"+dialog.getPfaVersion()+"/SampleApps/HelloWorld");
			Utility.copyFile(bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Properties/fwVersion.properties", shareLocation);
			
			Utility.executePhythonCredential("YTBPortalConfigEARapps.py", serverConfig.get("webSphereProfileName"),  
					serverConfig.get("wasInstallPortal"),deployEarsLocation, dialog.getUser() , dialog.getPwd());
			
			Utility.runOnPortlet(serverConfig.get("portal.installLocation"),dialog.getUser(),dialog.getPwd(), 
					dialog.getValueFolder(),buildDirectory+"/fwPortletDeploy.xml",serverConfig.get("portal.port"));
			
			Utility.runOnPortlet(serverConfig.get("portal.installLocation"),dialog.getUser(),dialog.getPwd(),
					dialog.getValueFolder(),bipDirectory+"/"+dialog.getPfaVersion()+"/SampleDeployScripts/"+"fwRegisterYTBTheme.xml",
					serverConfig.get("portal.port"));
			
			Utility.runOnPortlet(serverConfig.get("portal.installLocation"),dialog.getUser(),dialog.getPwd(),
					dialog.getValueFolder(),bipDirectory+"/"+dialog.getPfaVersion()+"/SampleDeployScripts/fwPortletCloneDeploy.xml",
					serverConfig.get("portal.port"));
			
			if(dialog.isValueSelected()){
				Utility.copyJarContent(shareLocation, bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Jars");
				Utility.copyJarContent(dialog.getValueFolder()+"/DC_FWK/LIB", bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Jars");
			}
			
			UtilityEclipse.setPreferences(Constants.FWK_VIEW,dialog.getValueFolder());
			UtilityEclipse.setPreferences(Constants.PORTAL_USER,dialog.getUser());
			UtilityEclipse.setPreferences(Constants.PORTAL_PWD,dialog.getPwd());
			
			MessageDialog.openInformation(
					window.getShell(),
					"EARs/WARs configured correctly",
					"Restart your server");
		}
				
		}catch (PluginException e) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error:",e.getMessage());
		}catch (Exception ex) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error","Unknow exception review the logs");
			UtilityEclipse.pluginLog("Exception",ex);
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {}

	@Override
	public void dispose() { }

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
