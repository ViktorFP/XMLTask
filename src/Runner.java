import javax.xml.datatype.DatatypeConfigurationException;

import by.epamlab.AirlinesDOMBuilder;

public class Runner {

	public static void main(String[] args) {
		try {
			AirlinesDOMBuilder airlinesDOMBuilder=new AirlinesDOMBuilder();
			airlinesDOMBuilder.buildSetAirlines("Airlines.xml");			
			airlinesDOMBuilder.exportDataToXml("testFile");
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
	}

}
