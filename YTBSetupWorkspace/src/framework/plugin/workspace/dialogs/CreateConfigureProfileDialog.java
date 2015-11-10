package framework.plugin.workspace.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Dialog to create and configure a server profile
 */
public class CreateConfigureProfileDialog  extends TitleAreaDialog {

	  private Text txtFwkFolder;
	  private String valueFwkFolder;
	  private Text textProfile;
	  private String valueProfile;
	  private String fwkView;

	  public CreateConfigureProfileDialog(Shell parentShell, String serverPath, String fwkView) {
	    super(parentShell);
	    this.fwkView = fwkView;
	  }

	  @Override
	  public void create() {
	    super.create();
	    setTitle("Create Profile");
	    setMessage("Please enter required information", IMessageProvider.INFORMATION);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(3, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);

		createBodyDialog(container);
	    return area;
	  }

	  private void createBodyDialog(Composite container) {
		  new Label(container, SWT.NONE).setText("Profile Name:");
		  textProfile = new Text(container, SWT.BORDER);
		  textProfile.setSize(35, 15);  
		  new Label(container, SWT.NONE).setText("");   
		  createFolderDialogFwk(container);
	  }
	  
	  private void createFolderDialogFwk(Composite container) {
		    new Label(container, SWT.NONE).setText("Select your FWK ClearCase view folder:");
		    GridData dataValueFolder = new GridData();
		    dataValueFolder.grabExcessHorizontalSpace = true;
		    dataValueFolder.horizontalAlignment = GridData.FILL;
		    
		    txtFwkFolder = new Text(container, SWT.BORDER);
		    txtFwkFolder.setLayoutData(dataValueFolder);
		    txtFwkFolder.setEditable(false);
		    txtFwkFolder.setText(fwkView);
		    Button buttonBrowse = new Button(container, SWT.PUSH);
		    buttonBrowse.setText("Browse"); 
		    buttonBrowse.addListener(SWT.Selection, new Listener() {
			      public void handleEvent(Event event) {
			        DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			        directoryDialog.setFilterPath("c:/clearcase/"); 
			        directoryDialog.setMessage("Please select clearcase directory(E.g. FW_1.0.0.0_LOCAL) and click OK"); 
			        txtFwkFolder.setText(directoryDialog.open());
			        saveInput();
			      }			
			 });
	 }

	@Override
	  protected boolean isResizable() {
	    return true;
	  }

	  /**
	   * This method saves the inputs into variables
	   */
	  private void saveInput() {
		  valueFwkFolder = txtFwkFolder.getText();
		  valueProfile = textProfile.getText();
	  }


	  @Override
	  protected void okPressed() {
	    saveInput();
	    String errors = "";
		 if(valueFwkFolder == null || valueFwkFolder.equals(""))
			  errors = "Please, select a Fwk clearcase view \n";
		 if(valueProfile.equals(""))
			  errors = "Please, enter a valid name for the profile \n";
		
	    if(errors.equals("")){
	    	super.okPressed();
	    }else{
	    	setErrorMessage(errors);
	    }
	  }

	 
	  
	public String getValueFwkFolder() {
		return valueFwkFolder;
	}

	public String getValueProfile() {
			return valueProfile;
	}


} 
