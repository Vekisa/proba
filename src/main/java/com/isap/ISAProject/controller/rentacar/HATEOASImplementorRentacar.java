package com.isap.ISAProject.controller.rentacar;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.RentACar;
import com.isap.ISAProject.model.rentacar.Vehicle;
import com.isap.ISAProject.model.rentacar.VehicleReservation;

public class HATEOASImplementorRentacar {
	/**
	 * Kreira linkove svih zahteva klase {@code RentACarController}
	 * @param rentacar objekat klase {@code RentACar}
	 * @return linkove svih zahteva definisanih u klasi {@code RentACarController} 
	 */
	public static Resource<RentACar> rentacarLinks(RentACar rentacar){
		Resource<RentACar> resource = new Resource<RentACar>(rentacar);
		resource.add(linkTo(methodOn(RentACarController.class).getRentACarById(rentacar.getId())).withRel("self"));
		resource.add(linkTo(methodOn(RentACarController.class).getAllRentACars(null)).slash("?page=0&size=5").withRel("all_rentacars"));
		resource.add(linkTo(methodOn(RentACarController.class).getBranchOfficesForRentACarWithId(rentacar.getId())).withRel("branch_offices"));
		return resource;
	}
	
	/**
	 * Kreira linkove svih zahteva za sve objekte iz prosleđene liste po HATEOAS principu
	 * @param rentacars lista {@code RentACar} objekata za koje se kreiraju linkovi svih zahteva
	 * @return listu resursa sa svim linkova, za sve {@code RentACar} objekte
	 */
	public static List<Resource<RentACar>> rentacarLinksList(List<RentACar> rentacars){
		List<Resource<RentACar>> list = new ArrayList<Resource<RentACar>>();
		for(RentACar rac : rentacars) {
			list.add(HATEOASImplementorRentacar.rentacarLinks(rac));
		}
		return list;
	}
	
	/**
	 * Kreira linkove svih zahteva klase {@code BranchOfficeController}
	 * @param branch objekat klase {@code BranchOffice}
	 * @return linkove svih zahteva definisanih u klasi {@code BranchOfficeController} 
	 */
	public static Resource<BranchOffice> branchOfficeLinks(BranchOffice branch){
		Resource<BranchOffice> resource = new Resource<BranchOffice>(branch);
		resource.add(linkTo(methodOn(BranchOfficeController.class).getBranchOfficeById(branch.getId())).withRel("self"));
		resource.add(linkTo(methodOn(BranchOfficeController.class).getAllBranchOffices(null)).slash("?page=0&size=5").withRel("all_branch_offices"));
		resource.add(linkTo(methodOn(BranchOfficeController.class).getVehiclesForBranchOfficeWithId(branch.getId())).withRel("vehicles"));
		resource.add(linkTo(methodOn(BranchOfficeController.class).getLocationOfBranchOffice(branch.getId())).withRel("location"));
		return resource;
	}
	
	/**
	 * Kreira linkove svih zahteva za sve objekte iz prosleđene liste po HATEOAS principu
	 * @param branches lista {@code BranchOffice} objekata za koje se kreiraju linkovi svih zahteva
	 * @return listu resursa sa svim linkova, za sve {@code BranchOffice} objekte
	 */
	public static List<Resource<BranchOffice>> branchOfficeLinksList(List<BranchOffice> branches){
		List<Resource<BranchOffice>> list = new ArrayList<>();
		for(BranchOffice bo : branches) {
			list.add(HATEOASImplementorRentacar.branchOfficeLinks(bo));
		}
		return list;
	}
	
	/**
	 * Kreira linkove svih zahteva klase {@code VehicleController}
	 * @param branch objekat klase {@code Vehicle}
	 * @return linkove svih zahteva definisanih u klasi {@code Vehicle} 
	 */
	public static Resource<Vehicle> vehicleLinks(Vehicle vehicle){
		Resource<Vehicle> resource = new Resource<Vehicle>(vehicle);
		resource.add(linkTo(methodOn(VehicleController.class).getAllVehicles(null)).slash("?page=0&size=5").withRel("all-vehicles"));
		resource.add(linkTo(methodOn(VehicleController.class).getVehicleById(vehicle.getId())).withRel("self"));
		resource.add(linkTo(methodOn(VehicleController.class).getVehicleReservationsForVehicleWithId(vehicle.getId())).withRel("vehicle-reservations"));
		return resource;
	}
	
	/**
	 * Kreira linkove svih zahteva za sve objekte iz prosleđene liste po HATEOAS principu
	 * @param vehicles lista {@code Vehicle} objekata za koje se kreiraju linkovi svih zahteva
	 * @return listu resursa sa svim linkova, za sve {@code Vehicle} objekte
	 */
	public static List<Resource<Vehicle>> vehicleLinksList(List<Vehicle> vehicles){
		List<Resource<Vehicle>> list = new ArrayList<>();
		for(Vehicle v : vehicles) {
			list.add(HATEOASImplementorRentacar.vehicleLinks(v));
		}
		return list;
	}
	
	/**
	 * Kreira linkove svih zahteva klase {@code VehicleReservationController}
	 * @param vehicleRes objekat klase {@code VehicleReservation}
	 * @return linkove svih zahteva definisanih u klasi {@code VehicleReservation} 
	 */
	public static Resource<VehicleReservation> vehicleReservationLinks(VehicleReservation vehicleRes){
		Resource<VehicleReservation> resource = new Resource<VehicleReservation>(vehicleRes);
		resource.add(linkTo(methodOn(VehicleReservationController.class).getAllVehicleReservations(null)).slash("?page=0&size=5").withRel("all-vehicle-reservations"));
		resource.add(linkTo(methodOn(VehicleReservationController.class).getVehicleReservationById(vehicleRes.getId())).withRel("self"));
		return resource;
	}
	
	/**
	 * Kreira linkove svih zahteva za sve objekte iz prosleđene liste po HATEOAS principu
	 * @param vehicleReses lista {@code VehicleReservation} objekata za koje se kreiraju linkovi svih zahteva
	 * @return listu resursa sa svim linkova, za sve {@code VehicleReservation} objekte
	 */
	public static List<Resource<VehicleReservation>> vehicleReservationLinksList(List<VehicleReservation> vehicleReses){
		List<Resource<VehicleReservation>> list = new ArrayList<>();
		for(VehicleReservation vr : vehicleReses) {
			list.add(HATEOASImplementorRentacar.vehicleReservationLinks(vr));
		}
		return list;
	}
}
