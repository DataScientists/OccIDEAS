package org.occideas.security.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenManager {

	private static final String HMAC_ALGO = "HmacSHA256";
	private static final String SEPARATOR = ".";
	private static final String SEPARATOR_SPLITTER = "\\.";
	private static final String OBJECT_SPLITTER = "@@";

	private static final String SECRET_KEY = "secret.key";

	private Logger logger = Logger.getLogger(TokenManager.class);

	private final Mac hmac;

	public TokenManager() {
		try {
			byte[] secretKey = DatatypeConverter
					.parseBase64Binary("occideas");
			hmac = Mac.getInstance(HMAC_ALGO);
			hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
		} catch (Exception e) {
			logger.error("failed to initialize HMAC Algo" + HMAC_ALGO + ": ", e);
			throw new IllegalStateException();
		}
	}

	public String parseUsernameFromToken(String token) {
		final String[] tokenSection = token.split(OBJECT_SPLITTER);
		final String[] user = tokenSection[0].split(SEPARATOR_SPLITTER);
		if (user.length == 2 && user[0].length() > 0 && user[1].length() > 0) {
			try {
				final byte[] userBytes = fromBase64(user[0]);
				final byte[] hash = fromBase64(user[1]);
				boolean validHash = Arrays.equals(createHmac(userBytes), hash);
				if (validHash) {
					return fromAuthJSON(userBytes);
				}
			} catch (IllegalArgumentException e) {
				logger.error("-- Token attempted to be tampered -- ", e);
			}
		}
		return null;
	}

	public Collection<GrantedAuthority> parseAuthFromToken(String token) {
		final String[] tokenSection = token.split(OBJECT_SPLITTER);
		if (indexExists(tokenSection, 1)) {
			final String[] authorities = tokenSection[1].split(SEPARATOR_SPLITTER);
			return process(authorities);
		}
		return Collections.emptyList();
	}
	
	private Collection<GrantedAuthority> process(String[] authorities){
		if (authorities.length == 2 && authorities[0].length() > 0 && authorities[1].length() > 0) {
			try {
				final byte[] authoritiesBytes = fromBase64(authorities[0]);
				final byte[] hash = fromBase64(authorities[1]);
				boolean validHash = Arrays.equals(createHmac(authoritiesBytes), hash);
				if (validHash) {
					return fromGrantedAuthJSON(authoritiesBytes);
				}
			} catch (IllegalArgumentException e) {
				logger.error("-- Token attempted to be tampered -- ", e);
			}
		}
		return Collections.emptyList();
	}

	public String parseExpiryFromToken(String token) {
		final String[] tokenSection = token.split(OBJECT_SPLITTER);
		if(indexExists(tokenSection, 2)){
			final String[] expiryDate = tokenSection[2].split(SEPARATOR_SPLITTER);
			return processExpiryFromToken(expiryDate);
		}
		return null;
	}
	
	private String processExpiryFromToken(String[] expiryDate){
		if (expiryDate.length == 2 && expiryDate[0].length() > 0 && expiryDate[1].length() > 0) {
			try {
				final byte[] expiryBytes = fromBase64(expiryDate[0]);
				if (Arrays.equals(createHmac(fromBase64(expiryDate[0])), fromBase64(expiryDate[1]))) {
					return (fromExpiryJSON(expiryBytes));
				}
			} catch (final IllegalArgumentException e) {
				logger.error("-- Expiry date attempted to be tampered -- ", e);
			}
		}
		return null;
	}

	public String createTokenForUser(String username, Collection<GrantedAuthority> authorities) {
		StringBuilder sb = new StringBuilder();
		sb.append(convertToHashBytes(username));
		sb.append(OBJECT_SPLITTER);
		sb.append(convertToHashBytes(authorities));
		sb.append(OBJECT_SPLITTER);
		String duration = "480";
		LocalDateTime localDateTime = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime()
				.plusMinutes(Long.valueOf(duration));
		sb.append(convertToHashBytes(localDateTime.toString()));
		return sb.toString();
	}

	private String convertToHashBytes(Object obj) {
		byte[] jsonObj = toJSON(obj);
		byte[] hash = createHmac(jsonObj);
		StringBuilder sb = new StringBuilder();
		sb.append(toBase64(jsonObj));
		sb.append(SEPARATOR);
		sb.append(toBase64(hash));
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private Collection<GrantedAuthority> fromGrantedAuthJSON(byte[] authoritiesBytes) {
		try {
			return new ObjectMapper().readValue(new ByteArrayInputStream(authoritiesBytes), Collection.class);
		} catch (IOException e) {
			logger.error("Failure reading granted auth bytes.", e);
			throw new IllegalStateException(e);
		}
	}

	private String fromAuthJSON(final byte[] userBytes) {
		try {
			return new ObjectMapper().readValue(new ByteArrayInputStream(userBytes), String.class);
		} catch (IOException e) {
			logger.error("Failure reading user bytes.", e);
			throw new IllegalStateException(e);
		}
	}

	private String fromExpiryJSON(final byte[] expiryBytes) {
		try {
			return new ObjectMapper().readValue(new ByteArrayInputStream(expiryBytes), String.class);
		} catch (IOException e) {
			logger.error("Failure reading expiry bytes.", e);
			throw new IllegalStateException(e);
		}
	}

	private byte[] toJSON(Object object) {
		try {
			return new ObjectMapper().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	private String toBase64(byte[] content) {
		return DatatypeConverter.printBase64Binary(content);
	}

	private byte[] fromBase64(String content) {
		return DatatypeConverter.parseBase64Binary(content);
	}

	// synchronized to guard internal hmac object
	private synchronized byte[] createHmac(byte[] content) {
		return hmac.doFinal(content);
	}

	private boolean indexExists(final Object[] obj, int index) {
		return index >= 0 && index < obj.length;
	}

}
