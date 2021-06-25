package com.cts.bms.bmsapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.response.CustomJsonResponse;

@CrossOrigin(origins="*")
@RestController
public class LogoutController {
	private static final Logger logger=LogManager.getLogger(LogoutController.class);
	@GetMapping("/logout")
	public ResponseEntity<Object> logOut(HttpServletRequest req, HttpServletResponse res) {
		logger.info("START");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			new SecurityContextLogoutHandler().logout(req, res, authentication);
			logger.info("END");
			return CustomJsonResponse.generateResponse("Successfully logged out", HttpStatus.OK, null);
		}
		logger.warn("BAD-REQUEST");
		return CustomJsonResponse.generateResponse("Cannot logout. No session found", HttpStatus.BAD_REQUEST, null);
		
	}
}
