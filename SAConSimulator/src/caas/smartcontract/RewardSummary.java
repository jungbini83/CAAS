package caas.smartcontract;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import caas.element.SimVehicle;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class RewardSummary extends Dialog {

	protected Object result;
	protected Shell shlRewardSummary;
	private Text text;
	private Table tableReportNodes;
	
	private ArrayList<SimVehicle> vehicles;
	
	public final String HOME_DIR = System.getProperty("user.home");
	private String rootDirName;
	private String ethDirName;
	private int totalReward;
	
	private enum DeviceTypeEnum {CCTV, Blackbox, Smartphone};
	private enum MonitoringTypeEnum {Photo, Video};
	private enum MonitoringResolutionEnum {SD, FHD, QHD, UHD};

	public RewardSummary(Shell parent, int style, String rootDir, String ethDir, String totalReward, ArrayList<SimVehicle> vehicle) {
		super(parent, style);
		setText("SWT Dialog");
		this.rootDirName = rootDir;
		this.ethDirName = ethDir;
		this.totalReward = Integer.parseInt(totalReward);
		this.vehicles = vehicle;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		setVehicleTreeItems();
		
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

	private void setVehicleTreeItems() {
		
		// 1. Check balance of coin of reporters
		checkCurrentBalance(vehicles);
				
		// 2. Add contribution attributes and calculating final weight from smart contract		
		Process script_exec;
		BufferedReader normalReader;		
		String output;
		for(SimVehicle vehicle: vehicles) {
					
			/* Save reporter's information using smart contract */
			String [] cmdDetectCriminal = {HOME_DIR + '/' + rootDirName + "/ethereum/SmartContract/WeightingContribution.sh", ethDirName, vehicle.getID(), 
					String.valueOf(DeviceTypeEnum.valueOf(vehicle.getDeviceName()).ordinal() + 1),
					String.valueOf(MonitoringTypeEnum.valueOf(vehicle.getDeviceType()).ordinal() + 1),
					String.valueOf(MonitoringResolutionEnum.valueOf(vehicle.getResolution()).ordinal() + 1),
					String.valueOf(new Double(vehicle.getRecordingTime()).intValue()), 
					String.valueOf(new Double(vehicle.getMonitoringDistance()).intValue()),
					vehicle.getSourceAddress()};			
			
			try {
				script_exec = Runtime.getRuntime().exec(cmdDetectCriminal);
				normalReader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
				
				while ((output = normalReader.readLine()) != null) {
					if (!output.contains("undefined"))
						System.out.println("log: " + output);					
				}
				
			} catch (IOException ioe) {
				System.out.println("Error: " + ioe.getMessage());
			}
			
		}
		
		if (vehicles.size() > 0) {
			
			for (int i = 0 ; i < 3 ; i++) {
			
				System.out.println("Waiting for deploying smart contract... (" + (3-i) + ")");
				
				try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			}
			
		}
		
		/* 4. Weighting contribution using smart contract */		
		int treeIdx = 0;
		int sumOfWeight = 0;
		for(SimVehicle vehicle: vehicles) {
			
			int nodeIndex = vehicles.size() - treeIdx;			
			String [] cmdWeightingContribution = {HOME_DIR + '/' + rootDirName + "/ethereum/SmartContract/CalcWeight.sh", ethDirName, 
												vehicle.getID(), String.valueOf(nodeIndex)};
			
			try {
				
				try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
				
				script_exec = Runtime.getRuntime().exec(cmdWeightingContribution);
				normalReader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
										
				while ((output = normalReader.readLine()) != null) {
					
					if (!output.contains("undefined")) {
						
						sumOfWeight += Integer.parseInt(output);
						
						TableItem nodeItem = new TableItem(tableReportNodes, SWT.NONE); 
						nodeItem.setText(new String[]{vehicle.getID(), String.valueOf(vehicle.getRecordingTime()), output, "0"});						
					}
				}						
				
				treeIdx++;
				
			} catch (IOException ie) {
				System.out.println("Error: " + ie.getMessage());
			}
		}
		
		/* 5. Calculate reward  */
		for (int i = 0 ; i < tableReportNodes.getItemCount() ; i++ ) {
			TableItem nodeItem = tableReportNodes.getItem(i);
			
			float adjustedReward = (Float.parseFloat(nodeItem.getText(2)) / sumOfWeight) * totalReward;
						
			nodeItem.setText(new String[] {nodeItem.getText(0), nodeItem.getText(1), nodeItem.getText(2), String.valueOf(Math.round(adjustedReward))});
			tableReportNodes.update();
		}
	}
	
	private void checkCurrentBalance(ArrayList<SimVehicle> vehicles) {
		
		Process script_exec;
		BufferedReader normalReader;
		String output;		
		for (SimVehicle vehicle: vehicles) {
			
			String [] cmd = {HOME_DIR + '/' + rootDirName + "/ethereum/SmartContract/CheckBalance.sh", ethDirName, vehicle.getID()};
			
			try {					
				
				script_exec = Runtime.getRuntime().exec(cmd);
				normalReader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
				
				while ((output = normalReader.readLine()) != null) {				
					vehicle.setCoin(Integer.parseInt(output));
				}
				
			} catch (IOException ioe) {
				System.out.println("Error: " + ioe.getMessage());
			}
		}		
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
		text.setText(String.valueOf(totalReward));
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
		tblcBalance.setText("Reward");
		
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
		btnPayp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Process script_exec;
				BufferedReader normalReader;								
				String output;
				for (int i = 0 ; i < tableReportNodes.getItemCount() ; i++ ) {
					
					try { Thread.sleep(500); } catch (InterruptedException e1) { e1.printStackTrace(); }
					
					TableItem nodeItem = tableReportNodes.getItem(i);
					
					String [] cmd = {HOME_DIR + '/' + rootDirName + "/ethereum/SmartContract/PayCoin.sh", ethDirName, nodeItem.getText(0), nodeItem.getText(3)};
					
					try {
						script_exec = Runtime.getRuntime().exec(cmd);
						normalReader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));						
						
						while ((output = normalReader.readLine()) != null)
							System.out.println("Transaction result address: " + output);
						
												
					} catch (IOException ioe) {
						System.out.println("Error: " + ioe.getMessage());
					}
				}	
				
			}
		});
		btnPayp.setText("PAY (&P)");
		btnPayp.setFont(SWTResourceManager.getFont("Sans", 14, SWT.BOLD));
		btnPayp.setBounds(10, 505, 117, 53);

	}
}
