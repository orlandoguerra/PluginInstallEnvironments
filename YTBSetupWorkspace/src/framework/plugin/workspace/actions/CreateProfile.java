package framework.plugin.workspace.actions;

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
import framework.plugin.workspace.dialogs.CreateConfigureProfileDialog;

public class CreateProfile  implements IWorkbenchWindowActionDelegate{
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
			
		String pathWAS = UtilityEclipse.getWASCurrentConfiguration();
		String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW) == null?"":UtilityEclipse.getPreferences(Constants.FWK_VIEW);
		CreateConfigureProfileDialog dialog = new CreateConfigureProfileDialog(shell,pathWAS, fwkView);
		dialog.create();
			if (dialog.open() == Window.OK) {
				Utility.createProfileUser(dialog.getValueProfile(),pathWAS );
				Utility.executePhython("YtbConfiguration.py", dialog.getValueProfile(),pathWAS , dialog.getValueFwkFolder());
				UtilityEclipse.setPreferences(Constants.CURRENT_PROFILE,dialog.getValueProfile());
				MessageDialog.openInformation(
						window.getShell(),
						"Profile Creation Complete",
						"Profile "+dialog.getValueProfile() + " Created Succesfully");
			} 
		}catch (PluginException e) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error:",e.getMessage());
		}catch (Exception ex) {
			MessageDialog.openInformation(window.getShell(),"Plugin Error","Unknow exception review the logs");
			UtilityEclipse.pluginLog("Exception",ex);
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
