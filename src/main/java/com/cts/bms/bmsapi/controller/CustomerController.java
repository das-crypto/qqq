package com.cts.bms.bmsapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.AdhaarNumberNotFoundException;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.CustomerService;




/*
 * Rest Controller to map all requests coming with customer prefix.
 */
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/customer")
public class CustomerController {
	private static final Logger logger=LogManager.getLogger(CustomerController.class);
	@Autowired
	private CustomerService customerService;
	
	/*
	 * Route to register new a customer.
	 * 
	 * handling post request with route "/signup"
	 */
	@PostMapping("/customer-signup")
	public ResponseEntity<Object> createCustomerAccount(@RequestBody Customer customer){
		logger.info("START");
		try{
			/*
			 * calling service logic to insert customer data in DB.
			 * 
			 * This method will throw application specific exception BMSException on
			 * Unsuccessful DB operation.
			 */
			Customer cust = customerService.createNewUser(customer);
			if(cust==null) {
				logger.error("UserID already exists");
				return CustomJsonResponse.generateResponse("UserID already exists", HttpStatus.CONFLICT, customer);
			}
			
		}catch(AdhaarNumberNotFoundException e) {
			/*
			 * handling application specific exception BMSException and returning custom JSON response 
			 * with message UserID already exists because all other validations
			 * is done earlier.
			 */
			logger.error("Account not found");
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.CONFLICT, customer);
		}
		/*
		 * On successful execution success message is sent along with created customer object in
		 * custom JSON response.
		 */
		logger.info("User successfully created");
		return CustomJsonResponse.generateResponse("User successfully created", HttpStatus.CREATED, customer);
	}
	
}
