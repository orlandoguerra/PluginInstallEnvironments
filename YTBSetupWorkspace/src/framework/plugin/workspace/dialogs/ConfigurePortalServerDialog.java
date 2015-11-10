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


public class ConfigurePortalServerDialog  extends TitleAreaDialog {

	  private Text txtFolder;
	  private String valueFolder;
	  private String fwkView;
	  private Text textPwd;
	  private Text textUser;
	  private String valuePwd;
	  private String valueUser;
	  
	  private String userBefore;
	  private String pwdBefore;
	  private List<String> phytonScripts;
	  private String currentDB;
	  private Combo scriptDropDown;
	  private String valueScript;

	  

	public ConfigurePortalServerDialog(Shell parentShell, List<String> phytonScripts,String fwkView, String userBefore, String pwdBefore, String currentDB) {
	    super(parentShell);
	    this.fwkView = fwkView;
	    this.userBefore = userBefore;
	    this.pwdBefore = pwdBefore;
	    this.phytonScripts = phytonScripts;
	    this.currentDB = currentDB;
	  }

	  public void create() {
		 super.create();
		 setTitle("Configure Portal Server");
		 setMessage("Select the configuration of the portal (the server should be running)", IMessageProvider.INFORMATION);
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
		createDatabaseOption(container);
		return area;
	}
	
	private void createDatabaseOption(Composite container){ 
		  new Label(container, SWT.NONE).setText("Select database:");
		  scriptDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER  | SWT.READ_ONLY); 
		  scriptDropDown.add("");
		  
		  int index1 = 0;
		  int counter1 = 0;
		  for (String scripts : phytonScripts) {
			  scriptDropDown.add(scripts);
			  counter1++;
			  if(scripts.equals(currentDB)){
				  index1 = counter1;
			  }
		  }
		  scriptDropDown.select(index1);
		  new Label(container, SWT.NONE).setText("");   
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
	 
				new Label(container, SWT.NONE).setText("Select your FWK ClearCase view folder:");
	
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
		  valuePwd = textPwd.getText(); 
		  valueScript = scriptDropDown.getText();
		  valueUser = textUser.getText();
	  }

	@Override
	  protected void okPressed() {
	    saveInput();
	    String errors = "";
		  
		if(valueFolder == null || valueFolder.equals(""))
			  errors = "Please, select a fwk clearcase view \n";
	    if(valuePwd == null || valuePwd.equals(""))
			  errors = "Please, provide the pwd \n";
	    if(valueUser == null || valueUser.equals(""))
			  errors = "Please, provide the user \n";
	    if(valueScript.equals(""))
			  errors = "Please, select a valid database \n";
	    
	    if(errors.equals("")){
	    	super.okPressed();
	    }else{
	    	setErrorMessage(errors);
	    }
	  }

	  public String getValueFolder() {
	    return valueFolder;
	  }
	  
	  public String getValuePwd() {
		    return valuePwd;
	 }
	  
	 public String getValueUser() {
			return valueUser;
	 }
	 
	 public String getValueScript() {
			return valueScript;
	  }

} 
