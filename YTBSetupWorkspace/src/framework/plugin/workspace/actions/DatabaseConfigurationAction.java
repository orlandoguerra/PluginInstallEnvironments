package framework.plugin.workspace.actions;

import java.util.List;

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
import framework.plugin.workspace.dialogs.DataBaseDialog;

public class DatabaseConfigurationAction  implements IWorkbenchWindowActionDelegate{
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
		
	    String currentProfile = UtilityEclipse.getPreferences(Constants.CURRENT_PROFILE) == null?"":UtilityEclipse.getPreferences(Constants.CURRENT_PROFILE);
	    String currentDB = UtilityEclipse.getPreferences(Constants.CURRENT_DB) == null?"":UtilityEclipse.getPreferences(Constants.CURRENT_DB);
	    String pathWAS = UtilityEclipse.getWASCurrentConfiguration();

		List<String> phytonScripts =  Utility.getFileInsideFilterStartsWith(Utility.loadExternalProperty("phyton.scripts.path"),"YtbConfigureDataSource");
		List<String> existingProfiles =  Utility.getDirectoriesInside(pathWAS+"/profiles");
		DataBaseDialog dialog = new DataBaseDialog(shell, phytonScripts, existingProfiles, currentProfile, currentDB);
		dialog.create();
		if (dialog.open() == Window.OK) {
			Utility.executePhython(dialog.getValueScript(), dialog.getValueProfile(), pathWAS, "");
			UtilityEclipse.setPreferences(Constants.CURRENT_DB, dialog.getValueScript());
			MessageDialog.openInformation(
					window.getShell(),
					"Database configured correctly",
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
