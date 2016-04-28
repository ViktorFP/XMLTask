package by.epamlab;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class FlightSegment {
	private Date departureDateTime;
	private Date arrivalDateTime;
	private String flightNumber;
	private Airport departureAirport;
	private Airport arrivalAirport;
	private String operatingAirlineCode;

	private Date dateTypeToDate(String dateType) throws DatatypeConfigurationException{
	DatatypeFactory factory = DatatypeFactory.newInstance();
			XMLGregorianCalendar xmlCal = factory.newXMLGregorianCalendar(dateType);
			Calendar cal = xmlCal.toGregorianCalendar();
			return cal.getTime();		
	}
	
	private String dateToDateType(Date date) throws DatatypeConfigurationException{
		GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.setTime(date);
		XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcalendar);		
		return xmlDate.toString();
	}
	
	public String getDepartureDateTime() throws DatatypeConfigurationException {
		return dateToDateType(departureDateTime);
	}

	public String getArrivalDateTime() throws DatatypeConfigurationException {
		return dateToDateType(arrivalDateTime);
	}

	public void setDepartureDateTime(String departureDateTime) throws DatatypeConfigurationException {
		this.departureDateTime = dateTypeToDate(departureDateTime);		
	}

	public void setArrivalDateTime(String arrivalDateTime) throws DatatypeConfigurationException {
		this.arrivalDateTime = dateTypeToDate(arrivalDateTime);
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(Airport arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getOperatingAirlineCode() {
		return operatingAirlineCode;
	}

	public void setOperatingAirlineCode(String operatingAirlineCode) {
		this.operatingAirlineCode = operatingAirlineCode;
	}

}
