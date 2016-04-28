package by.epamlab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AirlinesDOMBuilder {
	private List<Airline> airlines;
	private DocumentBuilder docBuilder;
	private DocumentBuilderFactory factory;
	private final static String FILE_PATH = "src/";

	private final static String GENERAL_ELEMENT = "Airline";
	private final static String SCHEDULE_ELEMENT = "Schedule";
	private final static String FLIGHT_ELEMENT="FlightSegment";

	private final static String NAME_ATTRIBUTE = "name";
	private final static String COUNTRY_ATTRIBUTE = "counrty";
	private final static String DEPARTURE_TIME="DepartureDateTime";
	private final static String ARRIVAL_TIME="ArrivalDateTime";
	private final static String FLIGHT_NUMBER="FlightNumber";
	private final static String DEPARTURE="DepartureAirport";
	private final static String ARRIVAL_TO="ArrivalAirport";
	private final static String LOCATION_CODE="LocationCode";
	private final static String CONTEXT_CODE="CodeContext";
	private final static String OPERATION_AIRLINE="OperatingAirline";
	private final static String CODE="Code";
	
	public AirlinesDOMBuilder() {
		this.airlines = new ArrayList<Airline>();
		// creat the DOMAnaliser
		factory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Error of configuration: " + e);
		}
	}

	public void buildSetAirlines(String fileName) throws DatatypeConfigurationException {
		Document doc = null;
		try {// parsing of XML and building of DOM structure
			doc = docBuilder.parse(FILE_PATH + fileName);
			Element root = doc.getDocumentElement();
			// getting list of children elements <Airline>
			NodeList airlineList = root.getElementsByTagName(GENERAL_ELEMENT);
			for (int i = 0; i < airlineList.getLength(); i++) {
				Element airlineElement = (Element) airlineList.item(i);
				Airline airline = buildAirline(airlineElement);
				airlines.add(airline);
			}
		} catch (IOException | SAXException e) {
			System.err.println("Error in buildSetAirlines: " + e);
		}
	}

	private Airline buildAirline(Element airlineElement) throws DatatypeConfigurationException {
		Airline airline = new Airline();
		// fill up the object of airline
		airline.setName(airlineElement.getAttribute(NAME_ATTRIBUTE));
		airline.setCounrty(airlineElement.getAttribute(COUNTRY_ATTRIBUTE));

		NodeList shedules = airlineElement.getElementsByTagName(SCHEDULE_ELEMENT);
		Element schedule = (Element) shedules.item(0);
		List<FlightSegment> flightSegments = airline.getSchedule().getFlightSegments();
		NodeList flightSegmentsList = schedule.getElementsByTagName(FLIGHT_ELEMENT);
		for (int i = 0; i < flightSegmentsList.getLength(); i++) {
			Element flightSegmentElement = (Element) flightSegmentsList.item(i);
			FlightSegment flightSegment = new FlightSegment();
			flightSegment.setDepartureDateTime(flightSegmentElement.getAttribute(DEPARTURE_TIME));
			flightSegment.setArrivalDateTime(flightSegmentElement.getAttribute(ARRIVAL_TIME));
			flightSegment.setFlightNumber(flightSegmentElement.getAttribute(FLIGHT_NUMBER));

			Element departureAirport = (Element) (flightSegmentElement.getElementsByTagName(DEPARTURE))
					.item(0);
			Element arrivalAirport = (Element) (flightSegmentElement.getElementsByTagName(ARRIVAL_TO)).item(0);
			Element operatingAirline = (Element) (flightSegmentElement.getElementsByTagName(OPERATION_AIRLINE))
					.item(0);

			flightSegment.setDepartureAirport(setAirport(departureAirport));
			flightSegment.setArrivalAirport(setAirport(arrivalAirport));
			flightSegment.setOperatingAirlineCode(operatingAirline.getAttribute(CODE));
			flightSegments.add(flightSegment);
		}
		return airline;
	}

	private Airport setAirport(Element element) {
		Airport airport = new Airport();
		airport.setLocationCode(element.getAttribute(LOCATION_CODE));
		airport.setCodeContext(element.getAttribute(CONTEXT_CODE));
		return airport;
	}

	public void exportDataToXml(String fileName) throws DatatypeConfigurationException {
		Document document = docBuilder.newDocument();
		Element rootElement = document.createElement("tns:Airlines");
		setAttr(document, rootElement,  "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		setAttr(document, rootElement,  "xmlns:tns", "http://www.example.org/Airlines");
		setAttr(document, rootElement,  "xsi:schemaLocation", "http://www.example.org/Airlines Airlines.xsd");		
		document.appendChild(rootElement);
		for (Airline airline : airlines) {
			Element eAirline = document.createElement(GENERAL_ELEMENT);
			// fill up the object of airline
			fillUp(document, eAirline, airline);
			rootElement.appendChild(eAirline);
		}
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(FILE_PATH + fileName + ".xml"));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private void fillUp(Document document, Element eAirline, Airline airline) throws DatatypeConfigurationException {
		setAttr(document, eAirline, NAME_ATTRIBUTE, airline.getName());
		setAttr(document, eAirline, COUNTRY_ATTRIBUTE, airline.getCounrty());

		Element eSchedule = document.createElement(SCHEDULE_ELEMENT);
		eAirline.appendChild(eSchedule);
		
		List <FlightSegment>flightSegments=airline.getSchedule().getFlightSegments();
		for(FlightSegment flightSegment:flightSegments){
			Element eFlightSegment = document.createElement(FLIGHT_ELEMENT);
			setAttr(document, eFlightSegment,  DEPARTURE_TIME, flightSegment.getDepartureDateTime());
			setAttr(document, eFlightSegment,  ARRIVAL_TIME, flightSegment.getArrivalDateTime());
			setAttr(document, eFlightSegment,  FLIGHT_NUMBER, flightSegment.getFlightNumber());
			eSchedule.appendChild(eFlightSegment);
			
			Element eDepartureAirport = document.createElement(DEPARTURE);			
			setAttr(document, eDepartureAirport,  LOCATION_CODE, flightSegment.getDepartureAirport().getLocationCode());
			setAttr(document, eDepartureAirport,  CONTEXT_CODE, flightSegment.getDepartureAirport().getCodeContext());
			eFlightSegment.appendChild(eDepartureAirport);
			
			Element eArrivalAirport = document.createElement(ARRIVAL_TO);
			setAttr(document, eArrivalAirport,  LOCATION_CODE, flightSegment.getArrivalAirport().getLocationCode());
			setAttr(document, eArrivalAirport,  CONTEXT_CODE, flightSegment.getArrivalAirport().getCodeContext());
			eFlightSegment.appendChild(eArrivalAirport);
			
			Element eOperatingAirline = document.createElement(OPERATION_AIRLINE);
			setAttr(document, eOperatingAirline,  CODE, flightSegment.getOperatingAirlineCode());
			eFlightSegment.appendChild(eOperatingAirline);
		}
	}

	private void setAttr(Document document, Element element, String attributeName, String value) {
		Attr attr = document.createAttribute(attributeName);
		attr.setValue(value);
		element.setAttributeNode(attr);
	}
}
