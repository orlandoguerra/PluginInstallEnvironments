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
public class UpdateJarsDialog extends TitleAreaDialog {


  private Combo comboDropDown;
  private List<String> bipDirectories;
  private String pfaVersion;
  private String currentPfaVersion;


  public UpdateJarsDialog(Shell parentShell, List<String> bipDirectories, String currentPfaVersion) {
    super(parentShell);
    this.bipDirectories = bipDirectories;
    this.currentPfaVersion = currentPfaVersion;
  }

  @Override
  public void create() {
    super.create();
    setTitle("Please enter required information");
    setMessage("Select the required version", IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout layout = new GridLayout(3, false);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    container.setLayout(layout);

    createDynamicFolder(container);
    
    return area;
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
	  pfaVersion = comboDropDown.getText();
  }
  
 

  @Override
  protected void okPressed() {
    saveInput();
    String errors = "";
    
	  if(pfaVersion == null || pfaVersion.equals(""))
		  errors = "Please, provide a jar version \n";
	  

    if(errors.equals("")){
    	super.okPressed();
    }else{
    	setErrorMessage(errors);
    }
    
  }

  public String getPfaVersion() {
		return pfaVersion;
  }
  
} 
