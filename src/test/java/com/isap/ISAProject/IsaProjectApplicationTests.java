package com.isap.ISAProject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.isap.ISAProject.service.EmailSenderService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {EmailSenderService.class, JavaMailSender.class})
public class IsaProjectApplicationTests {

	@Test
	public void contextLoads() {
	}

}
