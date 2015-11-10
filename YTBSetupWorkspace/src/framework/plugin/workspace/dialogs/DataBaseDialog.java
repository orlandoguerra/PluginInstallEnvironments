package framework.plugin.workspace.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class DataBaseDialog  extends TitleAreaDialog {

  private Combo comboDropDown;
  private Combo scriptDropDown;
  private String valueProfile;
  private String valueScript;
  private String currentDB;
  private List<String> phytonScripts;
  private List<String> profiles;
  private String currentProfile;
  
  public DataBaseDialog(Shell parentShell, List<String> phytonScripts,
		  List<String> profiles, String currentProfile, String currentDB ) {
    super(parentShell);
    
    this.phytonScripts = phytonScripts;
    this.profiles = profiles;
    this.currentProfile = currentProfile;
    this.currentDB = currentDB;
    
  }

  public void create() {
	    super.create();
	    setTitle("Configure Database");
	    setMessage("Select a profile and Database Configuration", IMessageProvider.INFORMATION);
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
		  new Label(container, SWT.NONE).setText("Profile Name:");
		  comboDropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER  | SWT.READ_ONLY); 
		  comboDropDown.add("");
		  int index = 0;
		  int counter = 0;
		  for (String profileDir : profiles) {
			  comboDropDown.add(profileDir);
			  counter++;
			  if(profileDir.equals(currentProfile)){
				  index = counter;
			  }
		  }
		  comboDropDown.select(index);
		  new Label(container, SWT.NONE).setText("");   
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

	  @Override
	  protected boolean isResizable() {
	    return true;
	  }
	  
	  /**
	   * This method saves the inputs into variables
	   */
	  private void saveInput() {
		  valueProfile = comboDropDown.getText();
		  valueScript = scriptDropDown.getText();
	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    String errors = "";
		  if(valueProfile.equals(""))
			  errors = "Please, enter a valid name for the profile \n";
		  if(valueScript.equals(""))
			  errors = "Please, select a valid database \n";
		  
	    if(errors.equals("")){
	    	super.okPressed();
	    }else{
	    	setErrorMessage(errors);
	    }
	    
	  }

	  public String getValueProfile() {
			return valueProfile;
	  }
	  
	  public String getValueScript() {
			return valueScript;
	  }

	  public void setValueScript(String valueScript) {
			this.valueScript = valueScript;
	  }


	} 
