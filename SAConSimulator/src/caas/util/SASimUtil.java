package caas.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class SASimUtil {

	public static double getLastDepartTime(String filePath) {
		
		String lastTime = "";
		
		try {
			
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			String s;
			String lastVehicle = "";
			while((s = br.readLine()) != null) {
				if (s.indexOf("<vehicle id=")>=0)
					lastVehicle = s;
			}
			
			lastTime = lastVehicle.substring(lastVehicle.indexOf("depart=")+8, lastVehicle.indexOf("\"", lastVehicle.indexOf("depart=")+8));
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Math.ceil(Float.parseFloat(lastTime));
		
	}
	
	public static int getNumOfVehicles(String filePath) {
		
		int numOfVehicle = 0;
		String vehID;
		
		try {
			
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			String s;
			String lastVehicle = "";
			while((s = br.readLine()) != null) {
				if (s.indexOf("<vehicle id=")>=0)
					lastVehicle = s;
			}
			
			vehID = lastVehicle.substring(lastVehicle.indexOf("<vehicle id=")+13, lastVehicle.indexOf(" ", lastVehicle.indexOf("<vehicle id=")+13)-1);			
			vehID = vehID.replace("veh", "");				
			
			numOfVehicle = Integer.parseInt(vehID);
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return numOfVehicle;
		
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		
		int x = new Random().nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
		
	}
	
}
