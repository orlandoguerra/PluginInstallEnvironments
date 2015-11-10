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


public class UpdateFwAnnotationJar implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		try{
			
			window.getShell();
			
			String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW);
			String ytbView = UtilityEclipse.getPreferences(Constants.YTB_VIEW);	
			String shareLocation = UtilityEclipse.getShareAppLocation();
			
			Utility.regenerateFwAnnotation(fwkView);
			Utility.regenerateSelfGeneratedJars(ytbView, fwkView, shareLocation);
			MessageDialog.openInformation(
					window.getShell(),
					"Self generated jars",
					"Self generated jars updated, refresh your projects");
			
			}catch (PluginException e) {
				MessageDialog.openInformation(window.getShell(),"Plugin Error",e.getMessage());
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

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

}
