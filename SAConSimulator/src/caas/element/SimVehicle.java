package caas.element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import caas.util.SASimUtil;

public class SimVehicle {

	private Random random = new Random();
	
	// Vehicle variables
	private String id;
	private double xpos;
	private double ypos;
	private String setDistance;
	private String ethCmdPath;
	private String ethDir;
	
	// Node variables
	private double nodeDistance;
	private List <String> connectedPeerNode = new ArrayList<String> ();
	
	// Monitoring device variables
	
	private enum DeviceNameEnum {CCTV, Blackbox, Smartphone};			// to get element randomly, use SASimUtil.randomEnum(DeviceName.class)
	private String deviceName;
	private enum DeviceTypeEnum {Photo, Video};
	private String deviceType;
	private enum MonitoringResolutionEnum {UHD, QHD, FHD, SD};
	private String monitoringResolution;
	private int recordingTime;										// second
	private int targetDistance;									// meter 
	private String sourceAddress;									// Recording source address
	
	public SimVehicle(String id, double xpos, double ypos, String setDistance, String ethCmdPath, String ethDir) {
		this.id = id;
		this.xpos = xpos;
		this.ypos = ypos;
		this.setDistance = setDistance;
		this.ethCmdPath = ethCmdPath;
		this.ethDir = ethDir;
		this.deviceName = SASimUtil.randomEnum(DeviceNameEnum.class).toString();
		this.deviceType = SASimUtil.randomEnum(DeviceTypeEnum.class).toString();
		this.monitoringResolution = SASimUtil.randomEnum(MonitoringResolutionEnum.class).toString();
		this.recordingTime = random.nextInt(3600);
		this.targetDistance = random.nextInt(100);
		this.sourceAddress = getRandomAddress(this.deviceName, this.deviceType, this.monitoringResolution);
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
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	
	public String getResolution() {
		return monitoringResolution;
	}
	
	public int getRecordingTime() {
		return recordingTime;
	}
	
	public int getTargetDistance() {
		return targetDistance;
	}
	
	public String getSourceAddress() {
		return sourceAddress;
	}
	
	public List<String> getConnectedNodes() {
		return connectedPeerNode;
	}
	
	public String getRandomAddress(String name, String type, String resolution) {
		if(type == "Photo") {
			
			String blackboxSamples [] = {
				"http://newsroom.etomato.com/userfiles/%EA%B3%BC%EC%86%8D%EA%B2%BD%EB%B3%B4%ED%91%9C%EC%A7%803.jpg",	// UHD
				"http://cfile21.uf.tistory.com/image/246DB03B54F3BA53212A0C",			// QHD
				"http://cfile1.uf.tistory.com/image/17410B404E6361543BA3E8",			// FHD
				"https://i.ytimg.com/vi/YM4vyp75F6s/maxresdefault.jpg"					// SD
					
			};
			
			String cctvSamples [] = {
				"http://www.hellot.net/_UPLOAD_FILES/magazine/image/thumbnail_1403858419.jpg",	// UHD
				"http://image.ytn.co.kr/general/jpg/2017/0129/201701291201573932_t.jpg", // QHD
				"http://cfile21.uf.tistory.com/image/2618AE40526F67131F3780",			// FHD
				"https://i.ytimg.com/vi/pXDBPzX4o_0/maxresdefault.jpg"					// SD
			};
			
			String phoneSamples [] = {
				"http://sojoong.joins.com/wp-content/uploads/sites/4/2017/04/20170326_33043977.jpg",	// UHD
				"http://www.ggitv.co.kr/news/photo/201611/1574_1329_1950.jpg",		// QHD
				"http://mblogthumb1.phinf.naver.net/20160526_132/tnmff40_1464238941391WBjJv_JPEG/%B9%E6%C8%B2%C7%CF%B4%C2_%C4%AE%B3%AFBroken.2014.720p.HDRip.H264-CJCONTENTS.mp4_004832832.jpg?type=w2",	// FHD
				"http://cfile29.uf.tistory.com/image/0353123451333DA40D6E37"		// SD
			};
			
			int sampleIdx = 0;
			if (resolution == "UHD") 		sampleIdx = 0;
			else if (resolution == "QHD")	sampleIdx = 1;
			else if (resolution == "FHD")	sampleIdx = 2;
			else							sampleIdx = 3;
						
			if (name == "CCTV")				return cctvSamples[sampleIdx];
			else if (name == "Blackbox")	return blackboxSamples[sampleIdx];
			else							return phoneSamples[sampleIdx];
			
		} else {
			int channel = random.nextInt(154);
			
			return "http://210.104.234.205/api/nPlayerLive.swf?ip=210.104.234.205&ch=ch" + channel + ".stream&appname=sd1";
		}
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
					Process script_exec = Runtime.getRuntime().exec(cmd);
					BufferedReader reader = new BufferedReader(new InputStreamReader(script_exec.getInputStream()));
					
					String output;
					while ((output = reader.readLine()) != null)
						System.out.println(output);
					
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
