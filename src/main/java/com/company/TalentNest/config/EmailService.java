package com.company.TalentNest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRejectionEmail(String toEmail, String fullName, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application update - TalentNest");
        message.setText("Dear " + fullName + ",\n\n" +
                "Thank you for applying to the position at " + companyName + ". After careful consideration, " +
                "we regret to inform you that you have not been selected to move forward in the hiring process.\n\n" +
                "We appreciate your interest and wish you all the best in your job search.\n\n" +
                "Best regards,\n" + companyName + " Team");

        mailSender.send(message);
    }

    public void sendShortlistedEmail(String toEmail, String fullName, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application update - TalentNest");
        message.setText("Dear " + fullName + ",\n\n" +
                "Thank you for applying to the position at " + companyName + ". We are pleased to inform you that you have been shortlisted for the next stage of our recruitment process.\n\n" +
                "Your qualifications and experience stood out to our team, and we look forward to learning more about you. " +
                "Our team will be in touch shortly to schedule the next steps, which may include an interview or additional assessment.\n\n" +
                "Thank you again for your interest in joining " + companyName + ".\n\n" +
                "Best regards,\n" + companyName + " Team");

        mailSender.send(message);
    }

    public void sendHiredEmail(String toEmail, String fullName, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Application update - TalentNest");
        message.setText("Dear " + fullName + ",\n\n" +
                "Congratulations! We are pleased to inform you that you have been selected for the position at " + companyName + ".\n\n" +
                "We were very impressed with your background, skills, and the way you presented yourself throughout the hiring process. " +
                "We are excited to welcome you to the team and look forward to the contributions you will make at " + companyName + ".\n\n" +
                "Our HR team will be reaching out to you shortly with the next steps and details about onboarding.\n\n" +
                "Once again, congratulations and welcome aboard!\n\n" +
                "Best regards,\n" + companyName + " Team");

        mailSender.send(message);
    }
}
