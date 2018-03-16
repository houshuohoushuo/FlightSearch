package com.example.monkey.myapplication.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ItinerarySystem implements Serializable{
	private HashMap<String, FlightInfo> flights = new HashMap<String, FlightInfo>();
    private HashMap<String, ArrayList<FlightInfo>> clientBooked = new HashMap<String, ArrayList<FlightInfo>>();

	/**
	 * Returns all flights that depart from origin and arrive at destination on
	 * the given date.
	 *
	 * @param date
	 *            a departure date (in the format YYYY-MM-DD)
	 * @param origin
	 *            a flight origin
	 * @param destination
	 *            a flight destination
	 * @return the flights that depart from origin and arrive at destination on
	 *         the given date formatted with one flight per line in exactly this
	 *         format: Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,
	 *         Destination,Price (the dates are in the format YYYY-MM-DD; the
	 *         price has exactly two decimal places)
	 * @throws ParseException
	 *             exception appears when the time format in buildList does not
	 *             match
	 */
	public String[] getFlights(String date, String origin, String destination) throws ParseException {
		ArrayList<ArrayList<FlightInfo>> flightOut = new ArrayList<ArrayList<FlightInfo>>();
		ArrayList<FlightInfo> flightIn = new ArrayList<FlightInfo>();
		// add hours and minutes to the date given
		date += " 00:00";
		// use the helper function buildList to build an ArrayList containing
		// flights or itineraries in ArrayLists
		buildList(date, origin, destination, flightIn, flightOut);
		List<String> sList = new ArrayList<String>();
		// return only flights instead of a mix of flights and itineraries
		for (ArrayList<FlightInfo> flightL : flightOut) {
			if (flightL.size() == 1) {
				sList.add(flightL.get(0).toString());
			}
		}

		String[] result = new String[sList.size()];

		int i = 0;
		for(String s : sList){
			result[i++] = s;
		}
		return result;
	}

	/**
	 * Returns all itineraries that depart from origin and arrive at destination
	 * on the given date. If an itinerary contains two consecutive flights F1
	 * and F2, then the destination of F1 should match the origin of F2. To
	 * simplify our task, if there are more than 6 hours between the arrival of
	 * F1 and the departure of F2, then we do not consider this sequence for a
	 * possible itinerary (we judge that the stopover is too long).
	 *
	 * @param date
	 *            a departure date (in the format YYYY-MM-DD)
	 * @param origin
	 *            a flight original
	 * @param destination
	 *            a flight destination
	 * @return itineraries that depart from origin and arrive at destination on
	 *         the given date with stopovers at or under 6 hours. Each itinerary
	 *         in the output should contain one line per flight, in the format:
	 *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,
	 *         Destination followed by total price (on its own line, exactly two
	 *         decimal places), followed by total duration (on its own line, in
	 *         format HH:MM).
	 * @throws ParseException
	 *             exception appears when the time format in buildList or
	 *             filterList does not match
	 */
	public String[] getItineraries(String date, String origin, String destination) throws ParseException {
		ArrayList<ArrayList<FlightInfo>> flightOut = new ArrayList<ArrayList<FlightInfo>>();
		ArrayList<FlightInfo> flightIn = new ArrayList<FlightInfo>();
		// add hours and minutes to the date given
		date += " 00:00";
		// use the helper function buildList to build an ArrayList containing
		// flights or itineraries in ArrayLists
		buildList(date, origin, destination, flightIn, flightOut);
		filterList(flightOut);
		List<String> sList = new ArrayList<String>();

		for (ArrayList<FlightInfo> flightL : flightOut) {
			String s = "";
			for (FlightInfo flight : flightL) {
				s += flight.getFlightnum()+","+flight.getDepartureDateTime()
						+","+flight.getArrivalDateTime()+","+flight.getAirline()
						+","+flight.getOrigin()+","+flight.getDestination()+","+flight.getNumSeat() + "\n";
			}
			double cost = 0;
			for (FlightInfo flight : flightL) {

				cost += Double.parseDouble(flight.getCost());
			}
			long duration = (flightL.get(flightL.size() - 1).getAd().getTime()) - (flightL.get(0).getDd().getTime());
			String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
					TimeUnit.MILLISECONDS.toMinutes(duration)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)));
			sList.add(new String(s + new DecimalFormat("#.00").format(cost) + "\n" + time + "\n"));
		}
		String[] result = new String[sList.size()];

		int i = 0;
		for(String s : sList){
			result[i++] = s;
		}
		return result;
	}
	/**
	 * a helper function of getFlights and getItineray. Based on information
	 * that is given, build an ArrayList which contains flights or Itineraries
	 * in ArrayLists.
	 *
     * @param dateTime
     *            a departure date (in the format YYYY-MM-DD)
	 * @param origin
	 *            a flight origin
	 * @param destination
	 *            a flight destination
	 * @param flightInnerL
	 *            an ArrayList which contains a flight or a itinerary
	 * @param flightOutterL
	 *            an ArrayList which contains ArrayLists of flight or itinerary
	 * @throws ParseException
	 *             exception appears when the time format is not in
	 *             "yyyy-MM-dd HH:mm"
	 */
	public void buildList(String dateTime, String origin, String destination, ArrayList<FlightInfo> flightInnerL,
						  ArrayList<ArrayList<FlightInfo>> flightOutterL) throws ParseException {

		for (FlightInfo flight : flights.values()) {
			// check if the flight matches the given origin and departure date
			if (flight.getOrigin().equals(origin)
					&& flight.getDepartureDateTime().split(" ")[0].equals(dateTime.split(" ")[0])) {

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date bTime = format.parse(dateTime);
				// check if the given time is after the departure time
				if (flight.getDd().after(bTime)) {
					// check if the final destination are the same
					if (flight.getDestination().equals(destination)) {
						// build a new sub arrayList and add all the elements
						// from the old sub arrayList to the new one,
						// then append the new one to the result arrayList
						ArrayList<FlightInfo> flightNewIn = new ArrayList<FlightInfo>();
						flightNewIn.addAll(flightInnerL);
						flightNewIn.add(flight);
						flightOutterL.add(flightNewIn);
					} else {
						// build a new sub arrayList and add all the elements
						// from the old sub arrayList to the new one
						ArrayList<FlightInfo> flightNewIn = new ArrayList<FlightInfo>();
						flightNewIn.addAll(flightInnerL);
						flightNewIn.add(flight);
						// call this method again. Input:dateTime will be the
						// arrival time of this flight; origin will be the
						// destination of this flight; （final) destination will
						// stay unchanged; replace the old sub arrayList
						// with the new one; the result arrayList will stay
						// unchanged
						buildList(flight.getArrivalDateTime(), flight.getDestination(), destination,
								flightNewIn, flightOutterL);
					}
				}
			}
		}
	}

	/**
	 * a helper function of getItineraries. Filter out the itineraries with
	 * duration between flights that are more than 6 hours and with no available seat
	 *
	 * @param flightOut
	 *            An arrayList which contains itineraries.
	 * @throws ParseException
	 *             exception appears when the time format in checkTimeItinerary
	 *             does not match
	 */
	public void filterList(ArrayList<ArrayList<FlightInfo>> flightOut) throws ParseException {
		ArrayList<ArrayList<FlightInfo>> newList = new ArrayList<ArrayList<FlightInfo>>();
		for (ArrayList<FlightInfo> flightL : flightOut) {
			if (flightL.size() == 1) {
				double numSeat = flightL.get(0).getNumSeat();
				if (numSeat > 0) {
					newList.add(flightL);
				}
			} else {

				int j = 0;
				for (int i = 0; i + 1 < flightL.size(); i++) {
					String aTime = flightL.get(i).getArrivalDateTime().split(" ")[1];
					String bTime = flightL.get(i + 1).getDepartureDateTime().split(" ")[1];
					Integer numSeatA = flightL.get(i).getNumSeat();
					Integer numSeatB = flightL.get(i+1).getNumSeat();
					// check if every durations between flights in this itinerary
					// are less than 6 hours and there are available seats in both flights	
					//and check if there is available seat in flights
					if (this.checkTimeItinerary(bTime, aTime)&& numSeatA>0 && numSeatB>0) {
						j += 1;
					}
					// add the itineraries with available seats and all duration between flights
					// are less than 6 hours to newList
					//
					if (j == flightL.size() - 1) {
						newList.add(flightL);
					}
				}
			}
		}
		// clear the elements in the flighOut and put the elements from newList
		// to flightOut
		// (which are the ones that meet the condition of 6 hours duration)
		flightOut.clear();
		flightOut.addAll(newList);
	}

	/**
	 * a helper function of getItineraries. Return true iff the time difference
	 * between aTime and bTime is less than 6 hours and aTime is after bTime,
	 * false otherwise.
	 *
	 * @param aTime
	 *            a time in the format of "HH:mm"
	 * @param bTime
	 *            a time in the format of "HH:mm"
	 * @return Return true iff the time difference between aTime and bTime is
	 *         less than 6 hours and aTime is after bTime, false otherwise.
	 * @throws ParseException
	 *             exception appears when the time format is not in "HH:mm"
	 */
	public boolean checkTimeItinerary(String aTime, String bTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date timeA = format.parse(aTime);
		Date timeB = format.parse(bTime);
		// get the time difference between aTime and bTime in milliseconds
		long difference = timeA.getTime() - timeB.getTime();
		return !(difference >= 3600000 * 6);
	}

	/**
	 * Returns the same itineraries as getItineraries produces, but sorted
	 * according to total itinerary cost, in non-decreasing order.
	 *
	 * @param date
	 *            a departure date (in the format YYYY-MM-DD)
	 * @param origin
	 *            a flight original
	 * @param destination
	 *            a flight destination
	 * @return itineraries (sorted in non-decreasing order of total itinerary
	 *         cost) that depart from origin and arrive at destination on the
	 *         given date with stopovers at or under 6 hours. Each itinerary in
	 *         the output should contain one line per flight, in the format:
	 *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,
	 *         Destination followed by total price (on its own line, exactly two
	 *         decimal places), followed by total duration (on its own line, in
	 *         format HH:MM).
	 * @throws ParseException
	 *             exception appears when the format of time given in buildList
	 *             does not match
	 */
	public String[] getItinerariesSortedByCost(String date, String origin, String destination)
			throws ParseException {
		ArrayList<ArrayList<FlightInfo>> flightOut = new ArrayList<ArrayList<FlightInfo>>();
		ArrayList<FlightInfo> flightIn = new ArrayList<FlightInfo>();
		date += " 00:00";
		HashMap<Double, ArrayList<FlightInfo>> itineraryMap = new HashMap();
		buildList(date, origin, destination, flightIn, flightOut);
		filterList(flightOut);
		// uses helper function to search for possible itineraries
		List<String> sList = new ArrayList<String>();
		for (ArrayList<FlightInfo> flightL : flightOut) {
			// iterates through itineraries
			double cost = 0;
			for (FlightInfo flight : flightL) {
				// calculates total cost for each itinerary
				cost += Double.parseDouble(flight.getCost());
			}
			itineraryMap.put(cost, flightL);
			// makes a map with the cost as key and itinerary as value
		}
		ArrayList<Double> sortedList = new ArrayList();
		// puts the keys into a list
		for (Double key : itineraryMap.keySet()) {
			sortedList.add(key);
		}
		Collections.sort(sortedList); // sorts the keys

		for (Double element : sortedList) {
			// constructs result string in the order of sorted keys
			ArrayList<FlightInfo> flightL = itineraryMap.get(element);
			String s = "";
			for (FlightInfo flight : flightL) {
				s += flight.getFlightnum()+","+flight.getDepartureDateTime()
						+","+flight.getArrivalDateTime()+","+flight.getAirline()
						+","+flight.getOrigin()+","+flight.getDestination()+","+flight.getNumSeat() + "\n";
				// ignores cost of each flight
			}
			double cost = 0;
			for (FlightInfo flight : flightL) {
				// calculates total cost of each itinerary
				cost += Double.parseDouble(flight.getCost());
			}
			long duration = (flightL.get(flightL.size() - 1).getAd().getTime()) - (flightL.get(0).getDd().getTime());
			// calculates total duration of each itinerary
			String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
					TimeUnit.MILLISECONDS.toMinutes(duration)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)));
			// converts millisecond to HH:mm format
			sList.add(new String(s + new DecimalFormat("#.00").format(cost) + "\n" + time + "\n"));
		}
		String[] result = new String[sList.size()];

		int i = 0;
		for(String s : sList){
			result[i++] = s;
		}
		return result;
	}

	/**
	 * Returns the same itineraries as getItineraries produces, but sorted
	 * according to total itinerary travel time, in non-decreasing order.
	 *
	 * @param date
	 *            a departure date (in the format YYYY-MM-DD)
	 * @param origin
	 *            a flight original
	 * @param destination
	 *            a flight destination
	 * @return itineraries (sorted in non-decreasing order of travel itinerary
	 *         travel time) that depart from origin and arrive at destination on
	 *         the given date with stopovers at or under 6 hours. Each itinerary
	 *         in the output should contain one line per flight, in the format:
	 *         Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,
	 *         Destination followed by total price (on its own line, exactly two
	 *         decimal places), followed by total duration (on its own line, in
	 *         format HH:MM).
	 * @throws ParseException
	 */
	public String[] getItinerariesSortedByTime(String date, String origin, String destination)
			throws ParseException {
		ArrayList<ArrayList<FlightInfo>> flightOut = new ArrayList<ArrayList<FlightInfo>>();
		ArrayList<FlightInfo> flightIn = new ArrayList<FlightInfo>();
		date += " 00:00";
		HashMap<Long, ArrayList<FlightInfo>> itineraryMap = new HashMap();
		buildList(date, origin, destination, flightIn, flightOut);
		filterList(flightOut);
		// uses helper function to get possible itineraries
		List<String> sList = new ArrayList<String>();
		for (ArrayList<FlightInfo> flightL : flightOut) {
			long duration = (flightL.get(flightL.size() - 1).getAd().getTime()) - (flightL.get(0).getDd().getTime());
			itineraryMap.put(duration, flightL);
			// calculates total duration for each itinerary
			// makes a map with duration as key and itinerary as value
		}
		ArrayList<Long> sortedList = new ArrayList();
		// puts the keys of the map into a list
		for (Long key : itineraryMap.keySet()) {
			sortedList.add(key);
		}
		// sorts the list
		Collections.sort(sortedList);

		for (long element : sortedList) {
			// constructs result string in the order of sorted list
			ArrayList<FlightInfo> flightL = itineraryMap.get(element);
			String s = "";
			for (FlightInfo flight : flightL) {
				// ignores cost for each flight
				s += flight.getFlightnum()+","+flight.getDepartureDateTime()
						+","+flight.getArrivalDateTime()+","+flight.getAirline()
						+","+flight.getOrigin()+","+flight.getDestination()+","+flight.getNumSeat() + "\n";
			}
			double cost = 0;
			for (FlightInfo flight : flightL) {
				// calculates total cost for each itinerary
				cost += Double.parseDouble(flight.getCost());
			}
			long duration = (flightL.get(flightL.size() - 1).getAd().getTime()) - (flightL.get(0).getDd().getTime());
			// calculates total duration
			String time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
					TimeUnit.MILLISECONDS.toMinutes(duration)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)));
			// converts millisecond to HH:mm format
			sList.add(new String(s + new DecimalFormat("#.00").format(cost) + "\n" + time + "\n"));
		}

		String[] result = new String[sList.size()];

		int i = 0;
		for(String s : sList){
			result[i++] = s;
		}
		return result;
	}
	/**
	 * Uploads flight information to the application from the file at the given
	 * path.
	 *
	 * @param file
	 *            the path to an input csv file of flight information with lines
	 *            in the format:
	 *            Number,DepartureDateTime,ArrivalDateTime,Airline,Origin,
	 *            Destination,Price (the dates are in the format YYYY-MM-DD; the
	 *            price has exactly two decimal places)
	 * @throws FileNotFoundException
	 *             exception appears when the path or file given does not exist
	 */
	public void uploadFlightInfo(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		while (sc.hasNext()) {
			String line = sc.nextLine();
			String[] s = line.split(",");
			//create a hashmap with flight info in it
			flights.put(s[0], new FlightInfo(s[0], s[1], s[2], s[3], s[4], s[5], s[6],new Integer(s[7])));
		}
		sc.close();
	}

	/**
	 * book a flight or itinerary. 
	 * @param lst a list of Strings of flight numbers from an Itinerary that the user chooses to book
	 * @throws IOException
	 */
    public void book(ArrayList<String> lst, String account, File bookingsFile, File flightfile) throws IOException {
        if (!bookingsFile.exists()) {
            try {
                bookingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		for (String flightNum : lst) {
			//find the flights in data structure that match the flight numbers given
			for (FlightInfo mapFlight : flights.values()) {
				if (flightNum.equals(mapFlight.getFlightnum())) {
					if (mapFlight.getNumSeat()>0) {
						//minus one seat after booking
						mapFlight.setNumSeat(mapFlight.getNumSeat()-1);
                        ArrayList<FlightInfo> bFlights = new ArrayList<FlightInfo>();
                        if (clientBooked.get(account) != null) {
                            bFlights.addAll(clientBooked.get(account));
                        }
                        bFlights.add(mapFlight);
                        clientBooked.put(account, bFlights);

					}
				}
			}}
//		System.out.println(bookedFlights);
		writeNumSeat(flightfile);

        try {
            OutputStream file = new FileOutputStream(bookingsFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // Serialize the students Map.
            output.writeObject(clientBooked);
            output.close();
        } catch (IOException ex) {
        }

    }

	/**
	 * a helper function of book, which writes the file of flightInfo after booking
	 * @throws IOException
	 */
	public void writeNumSeat(File file) throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		for (FlightInfo value : flights.values()) {
			//write all the changes made to data structure to the txt that saves flight info
			pw.println(value.toString());
		}
		pw.close();
	}


	/**
	 * upload the info of flights booked by an account. Used when user wants to display their booked flights
     * @param bookingsFile of bookings.ser, the file that saves accounts and their booked flights
     * @param account the client who books these flights
     * @return an ArrayList that contains all the flights booked by account in FlightInfo structure
	 * @throws FileNotFoundException the path is incorrect 
	 */
    public ArrayList<FlightInfo> viewBookedFlights(File bookingsFile, String account) throws FileNotFoundException {

        if (!bookingsFile.exists()) {
            try {
                bookingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                InputStream file = new FileInputStream(bookingsFile);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);

                // Deserialize the students Map.
                clientBooked = (HashMap<String, ArrayList<FlightInfo>>) input.readObject();
                input.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return clientBooked.get(account);
        }
        return new ArrayList<FlightInfo>();
    }


	public void saveToFile(File file) throws IOException{

		PrintWriter pw = new PrintWriter(new FileWriter(file));

		for(FlightInfo flightInfo: flights.values()){

			pw.println(flightInfo.toString());

		}

		pw.close();

	}

	public FlightInfo getFlightInfo(String flightNum){
		return flights.get(flightNum);
	}

	public HashMap getHashMap(){
		return flights;
	}


}