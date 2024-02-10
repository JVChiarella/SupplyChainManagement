package com.jvc.scmb.services;

import java.util.Map;

public interface VerificationService {

	Map<String, String> verifyJWT(String token);
	
}
