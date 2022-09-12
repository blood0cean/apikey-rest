package com.example.restservice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyController {

	@GetMapping("/token")
	public String getToken(@RequestParam(value = "password") String password) {
		long unixTimestamp = getCurrentUnixTime(ZoneId.of("UTC"));

		String text = password.trim().concat(String.valueOf(unixTimestamp));
		MessageDigest digest = null;
		try {
				digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
		}
		byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(hash);
	}

	private static String bytesToHex(byte[] hash) {
	  StringBuilder hexString = new StringBuilder(2 * hash.length);
	  for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) {
	      hexString.append('0');
	    }
	    hexString.append(hex);
	  }
	  return hexString.toString();
	}
	
	public long getCurrentUnixTime(ZoneId zoneId) {
	    Clock clock = Clock.system(zoneId);
	    return Instant.now(clock).getEpochSecond();
	}

}
