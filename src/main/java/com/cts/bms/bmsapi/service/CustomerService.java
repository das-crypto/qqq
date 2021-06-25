package com.cts.bms.bmsapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.AdhaarNumberNotFoundException;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Customer;



@Service
public class CustomerService {
	private static final Logger logger=LogManager.getLogger( CustomerService.class);
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	AccountDao accountDao;

	public Customer createNewUser(Customer customer) throws AdhaarNumberNotFoundException{
		logger.info("START");
		try {
			if(accountDao.checkAdhaarNo(customer).isEmpty()) {
				logger.error("No accounts found");
				throw new AdhaarNumberNotFoundException("No accounts found");
			}
			customer.setApproved(true);
			logger.info("END");
			return customerDao.addCustomer(customer);
		} catch (BmsException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	
}
