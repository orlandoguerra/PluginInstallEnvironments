package framework.plugin.workspace.actions;

import java.io.File;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import framework.Activator;
import framework.plugin.exceptions.PluginException;
import framework.plugin.util.Constants;
import framework.plugin.util.Utility;
import framework.plugin.util.UtilityEclipse;
import framework.plugin.workspace.dialogs.WorkspaceInputPortalDialog;

public class ConfigurePortalAction  implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;

	@Override
	public void run(IAction arg0) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		try{
		
		String bipDirectory =  Utility.loadExternalProperty("bip.developement");
		List<String> bipDirectories=  Utility.getDirectoriesInsideStartsWith(bipDirectory,"PFA_");
		String pfaVersion = UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION) == null?"PFA_latest":UtilityEclipse.getPreferences(Constants.CURRENT_PFA_VERSION);
		WorkspaceInputPortalDialog dialog = new WorkspaceInputPortalDialog(shell,bipDirectories,pfaVersion );
		dialog.create();
		if (dialog.open() == Window.OK) {
			
			
			String jarTIERfolder = Utility.getJarVersion(dialog.getValueFolderYTB());
			String pathWAS = UtilityEclipse.getWASCurrentConfiguration();
			

			String generalPath = Utility.loadExternalProperty("general.path");
			
			Utility.copyJarContent(dialog.getValueFolderFwk()+"/DC_FWK/LIB", bipDirectory+"/"+dialog.getPfaVersion()+"/FW_Jars");
			Utility.copyJarContent(dialog.getValueFolderFwk()+"/DC_FWK/LIB", generalPath+"\\JARS\\FWKIncludedJars");
			Utility.copyJarContent(dialog.getValueFolderFwk()+"/DC_FWK/LIB", generalPath+"\\JARS\\FWKYTBProperties");
			Utility.copyJarContent(dialog.getValueFolderYTB()+"/DC_YTB/LIB", generalPath+"\\JARS\\YTBIncludedJars");
			Utility.copyJarContent(dialog.getValueFolderYTB()+"/DC_YTB/LIB", Utility.loadExternalProperty("TIERS.jars.version")+"/"+jarTIERfolder);
			Utility.copyDirectoryContent(dialog.getValueFolderYTB(), generalPath+"\\ReplaceContents\\YTBnewContent");
			
			File fileJava = new File(dialog.getValueFolderYTB());
			String simpleView = fileJava.getName();
			
			Utility.updateTokenProperties(dialog.getValueFolderFwk()+"/DC_FWK/LIB/properties.jar",simpleView, "@@@");
			
			UtilityEclipse.creatPreferenceVariables(Constants.YTB_VIEW_SRC, dialog.getValueFolderYTB());
			UtilityEclipse.creatPreferenceVariables(Constants.FWK_VIEW_SRC, dialog.getValueFolderFwk());
			UtilityEclipse.creatPreferenceVariables(Constants.WAS_PATH_SRC, pathWAS);
			UtilityEclipse.creatPreferenceLink(Constants.YTB_LINK, dialog.getValueFolderYTB());
			
			String configPath = Utility.loadExternalProperty("configuration.path");
			/*
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/BO", configPath+"\\BO");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/DAO", configPath+"\\DAO");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/DO", configPath+"\\DO");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/SBO", configPath+"\\SBO");
			Utility.copyDirectoryContent(dialog.getValueFolderFwk()+"/DC_FWK/RSFW", configPath+"\\RSFW");
			Utility.copyDirectoryContent(dialog.getValueFolderFwk()+"/DC_FWK/FW", configPath+"\\FW");
			*/
			
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBOSSPortal", configPath+"\\YTBOSSPortal");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBSTPPortal", configPath+"\\YTBSTPPortal");
			
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBPortal", configPath+"\\YTBPortal");
			Utility.copyDirectoryContent(dialog.getValueFolderFwk()+"/DC_FWK/DTCUIFwk", configPath+"\\DTCUIFwk");
			Utility.copyDirectoryContent(dialog.getValueFolderFwk()+"/DC_FWK/FwAnnotationProcessor", configPath+"\\FwAnnotationProcessor");
			Utility.copyDirectoryContent(dialog.getValueFolderFwk()+"/DC_FWK/WksUtils", configPath+"\\WksUtils");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBAPPPortal", configPath+"\\YTBAPPPortal");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBBasePortlet", configPath+"\\YTBBasePortlet");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBCasesPortal", configPath+"\\YTBCasesPortal");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBNavigatorPortal", configPath+"\\YTBNavigatorPortal");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBQueueRules", configPath+"\\YTBQueueRules");
			Utility.copyDirectoryContent(dialog.getValueFolderYTB()+"/DC_YTB/YTBAnonymousPortal", configPath+"\\YTBAnonymousPortal");
			
			
			/*
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderFwk()+"/DC_FWK/RSFW/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderFwk()+"/DC_FWK/FW/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/BO/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/DAO/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/DO/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/SBO/.project");
			*/

			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBOSSPortal/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBSTPPortal/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBPortal/.project");	
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderFwk()+"/DC_FWK/DTCUIFwk/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderFwk()+"/DC_FWK/FwAnnotationProcessor/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderFwk()+"/DC_FWK/WksUtils/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBAPPPortal/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBBasePortlet/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBCasesPortal/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBNavigatorPortal/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBQueueRules/.project");
			UtilityEclipse.getProjectIntoWorkspace(dialog.getValueFolderYTB()+"/DC_YTB/YTBAnonymousPortal/.project");
			
			Utility.regenerateFwAnnotation(dialog.getValueFolderFwk());
			
			UtilityEclipse.setPreferences(Constants.YTB_VIEW,dialog.getValueFolderYTB());
			UtilityEclipse.setPreferences(Constants.FWK_VIEW,dialog.getValueFolderFwk());
		
			Utility.addCounter(Activator.PLUGIN_ID+".Portal");
			
			MessageDialog.openInformation(
					window.getShell(),
					"Release Notes",
					"Configuration complete");
			
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
