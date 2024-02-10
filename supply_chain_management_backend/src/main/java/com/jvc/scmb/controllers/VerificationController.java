package com.jvc.scmb.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.services.VerificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class VerificationController {
	
	private final VerificationService verificationService;

	@GetMapping()
	public ResponseEntity<?> verifyJWT(@RequestHeader (name="Authorization") String token){
		return new ResponseEntity<>(verificationService.verifyJWT(token), HttpStatus.OK);
	}
}
