package caas.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SASimUtil {

public double getLastDepartTime(String HOME_DIR, String filePath) {
		
		String lastTime = "";
		
		try {
			
			FileReader fr = new FileReader(HOME_DIR + filePath);
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
	
}
