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

public class SelectMapDialog extends Dialog {

	protected String selType;
	protected Shell shlSelectingSumoMap;
	
	public Canvas canvas;
	public Image gridImg, spiderImg, randomImg;

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
	public String open() {
		createContents();
		shlSelectingSumoMap.open();
		shlSelectingSumoMap.layout();
		Display display = getParent().getDisplay();
		
		gridImg = new Image(display, "resources/image/Grid.jpg");
		spiderImg = new Image(display, "resources/image/Spider.jpg");
		randomImg = new Image(display, "resources/image/Random.jpg");
		
		selType = "Grid";			// Default map
		
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
		shlSelectingSumoMap = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlSelectingSumoMap.setSize(450, 615);
		shlSelectingSumoMap.setText("SACM Simulator");
		
		Group grpSelectMap = new Group(shlSelectingSumoMap, SWT.NONE);
		grpSelectMap.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		grpSelectMap.setText("SUMO Map type");
		grpSelectMap.setBounds(10, 10, 424, 63);
		
		Button rBtnGrid = new Button(grpSelectMap, SWT.RADIO);
		rBtnGrid.addSelectionListener(new SelectionAdapter() {
						@Override
			public void widgetSelected(SelectionEvent e) {
			
				selType = rBtnGrid.getText();			
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
		rBtnGrid.setBounds(10, 10, 71, 19);
		rBtnGrid.setText("Grid");
		
		Button rBtnSpider = new Button(grpSelectMap, SWT.RADIO);
		rBtnSpider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				selType = rBtnSpider.getText();
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
		rBtnSpider.setBounds(168, 10, 88, 19);
		
		Button rBtnRandom = new Button(grpSelectMap, SWT.RADIO);
		rBtnRandom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				selType = rBtnRandom.getText();
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
		rBtnRandom.setBounds(324, 10, 88, 19);
		
		Group grpPreview = new Group(shlSelectingSumoMap, SWT.NONE);
		grpPreview.setFont(SWTResourceManager.getFont("Sans", 12, SWT.NORMAL));
		grpPreview.setText("Preview");
		grpPreview.setBounds(10, 86, 424, 436);
		
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
				shlSelectingSumoMap.dispose();
			}
		});
		btnSelect.setFont(SWTResourceManager.getFont("Sans", 12, SWT.BOLD));
		btnSelect.setBounds(321, 536, 113, 37);
		btnSelect.setText("Select");

	}
}
