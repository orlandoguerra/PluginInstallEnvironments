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


public class SetupPortalEARDialog  extends TitleAreaDialog {

	  private Text txtFolder;
	  private String valueFolder;
	  private String fwkView;
	  private Combo comboDropDown;
	  private String userBefore;
	  private String pwdBefore;
	  private Text textPwd;
	  private Text textUser;
	  private String user;
	  private String pwd;
	  private List<String> bipDirectories;
	  private String pfaVersion;
	  private Button buttonJars;
	  private String sharesPath;
	  private boolean valueSelected;
	

	public SetupPortalEARDialog(Shell parentShell, List<String> bipDirectories,String fwkView, 
			  String userBefore, String pwdBefore, String sharesPath) {
	    super(parentShell);
	    this.fwkView = fwkView;
	    this.userBefore = userBefore;
	    this.pwdBefore = pwdBefore;
	    this.bipDirectories = bipDirectories;
	    this.sharesPath = sharesPath;
	  }

	 public void create() {
		   super.create();
		   setTitle("Configure EAR/WAR Files");
		   setMessage("Select the EAR/WAR configuration of the portal (the server should be running)", IMessageProvider.INFORMATION);
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

	  /**
	   * Creates the body of the Dialog area
	   * @param container the composite area
	   */
	  private void createBodyDialog(Composite container) {
		
		new Label(container, SWT.NONE).setText("User Name:");
		textUser = new Text(container, SWT.BORDER);
		textUser.setText(userBefore);
		new Label(container, SWT.NONE).setText("");   
				
		new Label(container, SWT.NONE).setText("Portal Password:");
		textPwd = new Text(container, SWT.BORDER);
		textPwd.setSize(35, 15); 
		textPwd.setText(pwdBefore);
		new Label(container, SWT.NONE).setText("");  
		
		new Label(container, SWT.NONE).setText("PFA Version Name:");   
		  
		comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER  | SWT.READ_ONLY); 
		comboDropDown.add("");
		int index = 0;
		int counter = 0;
		for (String bipDir : bipDirectories) {
			 comboDropDown.add(bipDir);
			 counter++;
			 if("PFA_latest".equals(bipDir)){
				  index = counter;
			 }
	    }
		comboDropDown.select(index);
		new Label(container, SWT.NONE).setText("");

		Label lbtValueFolder = new Label(container, SWT.NONE);
		lbtValueFolder.setText("Select your FWK ClearCase view folder:");

		GridData dataValueFolder = new GridData();
		dataValueFolder.grabExcessHorizontalSpace = true;
		dataValueFolder.horizontalAlignment = GridData.FILL;

		txtFolder = new Text(container, SWT.BORDER);
		txtFolder.setLayoutData(dataValueFolder);
		txtFolder.setEditable(false);
		txtFolder.setText(fwkView);
		    
		Button buttonBrowse = new Button(container, SWT.PUSH);
		buttonBrowse.setText("Browse"); 
		buttonBrowse.addListener(SWT.Selection, new Listener() {
			 public void handleEvent(Event event) {
			     DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
			     directoryDialog.setFilterPath("c:/"); 
			     directoryDialog.setMessage("Please select clearcase directory(E.g. FW_1.0.0.0_LOCAL) and click OK"); 
			     txtFolder.setText(directoryDialog.open());
			     saveInput();
			  }			
		  });
		
		buttonJars =  new Button(container, SWT.CHECK);
		buttonJars.setSelection(true);
		buttonJars.setText("Update All Fw Jars:");
		
		  Text txtUrlFolder = new Text(container, SWT.BORDER);
		  txtUrlFolder.setText(sharesPath);
		  txtUrlFolder.setEditable(false);
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
			valueFolder = txtFolder.getText();
			pwd =  textPwd.getText();
			user = textUser.getText();
			pfaVersion = comboDropDown.getText();
			valueSelected = buttonJars.getSelection();
		}


	  @Override
	  protected void okPressed() {
	    saveInput();
	    String errors = "";
		  
		  if(valueFolder == null || valueFolder.equals(""))
			  errors = "Please, select a fwk clearcase view \n";
		  
		  if(user == null || user.equals(""))
			  errors = "Please, provide an user name \n";
		 
		  if(pwd == null || pwd.equals(""))
			  errors = "Please, provide password \n";
		  
		  if(pfaVersion == null || pfaVersion.equals(""))
			  errors = "Please, provide an EAR version \n";
		  
	    if(errors.equals("")){
	    	super.okPressed();
	    }else{
	    	setErrorMessage(errors);
	    }
	    
	  }

	  public String getValueFolder() {
	    return valueFolder;
	  }
	  
	  
	  public String getUser() {
			return user;
	  }

      public String getPwd() {
			return pwd;
	  }
      
      public String getPfaVersion() {
  		return pfaVersion;
  	  }
      
      public boolean isValueSelected() {
  		return valueSelected;
  	  }

} 
