package com.isap.ISAProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.Reservation;

import antlr.debug.Event;

@Service("emailSenderService")
public class EmailSenderService {
	
    public JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
    
    public void send(RegisteredUser user) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("bice10izise@gmail.com");
        mailMessage.setText("https://malipauk.herokuapp.com/users/registered/"+user.getConfirmationToken().getConfirmationToken()+"/confirm-account");
        this.sendEmail(mailMessage);
    }
    
    public void sendInvitation(RegisteredUser receiver, Reservation reservation, RegisteredUser sender) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setSubject("Trip invitation!");
        mailMessage.setFrom("bice10izise@gmail.com");
        String text = "User " + sender.getFirstName() + " " + sender.getLastName() + " (" + sender.getUsername() + ") invited you to a trip.\n";
        text += "You can accept on your profile.";
        mailMessage.setText(text);
        this.sendEmail(mailMessage);
    }
    
    public void sendReservationInfo(RegisteredUser receiver, Reservation reservation) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setSubject("Reservation info!");
        mailMessage.setFrom("bice10izise@gmail.com");
        String text = "You have confirmed the following reservation:\n\n\n" +
        "----------Ticket----------\n" +
        "Airline: " + reservation.getTicket().getSeats().get(0).getFlight().getAirline().getName() + "\n" +
        "From: " + reservation.getTicket().getSeats().get(0).getFlight().getStartDestination().getName() + "\n" + 
        "To: " + reservation.getTicket().getSeats().get(0).getFlight().getFinishDestination().getName() + "\n" +
        "Departue Time: " + reservation.getTicket().getSeats().get(0).getFlight().getDepartureTime() + "\n" +
        "Arrival Time: " + reservation.getTicket().getSeats().get(0).getFlight().getArrivalTime() + "\n" + 
        "Price(euro): " + reservation.getTicket().getPrice() + "\n\n";
        
        if(reservation.getVehicleReservation() != null) {
        	text += "----------Vehicle----------\n" + 
        			"Rent A Car: " + reservation.getVehicleReservation().getVehicle().getBranchOffice().getRentACar().getName() + "\n" +
        			"Begin Date: " + reservation.getVehicleReservation().getBeginDate() + "\n" +
        			"End Date: " + reservation.getVehicleReservation().getEndDate() + "\n" +
        			"Brand: " + reservation.getVehicleReservation().getVehicle().getBrand() + "\n" +
        			"Model: " + reservation.getVehicleReservation().getVehicle().getModel() + "\n" +
        			"Type: " + reservation.getVehicleReservation().getVehicle().getType() + "\n" +
        			"Number Of Seats: " + reservation.getVehicleReservation().getVehicle().getSeatsNumber() + "\n" +
        			"Price(euro): " + reservation.getVehicleReservation().getPrice() + "\n\n";
        }
        
        if(reservation.getRoomReservation() != null){
        	text += "----------Room----------\n" + 
        			"Hotel: " + reservation.getRoomReservation().getRoom().getFloor().getHotel().getName() + "\n" +
        			"Begin Date: " + reservation.getRoomReservation().getBeginDate() + "\n" +
        			"End Date: " + reservation.getRoomReservation().getEndDate() + "\n" +
        			"Room Type: " + reservation.getRoomReservation().getRoom().getRoomType().getName() + "\n" +
        			"Number Of Beds: " + reservation.getRoomReservation().getRoom().getNumberOfBeds() + "\n" +
        			"Price(euro): " + reservation.getRoomReservation().getPrice() + "\n\n";
        }
        
        text+= "-------------------------\n" +
        		"Price(euro): " + reservation.getPrice() + "\n";
       
        mailMessage.setText(text);
        this.sendEmail(mailMessage);
    }
}
