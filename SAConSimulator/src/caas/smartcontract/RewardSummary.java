package caas.smartcontract;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RewardSummary extends Dialog {

	protected Object result;
	protected Shell shlRewardSummary;
	private Text text;
	private Table tableReportNodes;
	
	public final String HOME_DIR = System.getProperty("user.home");

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public RewardSummary(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlRewardSummary.open();
		shlRewardSummary.layout();
		Display display = getParent().getDisplay();
		while (!shlRewardSummary.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlRewardSummary = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN);
		shlRewardSummary.setSize(430, 600);
		shlRewardSummary.setText("Reward Summary");
		shlRewardSummary.setLayout(null);
		
		Group grpPoliceAgency = new Group(shlRewardSummary, SWT.NONE);
		grpPoliceAgency.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		grpPoliceAgency.setText("Police Agency");
		grpPoliceAgency.setBounds(10, 10, 404, 66);
		
		Label lblTotalReward = new Label(grpPoliceAgency, SWT.NONE);
		lblTotalReward.setFont(SWTResourceManager.getFont("Sans", 14, SWT.BOLD));
		lblTotalReward.setBounds(10, 8, 200, 25);
		lblTotalReward.setText("Total Reward ($):");
		
		text = new Text(grpPoliceAgency, SWT.BORDER);
		text.setText("0");
		text.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		text.setBounds(216, 5, 174, 30);
		
		Group grpReporters = new Group(shlRewardSummary, SWT.NONE);
		grpReporters.setText("Report Nodes");
		grpReporters.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		grpReporters.setBounds(10, 99, 404, 400);
		
		tableReportNodes = new Table(grpReporters, SWT.BORDER | SWT.FULL_SELECTION);
		tableReportNodes.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		tableReportNodes.setBounds(10, 10, 382, 357);
		tableReportNodes.setHeaderVisible(true);
		tableReportNodes.setLinesVisible(true);
		
		TableColumn tblcNodeNo = new TableColumn(tableReportNodes, SWT.NONE);
		tblcNodeNo.setWidth(80);
		tblcNodeNo.setText("Node#");
		
		TableColumn tblcRecTime = new TableColumn(tableReportNodes, SWT.NONE);
		tblcRecTime.setWidth(100);
		tblcRecTime.setText("Rec. Time");
		
		TableColumn tblcWeight = new TableColumn(tableReportNodes, SWT.NONE);
		tblcWeight.setWidth(100);
		tblcWeight.setText("Weight");
		
		TableColumn tblcBalance = new TableColumn(tableReportNodes, SWT.NONE);
		tblcBalance.setWidth(100);
		tblcBalance.setText("Balance");
		
		Button btnNewButton = new Button(shlRewardSummary, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlRewardSummary.close();
				shlRewardSummary.dispose();
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		btnNewButton.setBounds(297, 505, 117, 53);
		btnNewButton.setText("Close(&C)");
		
		Button btnPayp = new Button(shlRewardSummary, SWT.NONE);
		btnPayp.setText("PAY (&P)");
		btnPayp.setFont(SWTResourceManager.getFont("Sans", 14, SWT.BOLD));
		btnPayp.setBounds(10, 505, 117, 53);

	}
}
