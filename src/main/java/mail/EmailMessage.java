package mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailMessage{

	@Autowired
    public JavaMailSender emailSender;
	
	public void sendMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("vmosorinski@gmail.com");
		message.setSubject("ISA");
		message.setText("Cao");
		emailSender.send(message);
	}
	
}
