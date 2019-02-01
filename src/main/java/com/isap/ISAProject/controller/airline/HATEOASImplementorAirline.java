package com.isap.ISAProject.controller.airline;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.controller.hotel.HotelController;
import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.airline.Flight;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.FlightSeat;
import com.isap.ISAProject.model.airline.FlightSeatCategory;
import com.isap.ISAProject.model.airline.FlightSegment;
import com.isap.ISAProject.model.airline.LuggageInfo;
import com.isap.ISAProject.model.airline.Passenger;
import com.isap.ISAProject.model.airline.Ticket;

public class HATEOASImplementorAirline {

	public static Resource<Airline> createAirline(Airline airline) {
		Resource<Airline> resource = new Resource<Airline>(airline);
		resource.add(linkTo(methodOn(AirlineController.class).getAirlineById(airline.getId())).withRel("self"));
		resource.add(linkTo(methodOn(AirlineController.class).getAllAirlines(null)).slash("?page=0&size=5").withRel("all_airlines"));
		resource.add(linkTo(methodOn(AirlineController.class).getLuggageInfosForAirlineWithId(airline.getId())).withRel("airline-luggage-infos"));
		resource.add(linkTo(methodOn(AirlineController.class).getFlightConfigurationsForAirlineWithId(airline.getId())).withRel("airline_flight_configurations"));
		resource.add(linkTo(methodOn(AirlineController.class).getFlightsOfAirline(airline.getId())).withRel("airline_flights"));
		resource.add(linkTo(methodOn(AirlineController.class).getLocationOfAirlineWithId(airline.getId())).withRel("location"));
		// TODO : Link za brze rezervacije
		return resource;
	}

	public static List<Resource<Airline>> createAirlinesList(List<Airline> airlines) {
		List<Resource<Airline>> list = new ArrayList<Resource<Airline>>();
		for(Airline a : airlines) {
			list.add(HATEOASImplementorAirline.createAirline(a));
		}
		return list;
	}

	public static Resource<LuggageInfo> createLuggageInfo(LuggageInfo luggageInfo) {
		Resource<LuggageInfo> resource = new Resource<LuggageInfo>(luggageInfo);
		resource.add(linkTo(methodOn(LuggageInfoController.class).getLuggageInfoWithId(luggageInfo.getId())).withRel("self"));
		resource.add(linkTo(methodOn(LuggageInfoController.class).getLuggageInfoWithId(null)).slash("?page=0&size=5").withRel("all-luggage-infos"));
		resource.add(linkTo(methodOn(LuggageInfoController.class).getAirlineForLuggageInfoWithId(luggageInfo.getId())).withRel("owner-airline"));
		return resource;
	}

	public static List<Resource<LuggageInfo>> createLuggageInfosList(List<LuggageInfo> luggageInfos) {
		List<Resource<LuggageInfo>> list = new ArrayList<Resource<LuggageInfo>>();
		for(LuggageInfo li : luggageInfos)
			list.add(HATEOASImplementorAirline.createLuggageInfo(li));
		return list;
	}

	public static Resource<Location> createDestination(Location destination) {
		Resource<Location> resource = new Resource<Location>(destination);
		resource.add(linkTo(methodOn(LocationController.class).getDestinationById(destination.getId())).withRel("self"));
		resource.add(linkTo(methodOn(LocationController.class).getAllDestinations(null)).slash("page=0&size=5").withRel("all-destinations"));
		resource.add(linkTo(methodOn(LocationController.class).getAirlinesOnLocation(destination.getId())).withRel("airlines-on-location"));
		resource.add(linkTo(methodOn(LocationController.class).getHotelsOnLocation(destination.getId())).withRel("hotels-on-location"));
		resource.add(linkTo(methodOn(LocationController.class).getOfficesOnLocation(destination.getId())).withRel("offices-on-location"));
		resource.add(linkTo(methodOn(LocationController.class).getFlightsFromDestinationWithId(destination.getId())).withRel("flights-from-location"));
		resource.add(linkTo(methodOn(LocationController.class).getFlightsToDestinationWithId(destination.getId())).withRel("flights-to-location"));
		resource.add(linkTo(methodOn(LocationController.class).getLongLatForDestinationWithName(destination.getId())).withRel("long_lat"));
		return resource;
	}

	public static List<Resource<Location>> createDestinationsList( List<Location> destinations) {
		List<Resource<Location>> list = new ArrayList<Resource<Location>>();
		for(Location d : destinations)
			list.add(HATEOASImplementorAirline.createDestination(d));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve konfiguracije, na avio kompaniju koja je poseduje, na svoje segmente
	 * @param configuration
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<FlightConfiguration> createFlightConfiguration(
			FlightConfiguration configuration) {
		Resource<FlightConfiguration> resource = new Resource<FlightConfiguration>(configuration);
		resource.add(linkTo(methodOn(FlightConfigurationController.class).getFlightConfigurationWithId(configuration.getId())).withRel("self"));
		resource.add(linkTo(methodOn(FlightConfigurationController.class).getAllFlightConfigurations(null)).slash("page=0&size=5").withRel("all-configurations"));
		resource.add(linkTo(methodOn(FlightConfigurationController.class).getAirlineForConfigurationWithId(configuration.getId())).withRel("owner-airline"));
		resource.add(linkTo(methodOn(FlightConfigurationController.class).getSegmentsForConfigurationWithId(configuration.getId())).withRel("configuration-segments"));
		return resource;
	}

	/**
	 * @param flightConfigurations - Lista konfiguracija za koje se kreiraju linkovi
	 * @return Lista resursa konfiguracija sa kreiranim linkovima
	 */
	public static List<Resource<FlightConfiguration>> createFlightConfigurationsList(List<FlightConfiguration> flightConfigurations) {
		List<Resource<FlightConfiguration>> list = new ArrayList<Resource<FlightConfiguration>>();
		for(FlightConfiguration fc : flightConfigurations)
			list.add(HATEOASImplementorAirline.createFlightConfiguration(fc));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve segmente, na konfiguraciju koja je poseduje, na kategoriju kojoj pripada
	 * @param segment
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<FlightSegment> createFlightSegment(
			FlightSegment segment) {
		Resource<FlightSegment> resource = new Resource<FlightSegment>(segment);
		resource.add(linkTo(methodOn(FlightSegmentController.class).getFlightSegmentWithId(segment.getId())).withRel("self"));
		resource.add(linkTo(methodOn(FlightSegmentController.class).getAllFlightSegments(null)).slash("page=0&size=5").withRel("all-segments"));
		resource.add(linkTo(methodOn(FlightConfigurationController.class).getFlightConfigurationWithId(segment.getConfiguration().getId())).withRel("owner-configuration"));
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getFlightSeatCategoryWithId(segment.getCategory().getId())).withRel("category"));
		return resource;
	}

	/**
	 * @param flightSegments - Lista segmenata za koje se kreiraju linkovi
	 * @return Lista resursa segemnata sa kreiranim linkovima
	 */
	public static List<Resource<FlightSegment>> createFlightSegmentsList(
			List<FlightSegment> flightSegments) {
		List<Resource<FlightSegment>> list = new ArrayList<Resource<FlightSegment>>();
		for(FlightSegment fs : flightSegments)
			list.add(HATEOASImplementorAirline.createFlightSegment(fs));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve letove, na destinaciju sa koje poleće, na destinaciju na koju sleće, na konfiguraciju leta, na sedišta
	 * @param flight
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<Flight> createFlight(@Valid Flight flight) {
		Resource<Flight> resource = new Resource<Flight>(flight);
		resource.add(linkTo(methodOn(FlightController.class).getFlightById(flight.getId())).withRel("self"));
		resource.add(linkTo(methodOn(FlightController.class).getAllFlights(null)).slash("page=0%size=5").withRel("all-flights"));
		resource.add(linkTo(methodOn(FlightController.class).getStartDestinationForFlightWithId(flight.getId())).withRel("start-destination"));
		resource.add(linkTo(methodOn(FlightController.class).getFinishDestinationForFlightWithId(flight.getId())).withRel("finish-destination"));
		resource.add(linkTo(methodOn(FlightController.class).getFlightSeatsForFlightWithId(flight.getId())).withRel("seats"));
		return resource;
	}

	/**
	 * @param flights - Lista letova za koje se kreiraju linkovi
	 * @return Lista letova sa kreiranim linkovima
	 */
	public static List<Resource<Flight>> createFlightsList(List<Flight> flights) {
		List<Resource<Flight>> list = new ArrayList<Resource<Flight>>();
		for(Flight f : flights)
			list.add(HATEOASImplementorAirline.createFlight(f));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve letove, na destinaciju sa koje poleće, na destinaciju na koju sleće, na konfiguraciju leta, na sedišta
	 * @param flight
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<FlightSeatCategory> createFlightSeatCategory(
			FlightSeatCategory flightSeatCategory) {
		Resource<FlightSeatCategory> resource = new Resource<FlightSeatCategory>(flightSeatCategory);
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getFlightSeatCategoryWithId(flightSeatCategory.getId())).withRel("self"));
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getAllFlightSeatCategories(null)).slash("page=0&size=5").withRel("all-categories"));
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getAirlineForSeatWithId(flightSeatCategory.getId())).withRel("owner-airline"));
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getSeatsInThisCategory(flightSeatCategory.getId())).withRel("seats"));
		resource.add(linkTo(methodOn(FlightSeatCategoryController.class).getSegmentsOfThisCategory(flightSeatCategory.getId())).withRel("segments"));
		return resource;
	}

	/**
	 * @param flightSeatCategories - Lista kategorija za koje se kreiraju linkovi
	 * @return Lista kategorija sa kreiranim linkovima
	 */
	public static List<Resource<FlightSeatCategory>> createFlightSeatCategoriesList(
			List<FlightSeatCategory> flightSeatCategories) {
		List<Resource<FlightSeatCategory>> list = new ArrayList<Resource<FlightSeatCategory>>();
		for(FlightSeatCategory fsc : flightSeatCategories)
			list.add(HATEOASImplementorAirline.createFlightSeatCategory(fsc));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sva sedista, na informaciju o prtljagu, na putnika, na kartu, na let
	 * @param fs
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<FlightSeat> createFlightSeat(FlightSeat fs) {
		Resource<FlightSeat> resource = new Resource<FlightSeat>(fs);
		resource.add(linkTo(methodOn(SeatController.class).getSeatWithId(fs.getId())).withRel("self"));
		resource.add(linkTo(methodOn(SeatController.class).getAllSeats(null)).slash("page=0&size=5").withRel("all-seats"));
		resource.add(linkTo(methodOn(SeatController.class).getLuggageInfoForSeatWithId(fs.getId())).withRel("luggage-info"));
		resource.add(linkTo(methodOn(SeatController.class).getPassengerForSeatWithId(fs.getId())).withRel("passenger"));
		resource.add(linkTo(methodOn(SeatController.class).getTicketForSeatWithId(fs.getId())).withRel("ticket"));
		resource.add(linkTo(methodOn(SeatController.class).getFlightForSeatWithId(fs.getId())).withRel("flight"));
		return resource;
	}

	/**
	 * @param seats - Lista sedista za koje se kreiraju linkovi
	 * @return Lista sedista sa kreiranim linkovima
	 */
	public static List<Resource<FlightSeat>> createFlightSeatsList(
			List<FlightSeat> seats) {
		List<Resource<FlightSeat>> list = new ArrayList<Resource<FlightSeat>>();
		for(FlightSeat fs : seats)
			list.add(HATEOASImplementorAirline.createFlightSeat(fs));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve karte, na let, na sedista
	 * @param t
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<Ticket> createTicket(Ticket t) {
		Resource<Ticket> resource = new Resource<Ticket>(t);
		resource.add(linkTo(methodOn(TicketController.class).getTicketWithId(t.getId())).withRel("self"));
		resource.add(linkTo(methodOn(TicketController.class).getAllTickets(null)).slash("page=0&size=5").withRel("all-tickets"));
		resource.add(linkTo(methodOn(TicketController.class).getFlightForTicketWithId(t.getId())).withRel("flight"));
		resource.add(linkTo(methodOn(TicketController.class).getSeatsForTicketWithId(t.getId())).withRel("seats"));
		// TODO : link ka rezervaciji
		return resource;
	}
	
	/**
	 * @param tickets - Lista karata za koje se kreiraju linkovi
	 * @return Lista karata sa kreiranim linkovima
	 */
	public static List<Resource<Ticket>> createTicketsList(
			List<Ticket> tickets) {
		List<Resource<Ticket>> list = new ArrayList<Resource<Ticket>>();
		for(Ticket t : tickets)
			list.add(HATEOASImplementorAirline.createTicket(t));
		return list;
	}

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve putnike, na njegove letove, na njegova sedista, na njegove karte
	 * @param passenger
	 * @return Resurs koji poštuje HATEOAS princip
	 */
	public static Resource<Passenger> createPassenger(Passenger passenger) {
		Resource<Passenger> resource = new Resource<Passenger>(passenger);
		resource.add(linkTo(methodOn(PassengerController.class).getPassengerWithId(passenger.getId())).withRel("self"));
		resource.add(linkTo(methodOn(PassengerController.class).getAllPassengers(null)).slash("page=0&size=5").withRel("all-passengers"));
		resource.add(linkTo(methodOn(PassengerController.class).getFlightsForPassengerWithId(passenger.getId())).withRel("flights"));
		resource.add(linkTo(methodOn(PassengerController.class).getSeatsForPassengerWithId(passenger.getId())).withRel("seats"));
		resource.add(linkTo(methodOn(PassengerController.class).getTicketsForPassengerWithId(passenger.getId())).withRel("tickets"));
		return resource;
	}
	
	/**
	 * @param passengers - Lista putnika za koje se kreiraju linkovi
	 * @return Lista putnika sa kreiranim linkovima
	 */
	public static List<Resource<Passenger>> createPassengersList(List<Passenger> passengers) {
		List<Resource<Passenger>> list = new ArrayList<Resource<Passenger>>();
		for(Passenger p : passengers)
			list.add(HATEOASImplementorAirline.createPassenger(p));
		return list;
	}

}
