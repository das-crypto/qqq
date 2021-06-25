package com.cts.bms.bmsapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.model.Branch;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.BranchService;




@CrossOrigin(origins="*")
@RestController
@RequestMapping("/admin")
public class BranchController {
	
	private static final Logger logger=LogManager.getLogger(BranchController.class);
	@Autowired
	BranchService service;
	
	@PatchMapping("/deposit-to-branch/{ifscCode}") 
	public ResponseEntity<Object> depositToBranch(@PathVariable  String ifscCode, @RequestBody double amount) {
     logger.info("START");
		if(service.depositIntoBranch(ifscCode, amount)) {
			 logger.warn("Amount successfully deposited to branch");
			return CustomJsonResponse.generateResponse("Amount successfully deposited to branch", HttpStatus.OK,null);
		}
		logger.warn("Amount deposit failed");
		return CustomJsonResponse.generateResponse("Amount deposit failed", HttpStatus.CONFLICT,null);		
	}
	
	@GetMapping("/view-all-branches") 
	public ResponseEntity<Object> viewBranches() {
		logger.info("START");
		List<Branch> branches = service.getAllBranches();
		logger.info("END");
		return CustomJsonResponse.generateResponse("All branches", HttpStatus.OK,branches);		
	}
	
	
	
	
}
