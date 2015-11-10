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
import framework.plugin.workspace.dialogs.ConfigurePortalServerDialog;

public class ConfigurePortalServerAction  implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		try{
			Map<String, String> serverConfig = UtilityEclipse.getServerConfigurationMap();
			String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW) == null?"":UtilityEclipse.getPreferences(Constants.FWK_VIEW);
			String userPortal = UtilityEclipse.getPreferences(Constants.PORTAL_USER) == null?"":UtilityEclipse.getPreferences(Constants.PORTAL_USER);
			String pwdPortal = UtilityEclipse.getPreferences(Constants.PORTAL_PWD) == null?"":UtilityEclipse.getPreferences(Constants.PORTAL_PWD);
			String dbPortal = UtilityEclipse.getPreferences(Constants.CURRENT_PORTAL_DB) == null?"":UtilityEclipse.getPreferences(Constants.CURRENT_PORTAL_DB);
			
			
			List<String> phytonScripts =  Utility.getFileInsideFilterStartsWith(Utility.loadExternalProperty("phyton.scripts.path"),"YtbPortalConfigureDataSource");
			ConfigurePortalServerDialog dialog = new ConfigurePortalServerDialog(shell, phytonScripts, fwkView, userPortal, pwdPortal, dbPortal);
			dialog.create();
			
			if (dialog.open() == Window.OK) {
				
				Utility.executePhythonCredential(dialog.getValueScript(), serverConfig.get("webSphereProfileName"),  serverConfig.get("wasInstallPortal"),
						dialog.getValueFolder(), dialog.getValueUser() , dialog.getValuePwd());

				//This process will run all the missing scripts
				List<String> phytonList =  Utility.getFileInsideFilterStartsWith(Utility.loadExternalProperty("phyton.scripts.path"),"YtbPortalList_");
				for (String script : phytonList) {
					Utility.executePhythonCredential(script, serverConfig.get("webSphereProfileName"),  serverConfig.get("wasInstallPortal"),
							dialog.getValueFolder(), dialog.getValueUser() , dialog.getValuePwd());
				}
				
				UtilityEclipse.setPreferences(Constants.FWK_VIEW,dialog.getValueFolder());
				UtilityEclipse.setPreferences(Constants.CURRENT_PORTAL_DB,dialog.getValueScript());
				UtilityEclipse.setPreferences(Constants.PORTAL_USER,dialog.getValueUser());
				UtilityEclipse.setPreferences(Constants.PORTAL_PWD,dialog.getValuePwd());
				
				MessageDialog.openInformation(
						window.getShell(),
						"Portal Server Configured",
						"Restart your server");
			}

		}catch (PluginException e) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error",e.getMessage());
		}catch (Exception ex) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error","Unknow exception review the logs");
			UtilityEclipse.pluginLog("Exception",ex);
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {}

	@Override
	public void dispose() {}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
