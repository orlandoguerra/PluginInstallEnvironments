package framework.plugin.workspace.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ReinstateEnvironmentDialog extends TitleAreaDialog {

	private String path;
	private String finalPath;
	private Text txtUrlFolder;
	private List<String> bipDirectories;
	private String currentPfaVersion;
	private Combo comboDropDown;
	private String pfaVersion;
	private String sharesPath;

	public ReinstateEnvironmentDialog(Shell parentShell, String path, List<String> bipDirectories, String currentPfaVersion, String sharesPath) {
	    super(parentShell);
	    this.path = path;
	    this.bipDirectories = bipDirectories;
	    this.currentPfaVersion = currentPfaVersion;
	    this.sharesPath = sharesPath;
	  }

	  @Override
	  public void create() {
	    super.create();
	    setTitle("Please enter required information");
	    setMessage("Provide the Rest information", IMessageProvider.INFORMATION);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(3, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);
	    createinputArea(container);
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
	  


	  private void createinputArea(Composite container){
		  new Label(container, SWT.NONE).setText("Shares Location:");
		  Text txtUrlFolderShares = new Text(container, SWT.BORDER);
		  txtUrlFolderShares.setText(sharesPath);
		  txtUrlFolderShares.setEditable(false);
		  new Label(container, SWT.NONE).setText("");
		
		  Label lbtValueFolder = new Label(container, SWT.NONE);
			lbtValueFolder.setText("Provide your URL Rest  Service:");

			GridData dataValueFolder = new GridData();
			dataValueFolder.grabExcessHorizontalSpace = true;
			dataValueFolder.horizontalAlignment = GridData.FILL;

			txtUrlFolder = new Text(container, SWT.BORDER);
			txtUrlFolder.setLayoutData(dataValueFolder);
			txtUrlFolder.setText(path);
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
		  finalPath = txtUrlFolder.getText();
		  pfaVersion = comboDropDown.getText();

	  }
	  

	 

	  @Override
	  protected void okPressed() {
	    saveInput();
	    String errors = "";
	    
	    if(finalPath == null || finalPath.equals(""))
			  errors = "Please, provide value to URL Rest service\n";
	    
	    if(pfaVersion == null || pfaVersion.equals(""))
			  errors = "Please, provide a jar version \n";
	    
	    if(errors.equals("")){
	    	super.okPressed();
	    }else{
	    	setErrorMessage(errors);
	    }
	    
	  }
	  
	  public String getFinalPath() {
			return finalPath;
	  }
	  
	  public String getPfaVersion() {
			return pfaVersion;
	  }
	  
	  
	} 
