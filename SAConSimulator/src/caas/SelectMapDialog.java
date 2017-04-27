package caas;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class SelectMapDialog extends Dialog {

	protected String selMapType = null;
	protected String [] selType = new String[3];
	protected Shell shlSelectingSumoMap;
	
	public Canvas canvas;
	public Combo cmbRealMap;
	public Image gridImg, spiderImg, randomImg, koreaUnivMapImg;
	private Text txtRouteOption;
	private Text txtMapOption;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public SelectMapDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String [] open() {
		createContents();
		shlSelectingSumoMap.open();
		shlSelectingSumoMap.layout();
		Display display = getParent().getDisplay();
		
		gridImg = new Image(display, "resources/image/Grid.jpg");
		spiderImg = new Image(display, "resources/image/Spider.jpg");
		randomImg = new Image(display, "resources/image/Random.jpg");
		koreaUnivMapImg = new Image(display, "resources/image/KoreaUnivMap.jpg");
		
		selMapType = "Grid";			// Default map
		
		while (!shlSelectingSumoMap.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return selType;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlSelectingSumoMap = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);		
		shlSelectingSumoMap.setSize(455, 690);
		shlSelectingSumoMap.setText("SACM Simulator");
		
		Group grpSelectMap = new Group(shlSelectingSumoMap, SWT.NONE);
		grpSelectMap.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		grpSelectMap.setText("SUMO Map type");
		grpSelectMap.setBounds(10, 10, 424, 58);
		
		Button rBtnGrid = new Button(grpSelectMap, SWT.RADIO);
		rBtnGrid.addSelectionListener(new SelectionAdapter() {
						@Override
			public void widgetSelected(SelectionEvent e) {
				
				selMapType = rBtnGrid.getText();
				
				cmbRealMap.setEnabled(false);
				txtMapOption.setEnabled(true);
				txtRouteOption.setEnabled(true);
				
				txtMapOption.setText("--grid.number=5 --grid.length=100");
				
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(gridImg, 0, 0, gridImg.getBounds().width, gridImg.getBounds().height, 0, 0, 400, 400);						
					}
				});
				
				canvas.redraw();				
			}
		});
		rBtnGrid.setSelection(true);
		rBtnGrid.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		rBtnGrid.setBounds(8, 10, 71, 19);
		rBtnGrid.setText("Grid");
		
		Button rBtnSpider = new Button(grpSelectMap, SWT.RADIO);
		rBtnSpider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				selMapType = rBtnSpider.getText();
				
				cmbRealMap.setEnabled(false);				
				txtMapOption.setEnabled(true);
				txtRouteOption.setEnabled(true);				
				
				txtMapOption.setText("--arms=5 --circles=3 --radius=100");
				
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(spiderImg, 0, 0, spiderImg.getBounds().width, spiderImg.getBounds().height, 0, 0, 400, 400);
					}
				});
				
				canvas.redraw();
			}
		});
		rBtnSpider.setText("Spider");
		rBtnSpider.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		rBtnSpider.setBounds(73, 10, 88, 19);
		
		Button rBtnRandom = new Button(grpSelectMap, SWT.RADIO);
		rBtnRandom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				selMapType = rBtnRandom.getText();
				
				cmbRealMap.setEnabled(false);
				txtMapOption.setEnabled(true);
				txtRouteOption.setEnabled(true);				
				
				txtMapOption.setText("true --rand.iterations=30");
				
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(randomImg, 0, 0, randomImg.getBounds().width, randomImg.getBounds().height, 0, 0, 400, 400);
					}
				});
				
				canvas.redraw();
			}
		});
		rBtnRandom.setText("Random");
		rBtnRandom.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		rBtnRandom.setBounds(153, 10, 88, 19);
		
		Button rBtnReal = new Button(grpSelectMap, SWT.RADIO);
		rBtnReal.addSelectionListener(new SelectionAdapter() {					
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				cmbRealMap.setEnabled(true);
				txtMapOption.setEnabled(false);
				txtRouteOption.setEnabled(false);
				
				if (cmbRealMap.getText() == "KoreaUniv") {										
					canvas.addPaintListener(new PaintListener() {
						public void paintControl(PaintEvent e) {
							e.gc.drawImage(koreaUnivMapImg, 0, 0, gridImg.getBounds().width, gridImg.getBounds().height, 0, 0, 400, 400);						
						}
					});
				} else if (cmbRealMap.getText() == "NRFSeoul") {										
//					canvas.addPaintListener(new PaintListener() {
//						public void paintControl(PaintEvent e) {
//							e.gc.drawImage(koreaUnivMapImg, 0, 0, gridImg.getBounds().width, gridImg.getBounds().height, 0, 0, 400, 400);						
//						}
//					});
				} else if (cmbRealMap.getText() == "CheonanIC") {
//					canvas.addPaintListener(new PaintListener() {
//						public void paintControl(PaintEvent e) {
//							e.gc.drawImage(koreaUnivMapImg, 0, 0, gridImg.getBounds().width, gridImg.getBounds().height, 0, 0, 400, 400);						
//						}
//					});
				}
			}
		});
		rBtnReal.setText("Real");
		rBtnReal.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		rBtnReal.setBounds(248, 10, 71, 19);
		
		cmbRealMap = new Combo(grpSelectMap, SWT.READ_ONLY);
		cmbRealMap.setEnabled(false);
		cmbRealMap.setItems(new String[] {"KoreaUniv", "NRFSeoul", "CheonanIC"});
		cmbRealMap.setBounds(308, 6, 106, 23);
		cmbRealMap.select(0);
		
		Group grpPreview = new Group(shlSelectingSumoMap, SWT.NONE);
		grpPreview.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		grpPreview.setText("Preview");
		grpPreview.setBounds(10, 172, 424, 436);
		
		canvas = new Canvas(grpPreview, SWT.BORDER);
		canvas.setBounds(10, 6, 400, 400);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(gridImg, 0, 0, gridImg.getBounds().width, gridImg.getBounds().height, 0, 0, 400, 400);
			}
		});
		
		Button btnSelect = new Button(shlSelectingSumoMap, SWT.DEFAULT);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (cmbRealMap.getEnabled())
					selType[0] = cmbRealMap.getText();
				else
					selType[0] = selMapType;
				
				selType[1] = txtMapOption.getText();
				selType[2] = txtRouteOption.getText();
								
				shlSelectingSumoMap.dispose();
			}
		});
		btnSelect.setFont(SWTResourceManager.getFont("Sans", 12, SWT.BOLD));
		btnSelect.setBounds(281, 614, 153, 37);
		btnSelect.setText("Load this map");
		
		Group grpMakeOption = new Group(shlSelectingSumoMap, SWT.NONE);
		grpMakeOption.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		grpMakeOption.setText("Make option");
		grpMakeOption.setBounds(10, 74, 424, 92);
		
		txtRouteOption = new Text(grpMakeOption, SWT.BORDER);
		txtRouteOption.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				selType[2] = txtRouteOption.getText();
			}
		});
		txtRouteOption.setFont(SWTResourceManager.getFont("Sans", 10, SWT.NORMAL));
		txtRouteOption.setText("-e 180 --intermediate 10 -p 7 -b 0.1");
		txtRouteOption.setBounds(133, 36, 279, 23);
		
		Label lblNewLabel = new Label(grpMakeOption, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Sans", 11, SWT.BOLD));
		lblNewLabel.setBounds(10, 38, 132, 23);
		lblNewLabel.setText("Route Option");
		
		Label lblMapOption = new Label(grpMakeOption, SWT.NONE);
		lblMapOption.setText("Map Option");
		lblMapOption.setFont(SWTResourceManager.getFont("Sans", 11, SWT.BOLD));
		lblMapOption.setBounds(10, 8, 132, 23);
		
		txtMapOption = new Text(grpMakeOption, SWT.BORDER);
		txtMapOption.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				selType[1] = txtMapOption.getText();
			}
		});
		txtMapOption.setText("--grid.number=5 --grid.length=100");
		txtMapOption.setFont(SWTResourceManager.getFont("Sans", 10, SWT.NORMAL));
		txtMapOption.setBounds(133, 7, 279, 23);

	}
}
