package caas;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.wb.swt.SWTResourceManager;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import it.polito.appeal.traci.*;

import caas.util.SASimUtil;
import caas.draw.*;

public class SAConSim {
	
	public SASimUtil saSimUtil = new SASimUtil();
	public double xPosWeight = 0;
	public double yPosWeight = 0;
	
	public final static String HOME_DIR = System.getProperty("user.home");
	
	protected Shell shlSelfadaptiveContractSimulator;
	public Composite compMain;
	
	public TreeItem trtmNewTreeitem;
	public Tree NodeTree;
	
	public static boolean autoStepRunning = false;
	public static HashSet<String> vehicleList = new HashSet <String>();						// Current Sumo Vehicle List
		
	public SumoTraciConnection connSumo;
	private Table table;
	private Text txtLog;
	private Text txtEthDir;
	private Text txtRootDir;
	private Text txtDelay;

	public static void main(String[] args) {	
				
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
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shlSelfadaptiveContractSimulator = new Shell();
		shlSelfadaptiveContractSimulator.setSize(1024, 768);
		shlSelfadaptiveContractSimulator.setText("Self-Adaptive Contract Simulator (test ver.)");
		shlSelfadaptiveContractSimulator.setLayout(null);
		
		compMain = new Composite(shlSelfadaptiveContractSimulator, SWT.NONE);
		
		compMain.setBounds(3, 3, 800, 600);
		compMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		//compMain.addPaintListener(new DrawVehicleListener());
		
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
		
		NodeTree = treeViewer.getTree();
		NodeTree.setBounds(10, 10, 179, 186);
		NodeTree.setLinesVisible(true);
		
		trtmNewTreeitem = new TreeItem(NodeTree, SWT.NONE);
		trtmNewTreeitem.setText("Ethereum");
		trtmNewTreeitem.setExpanded(true);
		
		txtLog = new Text(shlSelfadaptiveContractSimulator, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);
		txtLog.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		txtLog.setBounds(3, 612, 800, 120);
		
		Group grpSimulationSetting = new Group(shlSelfadaptiveContractSimulator, SWT.NONE);
		grpSimulationSetting.setText("Simulation Settings");
		grpSimulationSetting.setBounds(809, 3, 201, 254);
		
		Label lblEthereumDir = new Label(grpSimulationSetting, SWT.NONE);
		lblEthereumDir.setBounds(10, 40, 80, 15);
		lblEthereumDir.setText("Ethereum Dir:");
		
		txtEthDir = new Text(grpSimulationSetting, SWT.BORDER);
		txtEthDir.setText("ethereum");
		txtEthDir.setBounds(96, 36, 93, 23);	
		
		Label lblRootDir = new Label(grpSimulationSetting, SWT.NONE);
		lblRootDir.setText("Root Dir:");
		lblRootDir.setBounds(10, 10, 80, 15);
		
		txtRootDir = new Text(grpSimulationSetting, SWT.BORDER);
		txtRootDir.setText("caas");
		txtRootDir.setBounds(96, 6, 93, 23);
		
		Label label = new Label(grpSimulationSetting, SWT.NONE);
		label.setText("Delay(ms):");
		label.setBounds(10, 69, 80, 15);
		
		txtDelay = new Text(grpSimulationSetting, SWT.BORDER);
		txtDelay.setText("1000");
		txtDelay.setBounds(96, 65, 93, 23);
		
		btnStartSim.addMouseListener(new MouseAdapter() {
			
			public void mouseDown(MouseEvent e) {
				
				int randomNum = ThreadLocalRandom.current().nextInt(10000, 20000);
				
				txtLog.append("Running SUMO Server...\n");
				connSumo = new SumoTraciConnection(HOME_DIR + '/' + txtRootDir.getText() + "/sumo/map/grid/grid.sumocfg", randomNum);
				
				try {
					connSumo.runServer();
					connSumo.nextSimStep();												// First step is empty
					
					txtLog.append("Successfully loading sumo map.\n");
					txtLog.append("Map bounds are: " + connSumo.queryBounds() + "\n");
					
					xPosWeight = compMain.getBounds().width / connSumo.queryBounds().getWidth();
					yPosWeight = compMain.getBounds().height / connSumo.queryBounds().getHeight();
					
				} catch (Exception ex) {
					txtLog.append("Sumo Server error: " + ex.getMessage() + "\n");
				}				
				
				txtLog.append("Running Master's node..." + "\n");
				String [] cmd = {HOME_DIR + '/' + txtRootDir.getText() + "/ethereum/runMasterNode.sh", txtEthDir.getText()};
				
				try {
					Process script_exec = Runtime.getRuntime().exec(cmd);
					BufferedReader reader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
					
					String output;
					while ((output = reader.readLine()) != null) {
						txtLog.append(output + "\n");
					}
					
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
		
		btnNextStep.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				
				btnAutoStep.setEnabled(false); 									// Set Auto step to disable
				
				if (connSumo != null) {
					try {
						nextStep();						
						connSumo.nextSimStep();			
					} catch (IOException ioe) {
						txtLog.append("Sumo Server error: " + ioe.getMessage() + "\n");
					}
				}
				
			}
		});
		
		
		btnAutoStep.addMouseListener(new MouseAdapter() {
			
			public void mouseDown(MouseEvent e) {
				
				autoStepRunning = true;
				
				int stepTime = (int) saSimUtil.getLastDepartTime(HOME_DIR, '/' + txtRootDir.getText() + "/sumo/map/grid/grid.rou.xml");
				
				btnNextStep.setEnabled(false); 									// Set Next step button to disable
				
				for (int i = 0; i < stepTime; i++) {
					
					Display.getDefault().asyncExec(new Runnable() {
						
						public void run() {
							
							try {
								if (autoStepRunning) {
									nextStep();
									Thread.sleep(Integer.parseInt(txtDelay.getText()));		
									
									connSumo.nextSimStep();
								}
							} catch (IOException | InterruptedException e) {
								try {
									connSumo.close();
								} catch (IOException | InterruptedException e1) {}
								
								e.printStackTrace();
							}
						}
						
					});
				}
			}
		});
		
		btnStopSim.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				
				autoStepRunning = false;				
					
				terminate();
								
				btnNextStep.setEnabled(false);									// Set Next Step button disable
				btnAutoStep.setEnabled(false); 									// Set Auto Step button disable
				btnStartSim.setEnabled(true);									// Set Start button enable
				btnStopSim.setEnabled(false);									// Set Stop button disable
				
				trtmNewTreeitem.removeAll(); 									// Remove all items of ethereum root tree item
			}
		});
		
		shlSelfadaptiveContractSimulator.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent arg0) {				
				terminate();			
			}
		});
	}
	
	public void terminate() {
		
		if (connSumo != null) {
			
			autoStepRunning = false;
			
			System.out.println("Closing SUMO Server...");
			try {
				connSumo.close();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();;
			} 
			
			System.out.println("Terminate all ethereum nodes...");
		}
		
		String [] cmd = {"killall", "geth"};				
		String [] removeCmd = {"rm", "-r", HOME_DIR + "/" + txtEthDir.getText()};
		
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
	
	
	
	public void nextStep() {

		int time = connSumo.getCurrentSimTime() / 1000;
		
		try {
			Collection<Vehicle> vehicles = connSumo.getVehicleRepository().getAll().values();
			
			for (Vehicle vehicle: vehicles) {
				
				compMain.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						try {
							e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE)); 
							e.gc.fillOval((int)(vehicle.getPosition().getX() * xPosWeight)-10, (int)(vehicle.getPosition().getY() * yPosWeight)-10, 20, 20);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				
				compMain.redraw();
				
				if (!vehicleList.contains(vehicle.getID())) {
					
					txtLog.append("Vehicle ID " + vehicle.getID() + " is newly entered in the map.\n");				
					txtLog.append("At time step " + time + ", Vehicle ID " + vehicle.getID() + 
						"'s location is (" + vehicle.getPosition().getX() + "," + vehicle.getPosition().getY() + ")\n");	
					vehicleList.add(vehicle.getID());
					
					String [] cmd = {HOME_DIR + '/' + txtRootDir.getText() + "/ethereum/runEthNode.sh", vehicle.getID()};
					
					try {
						Process script_exec = Runtime.getRuntime().exec(cmd);
						BufferedReader reader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
						
						String output;
						while ((output = reader.readLine()) != null) {
							txtLog.append(output + "\n");
						}
						
					} catch (IOException ioe) {
						txtLog.append("Error: " + ioe.getMessage() + "\n");
					}
									
					TreeItem nodeItem = new TreeItem(trtmNewTreeitem, SWT.NONE);
					nodeItem.setText("Node#" + vehicle.getID());
				}																
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
