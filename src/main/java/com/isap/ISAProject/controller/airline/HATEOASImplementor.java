package com.isap.ISAProject.controller.airline;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.model.airline.Airline;
import com.isap.ISAProject.model.airline.Destination;
import com.isap.ISAProject.model.airline.FlightConfiguration;
import com.isap.ISAProject.model.airline.LuggageInfo;

class HATEOASImplementor {

	/**
	 * Linkovi koji će biti kreirani : na sebe, na sve aviokompanije, na svaku od svojih kolekcija
	 * @param airline
	 * @return Resurs koji podržava HATEOAS princip
	 */
	public static Resource<Airline> createAirline(Airline airline) {
		Resource<Airline> resource = new Resource<Airline>(airline);
		resource.add(linkTo(methodOn(AirlineController.class).getAirlineById(airline.getId())).withRel("self"));
		resource.add(linkTo(methodOn(AirlineController.class).getAllAirlines(null)).slash("?page=0&size=5").withRel("all-airlines"));
		resource.add(linkTo(methodOn(AirlineController.class).getDestinationsForAirlineWithId(airline.getId())).withRel("airline-destinations"));
		resource.add(linkTo(methodOn(AirlineController.class).getLuggageInfosForAirlineWithId(airline.getId())).withRel("airline-luggage-infos"));
		resource.add(linkTo(methodOn(AirlineController.class).getFlightConfigurationsForAirlineWithId(airline.getId())).withRel("airline-flight-configurations"));
		return resource;
	}
	
	/**
	 * @param airlines - Lista aviona za koje se kreiraju linkovi
	 * @return Lista resursa aviona sa kreiranim linkovima
	 */
	public static List<Resource<Airline>> createAirlinesList(List<Airline> airlines) {
		List<Resource<Airline>> list = new ArrayList<Resource<Airline>>();
		for(Airline a : airlines) {
			list.add(HATEOASImplementor.createAirline(a));
		}
		return list;
	}
	
	public static Resource<LuggageInfo> createLuggageInfo(LuggageInfo luggageInfo) {
		Resource<LuggageInfo> resource = new Resource<LuggageInfo>(luggageInfo);
		// TODO : Dodati linkove, dokumentaciju
		return resource;
	}
	
	/**
	 * @param luggageInfos - Lista informacija o prtljagu za koje se kreiraju linkovi
	 * @return Lista resursa informacija o prtljagu sa kreiranim linkovima
	 */
	public static List<Resource<LuggageInfo>> createLuggageInfosList(List<LuggageInfo> luggageInfos) {
		List<Resource<LuggageInfo>> list = new ArrayList<Resource<LuggageInfo>>();
		for(LuggageInfo li : luggageInfos)
			list.add(HATEOASImplementor.createLuggageInfo(li));
		return list;
	}

	public static Resource<Destination> createDestination(Destination d) {
		// TODO : Dodati linkove, dokumentaciju
		return null;
	}
	
	/**
	 * @param destinations - Lista destinacija za koje se kreiraju linkovi
	 * @return Lista resursa destinacija sa kreiranim linkovima
	 */
	public static List<Resource<Destination>> createDestinationsList( List<Destination> destinations) {
		List<Resource<Destination>> list = new ArrayList<Resource<Destination>>();
		for(Destination d : destinations)
			list.add(HATEOASImplementor.createDestination(d));
		return list;
	}

	public static Resource<FlightConfiguration> createFlightConfiguration(
	        FlightConfiguration fc) {
		// TODO : Dodati linkove, dokumentaciju
		return null;
	}
	
	/**
	 * @param flightConfigurations - Lista konfiguracija za koje se kreiraju linkovi
	 * @return Lista resursa konfiguracija sa kreiranim linkovima
	 */
	public static List<Resource<FlightConfiguration>> createFlightConfigurationsList(List<FlightConfiguration> flightConfigurations) {
		List<Resource<FlightConfiguration>> list = new ArrayList<Resource<FlightConfiguration>>();
		for(FlightConfiguration fc : flightConfigurations)
			list.add(HATEOASImplementor.createFlightConfiguration(fc));
		return list;
	}
	
}
