package com.mariana.dscatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mariana.dscatalog.dto.EmailDTO;
import com.mariana.dscatalog.entities.PasswordRecover;
import com.mariana.dscatalog.entities.User;
import com.mariana.dscatalog.repositories.PasswordRecoverRepository;
import com.mariana.dscatalog.repositories.UserRepository;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;

@Service
public class AuthService {
	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Autowired
	private EmailService emailService;
	

	public void recoverToken(EmailDTO body) {
		User user = userRepository.findByEmail(body.getEmail());
		if(user == null) {
			throw new ResouceNotFoundException("Email mão encontrado");
		}
		
		String token = UUID.randomUUID().toString();
		
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(body.getEmail());
		entity.setToken(token);
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);
        
       String text = "Acesse o link para definir uma nova senha\n\n"
    		   + recoverUri + token + " Validade de " + tokenMinutes + "minutos";
        
       emailService.sendEmail(body.getEmail(), "Recuperação de senha", text);
	}

}
