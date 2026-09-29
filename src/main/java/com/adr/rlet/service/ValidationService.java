package com.adr.rlet.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

	

	public void validateQuantity(int quantity) {
	    if (quantity <= 0) throw new IllegalArgumentException("bad qty");
	}
	
}
