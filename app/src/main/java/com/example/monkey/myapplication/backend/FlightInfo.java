package com.example.monkey.myapplication.backend;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightInfo implements Serializable{
	private String flightnum;
	private String departureDateTime;
	private String arrivalDateTime;
	private String airline;
	private String origin;
	private String destination;
	private String cost;
	private long duration;
	private Date dd;
	private Date ad;
	private Integer numSeat;

	public FlightInfo(String flightnum, String departureDateTime, String arrivalDateTime, String airline, String origin,
			String destination, String cost, Integer numSeat) {
		super();
		this.flightnum = flightnum;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
		this.numSeat = numSeat;
		try {
			this.dd = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureDateTime);
			this.ad = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalDateTime);
		} catch (ParseException e) {
			System.out.println("the format of the date is not correct");
		}
	}
	
	public Integer getNumSeat() {
		return numSeat;
	}
	
	public void setNumSeat(Integer numSeat) {
		this.numSeat = numSeat;
	}

	public Date getDd() {
		return dd;
	}

	public void setDd(Date dd) {
		this.dd = dd;
	}

	public Date getAd() {
		return ad;
	}

	public void setAd(Date ad) {
		this.ad = ad;
	}

	public String getFlightnum() {
		return flightnum;
	}

	public void setFlightnum(String flightnum) {
		this.flightnum = flightnum;
	}

	public String getDepartureDateTime() {
		return departureDateTime;
	}

	public void setDepartureDateTime(String departureDateTime) {
		this.departureDateTime = departureDateTime;
		try {
			this.setDd((new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureDateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getArrivalDateTime() {
		return arrivalDateTime;
	}

	public void setArrivalDateTime(String arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
		try {
			this.setAd((new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalDateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public long getDuration() {
		return (long) (this.getAd().getTime() - this.getDd().getTime());
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return flightnum + "," + departureDateTime + "," + arrivalDateTime + "," + airline + "," + origin + ","
				+ destination + "," + cost + "," + numSeat;
	}

}
