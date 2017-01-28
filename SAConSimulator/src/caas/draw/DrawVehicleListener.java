package caas.draw;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

public class DrawVehicleListener implements PaintListener {

	public void paintControl(PaintEvent e) {
		
		GC gc =  e.gc;
		Composite comp = (Composite)e.getSource();
		
		int width = comp.getBounds().width;
		int height = comp.getBounds().height;
		
		System.out.println("Width=" + width + ", Height=" + height);
	}
	
}
