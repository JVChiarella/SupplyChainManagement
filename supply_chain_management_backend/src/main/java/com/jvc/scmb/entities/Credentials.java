package com.jvc.scmb.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Credentials {
	
	private String username;
	
	private String password;

}
