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
import framework.plugin.workspace.dialogs.UpdateJarsDialog;

public class UpdateJars implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		try{

			String bipDirectory =  Utility.loadExternalProperty("bip.developement");
			List<String> bipDirectories=  Utility.getDirectoriesInsideStartsWith(bipDirectory,"PFA_");
			String pfaVersion = UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION) == null?"PFA_latest":UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION);	
			UpdateJarsDialog dialog = new UpdateJarsDialog(shell, bipDirectories, pfaVersion);
			dialog.create();
			
			if (dialog.open() == Window.OK) {
				String ytbView = UtilityEclipse.getPreferences(Constants.YTB_VIEW);
				String fwkView = UtilityEclipse.getPreferences(Constants.FWK_VIEW);
				String jarTIERfolder = Utility.getJarVersion(ytbView);
				
				String generalPath = Utility.loadExternalProperty("general.path");
				Utility.copyJarContent(fwkView+"/DC_FWK/LIB", bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Jars");
				Utility.copyJarContent(fwkView+"/DC_FWK/LIB", generalPath+"\\JARS\\FWKIncludedJars");
				Utility.copyJarContent(ytbView+"/DC_YTB/LIB", generalPath+"\\JARS\\YTBIncludedJars");
				Utility.copyJarContent(ytbView+"/DC_YTB/LIB", Utility.loadExternalProperty("TIERS.jars.version")+"/"+jarTIERfolder);
				UtilityEclipse.setPreferences(Constants.CURRENT_PFA_VERSION,dialog.getPfaVersion());
				MessageDialog.openInformation(
					window.getShell(),
					"Jars Updated",
					"Jars updated successfully, refresh your projects");
			}
			
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
