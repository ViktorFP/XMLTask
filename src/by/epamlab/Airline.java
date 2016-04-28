package by.epamlab;

import java.util.ArrayList;
import java.util.List;

public class Airline {
private String name;
private String counrty;
private Schedule schedule;



public Airline() {
	schedule=new Schedule();
}

public void setName(String name){
	this.name=name;
}

public Schedule getSchedule() {
	return schedule;
}

public void setSchedule(Schedule schedule) {
	this.schedule = schedule;
}

public String getName() {
	return name;
}

public String getCounrty() {
	return counrty;
}

public void setCounrty(String counrty){
	this.counrty=counrty;
}

class Schedule{

	private List<FlightSegment> flightSegments;

	public Schedule(){
		flightSegments=new ArrayList<>();
	}
	
	public List<FlightSegment> getFlightSegments() {
		return flightSegments;
	}

	public void setFlightSegments(List<FlightSegment> flightSegments) {
		this.flightSegments = flightSegments;
	} 		
}
}
