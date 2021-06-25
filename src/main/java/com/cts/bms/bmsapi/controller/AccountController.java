package com.cts.bms.bmsapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Transaction;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.AccountService;
import com.cts.bms.bmsapi.service.TransactionService;




@CrossOrigin(origins="*")
@RestController
@RequestMapping("/customer")
public class AccountController {
	private static final Logger logger=LogManager.getLogger(AccountController.class);
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService service;

	@GetMapping("/get-balance/{accountNo}")
	public ResponseEntity<Object> getBalance(@PathVariable long accountNo) {

		Account account = service.checkBalance(accountNo);
		logger.info("START");
		if (account == null) {
			logger.error("ACCOUNT NOT FOUND");
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		logger.info("END");
		return CustomJsonResponse.generateResponse("Account balance", HttpStatus.OK, account.getBalance());
		
	}

	@PostMapping("/deposit/{accountNo}")
	public ResponseEntity<Object> depositToAccount(@PathVariable long accountNo,@RequestBody double amount) {

		Account account = service.checkBalance(accountNo);
		logger.info("START");
		if (account == null) {
			logger.error("ACCOUNT NOT FOUND");
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		logger.info("START");
		if(service.depositIntoAccount(account, amount)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Amount deposited successfully", HttpStatus.OK, amount);
		}
		logger.error("Amount cannot be deposited");
		return CustomJsonResponse.generateResponse("Amount cannot be deposited", HttpStatus.CONFLICT, amount);
	}
	
	@PostMapping("/withdraw/{accountNo}")
	public ResponseEntity<Object> withdrawFromAccount(@PathVariable long accountNo,@RequestBody double amount) {

		Account account = service.checkBalance(accountNo);
		logger.info("START");
		if (account == null) {
			logger.error("Account not found");
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		logger.info("START");
		if(service.withdrawFromAccount(account, amount)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Amount withdrawn successfully", HttpStatus.OK, amount);
		}
		logger.error("Insufficient balance");
		return CustomJsonResponse.generateResponse("Insufficient balance", HttpStatus.CONFLICT, amount);
	}
	
	@PostMapping("/account-transfer/{fromAccountNo}/{toAccountNo}")
	public ResponseEntity<Object> accountToAccountTransfer(@PathVariable long fromAccountNo,@PathVariable long toAccountNo,@RequestBody double amount) {
		try {
			logger.info("START");
			if(service.accountToAccountTransfer(fromAccountNo, toAccountNo, amount)) {
				logger.info("END");
				return CustomJsonResponse.generateResponse("Amount sent successfully", HttpStatus.OK, amount);
			}
		} catch (BmsException e) {
			logger.warn("EXCEPTION OCCURS");
			return CustomJsonResponse.generateResponse(e.getMessage(), HttpStatus.CONFLICT, amount);
		}logger.error("Account not found");
		return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, amount);
	}
	
	@GetMapping("/view-statement/{accountNo}") 
	public ResponseEntity<Object> getAllTransactionsForAccount(@PathVariable long accountNo) {
		
		
		List<Transaction> transactions = transactionService.viewAllTransactions(accountNo);
		logger.info("START");
		if(transactions!=null) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("All transactions", HttpStatus.OK,transactions);
		}
		logger.error("Transactions cannot be fetched");
		return CustomJsonResponse.generateResponse("Transactions cannot be fetched", HttpStatus.CONFLICT,transactions);
	}
}
