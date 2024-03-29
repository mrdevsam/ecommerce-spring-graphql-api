package dev.mrdevsam.projects.ecommercespringgraphqlapi.services;

import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.*;

@Service
public class TokenService {

	private final JwtEncoder encoder;

	public TokenService(JwtEncoder encoder) {
		this.encoder = encoder;
	}

	public String generateToken(Authentication auth) {
		Instant now = Instant.now();
		String scope = auth.getAuthorities().stream()
							.map(GrantedAuthority::getAuthority)
							.collect(Collectors.joining(" "));
		JwtClaimsSet claims = JwtClaimsSet.builder()
										.issuer("self")
										.issuedAt(now)
										.expiresAt(now.plus(1, ChronoUnit.HOURS))
										.subject(auth.getName())
										.claim("scope", scope)
										.build();
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
