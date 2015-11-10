package framework.plugin.workspace.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/*
 * Dialog to set up the workspace
 */
public class WorkspaceInputPortalDialog extends TitleAreaDialog {

  private Text txtFolderYTB;
  private String valueFolderYTB;
  private Text txtFolderFwk;
  private String valueFolderFwk;
  
  private Combo comboDropDown;
  private List<String> bipDirectories;
  private String pfaVersion;
  private String currentPfaVersion;


  public WorkspaceInputPortalDialog(Shell parentShell, List<String> bipDirectories, String currentPfaVersion) {
    super(parentShell);
    this.bipDirectories = bipDirectories;
    this.currentPfaVersion = currentPfaVersion;
  }

  @Override
  public void create() {
    super.create();
    setTitle("Please enter required information");
    setMessage("Select the folders from your system", IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout = new GridLayout(3, false);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    container.setLayout(layout);

    createFolderDialogYTB(container);
    createFolderDialogFwk(container);
    createDynamicFolder(container);
    
    return area;
  }

  private void createFolderDialogYTB(Composite container) {

	new Label(container, SWT.NONE).setText("Select your YTB ClearCase view folder:");

    GridData dataValueFolder = new GridData();
    dataValueFolder.grabExcessHorizontalSpace = true;
    dataValueFolder.horizontalAlignment = GridData.FILL;

    txtFolderYTB = new Text(container, SWT.BORDER);
    txtFolderYTB.setLayoutData(dataValueFolder);
    txtFolderYTB.setEditable(false);
    
    Button buttonBrowse = new Button(container, SWT.PUSH);
    buttonBrowse.setText("Browse"); 
    buttonBrowse.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	        DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
	        directoryDialog.setFilterPath("c:/clearcase/"); 
	        directoryDialog.setMessage("Please select clearcase directory(E.g. YTB_LTSS_94.0.0.0_LOCAL) and click OK"); 
	        txtFolderYTB.setText(directoryDialog.open());
	        saveInput();
	      }			
	    });
  }
  
  
  private void createFolderDialogFwk(Composite container) {

	    new Label(container, SWT.NONE).setText("Select your FWK ClearCase view folder:");

	    GridData dataValueFolder = new GridData();
	    dataValueFolder.grabExcessHorizontalSpace = true;
	    dataValueFolder.horizontalAlignment = GridData.FILL;

	    txtFolderFwk = new Text(container, SWT.BORDER);
	    txtFolderFwk.setLayoutData(dataValueFolder);
	    txtFolderFwk.setEditable(false);
	    
	    Button buttonBrowse = new Button(container, SWT.PUSH);
	    buttonBrowse.setText("Browse"); 
	    buttonBrowse.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event event) {
		        DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
		        directoryDialog.setFilterPath("c:/clearcase/"); 
		        directoryDialog.setMessage("Please select clearcase directory(E.g. FW_1.0.0.0_LOCAL) and click OK"); 
		        txtFolderFwk.setText(directoryDialog.open());
		        saveInput();
		      }			
		    });
	  }
  
  private void createDynamicFolder(Composite container){
	  new Label(container, SWT.NONE).setText("PFA Version Name:");   
	  
		comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER  | SWT.READ_ONLY); 
		comboDropDown.add("");
		int index = 0;
		int counter = 0;
		for (String bipDir : bipDirectories) {
			 comboDropDown.add(bipDir);
			 counter++;
			 if(currentPfaVersion.equals(bipDir)){
				  index = counter;
			 }
	    }
		comboDropDown.select(index);
		new Label(container, SWT.NONE).setText("");
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  /**
   * This method saves the inputs into variables
   */
  private void saveInput() {
	  valueFolderYTB = txtFolderYTB.getText();
	  valueFolderFwk = txtFolderFwk.getText();
	  pfaVersion = comboDropDown.getText();
  }
  
 

  @Override
  protected void okPressed() {
    saveInput();
    String errors = "";
	  
	  if(valueFolderYTB == null || valueFolderYTB.equals(""))
		  errors = "Please, select a YTB clearcase view \n";
	  
	  if(valueFolderFwk == null || valueFolderFwk.equals(""))
		  errors = "Please, select a Fwk clearcase view \n";
	  
	  if(pfaVersion == null || pfaVersion.equals(""))
		  errors = "Please, provide a jar version \n";
	  

    if(errors.equals("")){
    	super.okPressed();
    }else{
    	setErrorMessage(errors);
    }
    
  }

  public String getValueFolderYTB() {
    return valueFolderYTB;
  }
  
  
  public String getValueFolderFwk() {
	return valueFolderFwk;
  }  
  
  public String getPfaVersion() {
		return pfaVersion;
  }
  
} 
