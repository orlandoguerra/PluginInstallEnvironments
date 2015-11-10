package framework.plugin.workspace.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import framework.plugin.exceptions.PluginException;
import framework.plugin.util.Constants;
import framework.plugin.util.Utility;
import framework.plugin.util.UtilityEclipse;

public class UpdateSettingsAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	
	/**
	 * The constructor.
	 */
	public UpdateSettingsAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		
		try{
			
		window.getShell();
		String configPath = Utility.loadExternalProperty("configuration.path");
		
		String ytbView = UtilityEclipse.getPreferences(Constants.YTB_VIEW);
		String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW);
		
		if(ytbView == null || ytbView.equals("")){
			throw new PluginException("Please, first use the setup YTB workspace functionality");
		}

		Utility.copyDirectoryContent(ytbView+"/DC_YTB/BO", configPath+"\\BO");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/DAO", configPath+"\\DAO");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/DO", configPath+"\\DO");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/SBO", configPath+"\\SBO");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBWEB", configPath+"\\YTBWEB");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/WSEJB", configPath+"\\WSEJB");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/WSWEB", configPath+"\\WSWEB");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTB_EAR", configPath+"\\YTB_EAR");
		Utility.copyDirectoryContent(fwkView+"/DC_FWK/RSFW", configPath+"\\RSFW");
		Utility.copyDirectoryContent(fwkView+"/DC_FWK/FW", configPath+"\\FW");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBGeneratePDF", configPath+"\\YTBGeneratePDF");
		
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBOSSPortal", configPath+"\\YTBOSSPortal");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBSTPPortal", configPath+"\\YTBSTPPortal");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBPortal", configPath+"\\YTBPortal");
		Utility.copyDirectoryContent(fwkView+"/DC_FWK/DTCUIFwk", configPath+"\\DTCUIFwk");
		Utility.copyDirectoryContent(fwkView+"/DC_FWK/FwAnnotationProcessor", configPath+"\\FwAnnotationProcessor");
		Utility.copyDirectoryContent(fwkView+"/DC_FWK/WksUtils", configPath+"\\WksUtils");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBAPPPortal", configPath+"\\YTBAPPPortal");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBBasePortlet", configPath+"\\YTBBasePortlet");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBCasesPortal", configPath+"\\YTBCasesPortal");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBNavigatorPortal", configPath+"\\YTBNavigatorPortal");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBQueueRules", configPath+"\\YTBQueueRules");
		Utility.copyDirectoryContent(ytbView+"/DC_YTB/YTBAnonymousPortal", configPath+"\\YTBAnonymousPortal");
		
		MessageDialog.openInformation(
				window.getShell(),
				"Settings Updated",
				"Settings updated successfully, refresh your projects");
		
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
