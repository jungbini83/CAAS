package caas.element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimVehicle {
	
	private String id;
	private double xpos;
	private double ypos;
	private String setDistance;
	private String ethCmdPath;
	private String ethDir;
	
	private double nodeDistance;
	private List <String> connectedPeerNode = new ArrayList<String> ();
	
	public SimVehicle(String id, double xpos, double ypos, String setDistance, String ethCmdPath, String ethDir) {
		this.id = id;
		this.xpos = xpos;
		this.ypos = ypos;
		this.setDistance = setDistance;
		this.ethCmdPath = ethCmdPath;
		this.ethDir = ethDir;
	}
	
	public void setXpos(double value) {
		xpos = value;
	}
	
	public void setYpos(double value) {
		ypos = value;
	}
	
	public double getXpos() throws IOException {
		return xpos;
	}
	
	public double getYpos() throws IOException  {
		return ypos;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
	public List<String> getConnectedNodes() {
		return connectedPeerNode;
	}
	
	public void findNearNodes(ArrayList<SimVehicle> simVehicles) throws IOException {
		for(SimVehicle vehicle: simVehicles) {
			if (vehicle.getID() != this.getID())
				addPeerNode(vehicle.getID(), vehicle.getXpos(), vehicle.getYpos(), setDistance);
		}
	}
	
	public void addPeerNode(String peerId, double xPeerPos, double yPeerPos, String distance) {

		double peerDistance = Double.parseDouble(distance);
		
		double xLength = Math.abs(xpos - xPeerPos);
		double yLength = Math.abs(ypos - yPeerPos);
		
		nodeDistance = Math.sqrt(Math.pow(xLength,2) + Math.pow(yLength,2));
		
		if (peerDistance > nodeDistance) {
			if (!connectedPeerNode.contains(peerId)) {
				
				connectedPeerNode.add(peerId);
				
				// Add this node to target node
				System.out.println("Add Node #" + peerId + " to Node #" + this.id);
				String [] cmd = {ethCmdPath + "/ethereum/manageScript/addPeer.sh", ethDir, peerId, this.id};
				
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
				
		} else {
			if (connectedPeerNode.contains(peerId)) {
				
				connectedPeerNode.remove(peerId);
				
				// Remove this node to target node
				System.out.println("Remove Node #" + peerId + " from Node #" + this.id);
				String [] cmd = {ethCmdPath + "/ethereum/manageScript/removePeer.sh", ethDir, peerId, this.id};
				
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
				
		}
		
	}
}
