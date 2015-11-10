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
import framework.plugin.workspace.dialogs.ReinstateEnvironmentDialog;

public class ReinstateEnvironmentAction  implements IWorkbenchWindowActionDelegate{
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
		
		String restPath = "http://localhost:"+UtilityEclipse.getRestPort();
		Map<String, String> serverConfig = UtilityEclipse.getServerConfigurationMap();
		
		String bipDirectory =  Utility.loadExternalProperty("bip.developement");
		List<String> bipDirectories=  Utility.getDirectoriesInsideStartsWith(bipDirectory,"PFA_");
		String pfaVersion = UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION) == null?"PFA_latest":UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION);
		
		String shareLocation = UtilityEclipse.getShareAppLocation();
		ReinstateEnvironmentDialog dialog = new ReinstateEnvironmentDialog(shell, restPath, bipDirectories, pfaVersion, shareLocation);

		String userPortal = UtilityEclipse.getPreferences(Constants.PORTAL_USER);
		String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW);
		String ytbView = UtilityEclipse.getPreferences(Constants.YTB_VIEW);
		
		if(fwkView==null || userPortal == null){
			throw new PluginException("This option cannot be used, first setup a view and User");
		}
		
		dialog.create();
		if (dialog.open() == Window.OK) {
			Utility.copyFile(fwkView+"/DC_FWK/LIB/Aging/FWSoftAgingPortletFilter.jar", shareLocation);
			Utility.copyFile(fwkView+"/DC_FWK/LIB/Aging/FwSoftAgingServicesFilter.jar", shareLocation);
			
			Utility.regenerateSelfGeneratedJars(ytbView, fwkView, shareLocation);
			Utility.copyRcXmlFiles(shareLocation, ytbView);
			Utility.copyDirectoryContent(shareLocation, Utility.loadExternalProperty("ytb.app"));
			Utility.copyJarContent(shareLocation, bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Jars");
			
			Utility.copyDirectoryContent(shareLocation, ytbView+"/DC_YTB/CENTER/MemberProps");
			Utility.replaceOnFile(shareLocation+"/Member.properties", "@YTB-SSP-RS-Service@/WSWEB", dialog.getFinalPath()+"/WSWEB");
			Utility.replaceOnFile(shareLocation+"/Member.properties", "@YTB-SSP-RS-Service@", "http://localhost:"+serverConfig.get("portal.port"));
			Utility.replaceOnFile(shareLocation+"/fw.properties", "@YTB-SSP-RS-Service@", "http://localhost:"+serverConfig.get("portal.port"));
			
			Utility.replaceOnFile(shareLocation+"/Ytb.properties", "@YTB-SSP-RS-Service@/WSWEB", dialog.getFinalPath()+"/WSWEB");
			Utility.replaceOnFile(shareLocation+"/Ytb.properties", "@YTB-SSP-RS-Service@", "http://localhost:"+serverConfig.get("portal.port"));

			MessageDialog.openInformation(
					window.getShell(),
					"Local environment reinstated",
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
