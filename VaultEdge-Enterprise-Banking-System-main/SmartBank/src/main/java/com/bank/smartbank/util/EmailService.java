package com.bank.smartbank.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username:noreply@smartbank.com}")
	private String fromEmail;
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;

	}

	@Async
	public void sendEmail(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			mailSender.send(message);
			System.out.println(" Email sent to: " + to);
		} catch (Exception e) {
			System.out.println("Failed to send email to :" + to);
			e.printStackTrace();
		}
	}

	@Async
	public void sendOtpEmail(String to, String otp) {
		String subject = Constants.EMAIL_SUBJECT_OTP;
		String text = buildOtpEmailBody(otp);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendWelcomeEmail(String to, String name) {
		String subject = Constants.EMAIL_SUBJECT_WELCOME;
		String text = buildWelcomeEmailBody(name);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendTransferConfirmationEmail(String to, String transactionRef, String amount) {
		String subject = Constants.EMAIL_SUBJECT_TRANSFER;
		String text = buildTransferEmailBody(transactionRef, amount);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendLoanApprovalEmail(String to, String loanNumber, String amount) {
		String subject = Constants.EMAIL_SUBJECT_LOAN_APPROVED;
		String text = buildLoanApprovalEmailBody(loanNumber, amount);
		sendEmail(to, subject, text);
	}

	private String buildOtpEmailBody(String otp) {
		return String.format(""" 
				
				seu codigo de de login e :
					%s
					e expira em  %d minutes.
				""", otp, Constants.OTP_VALIDITY_MINUTES);
	}

	private String buildWelcomeEmailBody(String name) {
		return String.format("""
				Caro %s,

				Bem vindo ao  Smart Bank!

				Sua conta foi criada com sucesso. agora voce pode:
				- Criar contas poupanca
				- Fazer transferencias
				- Fazer emprestimos
				- Monitorar seu saldo

				Obrigado por escolher o  Smart Bank.

				
				Smart Bank Team
				""", name);
	}

	private String buildTransferEmailBody(String transactionRef, String amount) {
		return String.format("""
				Caro cliente,

				Sua transferencia for realizada com sucesso.

				COdigo de Referencia: %s
				Valor: ₹%s

				se desconhece essa transacao contacte o time de suporte.

				
				Smart Bank Team
				""", transactionRef, amount);
	}

	private String buildLoanApprovalEmailBody(String loanNumber, String amount) {
		return String.format("""
				Caro Cliente,

				Parabens! se pedido de emprestimo foi aprovado com sucesso.

				numero do pedido: %s
				valor aprovado: ₹%s

				seu pedido de emprestimo sera adicionado a sua conta em 24h.

				
				Smart Bank Team
				""", loanNumber, amount);
	}
}
