package caas;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;

import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Group;

public class SAConSim {

	protected Shell shlSelfadaptiveContractSimulator;
	
	public static String userHomeDir;
	
	public static SumoTraciConnection connSumo;
	private Table table;
	private Text txtLog;
	private Text txtDelay;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		userHomeDir = System.getProperty("user.home");		
				
		try {
			SAConSim window = new SAConSim();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlSelfadaptiveContractSimulator.open();
		shlSelfadaptiveContractSimulator.layout();
		while (!shlSelfadaptiveContractSimulator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void terminate() {
		
		if (connSumo != null && !connSumo.isClosed()) {
			
			System.out.println("Closing SUMO Server...");
			try {
				connSumo.close();
			} catch (InterruptedException IE) {
				System.out.println("SUMO Server Error: " + IE.getMessage());
			} catch (IOException IOE) {
				System.out.println("SUMO Server Error: " + IOE.getMessage());
			}
			
			System.out.println("Terminate all ethereum nodes...");
			
		}
		
		String [] cmd = {"killall", "geth"};				
		String [] removeCmd = {"rm", "-r", userHomeDir + "/ethereum"};
		
		try {
			Process script_exec = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
			
			String output;
			while ((output = reader.readLine()) != null) {
				System.out.println(output);
			}
			
			Runtime.getRuntime().exec(removeCmd);
			System.out.println("Successfully terminated!");
			
		} catch (IOException ioe) {
			System.out.println("Error: " + ioe.getMessage());
		}	
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		HashSet<String> vehicleList = new HashSet <String>();						// Current Sumo Vehicle List
		
		shlSelfadaptiveContractSimulator = new Shell();
		shlSelfadaptiveContractSimulator.setSize(1024, 768);
		shlSelfadaptiveContractSimulator.setText("Self-Adaptive Contract Simulator (test ver.)");
		shlSelfadaptiveContractSimulator.setLayout(null);
		
		
		Composite compMain = new Composite(shlSelfadaptiveContractSimulator, SWT.NONE);
		compMain.setBounds(3, 3, 800, 600);
		compMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		
		Button btnStartSim = new Button(compMain, SWT.NONE);
		btnStartSim.setBounds(670, 460, 120, 28);
		btnStartSim.setSelection(true);		
		btnStartSim.setFont(SWTResourceManager.getFont("Sans", 14, SWT.BOLD));
		btnStartSim.setText("Start");
		
		Button btnNextStep = new Button(compMain, SWT.NONE);
		btnNextStep.setBounds(670, 494, 120, 28);
		btnNextStep.setEnabled(false);		
		btnNextStep.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		btnNextStep.setText("Next Step");
		
		Button btnStopSim = new Button(compMain, SWT.NONE);
		btnStopSim.setBounds(670, 562, 120, 28);
		btnStopSim.setEnabled(false);
		btnStopSim.setFont(SWTResourceManager.getFont("Sans", 14, SWT.BOLD));
		btnStopSim.setText("Stop");
		
		Button btnAutoStep = new Button(compMain, SWT.NONE);
		
		btnAutoStep.setEnabled(false);
		btnAutoStep.setFont(SWTResourceManager.getFont("Sans", 14, SWT.NORMAL));
		btnAutoStep.setBounds(670, 528, 120, 28);
		btnAutoStep.setText("Auto Step");
		
		Group grpNodeProperties = new Group(shlSelfadaptiveContractSimulator, SWT.NONE);
		grpNodeProperties.setText("Node & Properties");
		grpNodeProperties.setBounds(809, 263, 201, 469);
		
		table = new Table(grpNodeProperties, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 202, 179, 241);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Property");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("Value");
		
		TreeViewer treeViewer = new TreeViewer(grpNodeProperties, SWT.BORDER);
		
		Tree NodeTree = treeViewer.getTree();
		NodeTree.setBounds(10, 10, 179, 186);
		NodeTree.setLinesVisible(true);
		
		TreeItem trtmNewTreeitem = new TreeItem(NodeTree, SWT.NONE);
		trtmNewTreeitem.setText("Ethereum");
		trtmNewTreeitem.setExpanded(true);
		
		btnStopSim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				terminate();
				
				txtLog.append("Closing SUMO Server...\n");
				txtLog.append("Terminate all ethereum nodes...\n");
				
				btnNextStep.setEnabled(false);									// Set Next Step button disable
				btnAutoStep.setEnabled(false); 									// Set Auto Step button disable
				btnStartSim.setEnabled(true);									// Set Start button enable
				btnStopSim.setEnabled(false);									// Set Stop button disable
				
				trtmNewTreeitem.removeAll(); 									// Remove all items of ethereum root tree item
			}
		});
		
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				Collection<Vehicle> vehicles;				
				if (connSumo != null) {
					
					try {
					
						int time = connSumo.getCurrentSimTime() / 1000;
						vehicles = connSumo.getVehicleRepository().getAll().values();
						
						for (Vehicle vehicle: vehicles) {
							
							if (!vehicleList.contains(vehicle.getID())) {
							
								txtLog.append("Vehicle ID " + vehicle.getID() + " is newly entered in the map.\n");								
								vehicleList.add(vehicle.getID());
								
								TreeItem nodeItem = new TreeItem(trtmNewTreeitem, SWT.NONE);
								nodeItem.setText("Node#" + vehicle.getID());
								NodeTree.setTopItem(nodeItem);
							} 
							
							txtLog.append("At time step " + time + ", Vehicle ID " + vehicle.getID() + 
									"'s location is (" + vehicle.getPosition().getX() + "," + vehicle.getPosition().getY() + ")\n");							
						}
						
						txtLog.getParent().layout();
						connSumo.nextSimStep();
						
					} catch (IOException ioe) {
						txtLog.append("Sumo Server error: " + ioe.getMessage() + "\n");
					}
				}
				
			}
		});
		
		btnStartSim.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				txtLog.append("Running SUMO Server...\n");
				connSumo = new SumoTraciConnection(userHomeDir + "/caasproject/sumo/map/grid/grid.sumocfg", 12345);
				
				try {
					connSumo.runServer();
					connSumo.nextSimStep();												// First step is empty
					
					txtLog.append("Successfully loading sumo map.\n");
					txtLog.append("Map bounds are: " + connSumo.queryBounds() + "\n");
					
				} catch (Exception ex) {
					txtLog.append("Sumo Server error: " + ex.getMessage() + "\n");
				}				
				
				txtLog.append("Running Master's node..." + "\n");
				String [] cmd = {userHomeDir + "/caasproject/ethereum/runMasterNode.sh", "ethereum"};
				
				try {
					Process script_exec = Runtime.getRuntime().exec(cmd);
					BufferedReader reader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
					
					String output;
					while ((output = reader.readLine()) != null) {
						txtLog.append(output + "\n");
					}
					
					txtLog.append("Successfully running Master's node!" + "\n");
					
				} catch (IOException ioe) {
					txtLog.append("Error: " + ioe.getMessage() + "\n");
				}
				
				btnNextStep.setEnabled(true);									// Set Next Step button enable
				btnAutoStep.setEnabled(true); 									// Set Auto Step button enable
				btnStartSim.setEnabled(false);									// Set Start button disable
				btnStopSim.setEnabled(true);									// Set Stop button enable
				
				TreeItem masterNodeItem = new TreeItem(trtmNewTreeitem, SWT.NONE);
				masterNodeItem.setText("Master Node");
			}
		});
		
		btnAutoStep.addMouseListener(new MouseAdapter() {
			
			public void mouseDown(MouseEvent e) {
				
				
			}
		});
		
		txtLog = new Text(shlSelfadaptiveContractSimulator, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		txtLog.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		txtLog.setBounds(3, 612, 800, 120);
		
		Group grpSimulationSetting = new Group(shlSelfadaptiveContractSimulator, SWT.NONE);
		grpSimulationSetting.setText("Simulation Settings");
		grpSimulationSetting.setBounds(809, 3, 201, 254);
		
		Label lblNewLabel = new Label(grpSimulationSetting, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 80, 15);
		lblNewLabel.setText("Delay(ms):");
		
		txtDelay = new Text(grpSimulationSetting, SWT.BORDER);
		txtDelay.setText("300");
		txtDelay.setBounds(96, 6, 93, 23);	
		
		
		shlSelfadaptiveContractSimulator.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {				
				terminate();			
			}
		});

	}
}
