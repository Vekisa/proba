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

@Service("emailSenderService")
public class EmailSenderService {

    private JavaMailSender javaMailSender;

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
        mailMessage.setText("http://localhost:8080/users/registered/"+user.getConfirmationToken().getConfirmationToken()+"/confirm-account");
        this.sendEmail(mailMessage);
    }
    
    public void sendInvitation(RegisteredUser receiver, Reservation reservation, RegisteredUser sender) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setSubject("Trip invitation!");
        mailMessage.setFrom("bice10izise@gmail.com");
        String text = "User " + sender.getFirstName() + " " + sender.getLastName() + " (" + sender.getUsername() + ") invited you to a trip.";
        text += "Link for accepting/declining the invitation " + "";
        mailMessage.setText(text);
        this.sendEmail(mailMessage);
    }
    
    public void sendReservationInfo(RegisteredUser receiver, Reservation reservation) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver.getEmail());
        mailMessage.setSubject("Reservation info!");
        mailMessage.setFrom("bice10izise@gmail.com");
        String text = "You have confirmed the following reservation: ";
        ObjectMapper mapper = new ObjectMapper();
        try {
			text += mapper.writeValueAsString(reservation);
		} catch (JsonProcessingException e) {
			text += reservation.getId();
		}
        mailMessage.setText(text);
        this.sendEmail(mailMessage);
    }
}
