package org.bizpay.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.bizpay.exception.AppPreException;
import org.springframework.http.ResponseEntity;

public interface AppPreService {
	public  HashMap<String, Object>  login(HttpServletRequest req) throws AppPreException , Exception;
}
