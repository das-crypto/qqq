package com.cts.bms.bmsapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.IntroducerNotFoundException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Admin;
import com.cts.bms.bmsapi.model.Branch;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.Loan;
import com.cts.bms.bmsapi.model.Transaction;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.AccountService;
import com.cts.bms.bmsapi.service.AdminService;
import com.cts.bms.bmsapi.service.LoanService;
import com.cts.bms.bmsapi.service.TransactionService;




@CrossOrigin(origins="*")
@RestController
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger=LogManager.getLogger( AdminController.class);
	
	@Autowired
	AdminService service;
	
	@Autowired
	LoanService loanService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService accountService;
	
	
	@PostMapping("/create-new-admin")
	public ResponseEntity<Object> createAdmin(@RequestBody Admin admin) {
		logger.info("START");
		if(service.createNewAdmin(admin)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Admin successfully signed up", HttpStatus.OK,admin);
		}
		logger.error("No Employee found");
		return CustomJsonResponse.generateResponse("No Employee found", HttpStatus.NOT_FOUND,admin);
	}
	@GetMapping("/customer-list") 
	public ResponseEntity<Object> getAllCustomers() {
		logger.info("START");
		List<Customer> customerList = service.getAllCustomers();
		logger.info("END");
		return CustomJsonResponse.generateResponse("All approved customers", HttpStatus.OK,customerList);
	}
	
	@PostMapping("/add-new-branch")
	public ResponseEntity<Object> addNewBranch(@RequestBody Branch branch ) {
		logger.info("START");
		if(service.addBankBranch(branch)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Branch creation Successfull", HttpStatus.CREATED, branch);
		}   logger.error("Branch creation Failed");
			return CustomJsonResponse.generateResponse("Branch creation Failed", HttpStatus.EXPECTATION_FAILED, branch);
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<Object> createNewAccount(@RequestBody Account account ) {
		boolean flag;
		logger.info("START");
		try {
			flag = service.createNewAccount(account);
			if(flag) {
				logger.info("END");
				return CustomJsonResponse.generateResponse("Account created", HttpStatus.OK, account);
			}
			logger.error("Account creation unsuccessfull");
			return CustomJsonResponse.generateResponse("Account creation unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
		} catch (IntroducerNotFoundException e) {
			logger.error("Introducer not found");
			return CustomJsonResponse.generateResponse("Introducer not found", HttpStatus.NOT_FOUND, account);
		}
		
		
	}
	
	@PatchMapping("/disable-account")
	public ResponseEntity<Object> disableAccount(@RequestBody Account account) {
		logger.info("START");
		if(service.disableAccount(account)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Account disabled successfully", HttpStatus.OK, account);
		}
		logger.info("ACCOUNT DISABLE UNSUCCESSFULL");
		return CustomJsonResponse.generateResponse("Account disable unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
	}
	
	@PatchMapping("/enable-account")
	public ResponseEntity<Object> enableAccount(@RequestBody Account account) {
		logger.info("START");
		if(service.enableAccount(account)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Account enabled successfully", HttpStatus.OK, account);
		}
		logger.error("Account enable unsuccessfull");
		return CustomJsonResponse.generateResponse("Account enable unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
	}
	 
	@PatchMapping("/grant-loan") 
	public ResponseEntity<Object> grantLoan(@RequestBody Loan loan) {
		logger.info("START");
		if(loanService.grantLoan(loan)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Loan successfully granted", HttpStatus.OK, loan);
		} logger.error("Loan cannot be granted");
		return CustomJsonResponse.generateResponse("Loan cannot be granted", HttpStatus.EXPECTATION_FAILED, loan);
	}
	
	@DeleteMapping("/reject-loan/{loanId}") 
	public ResponseEntity<Object> rejectLoan(@PathVariable long loanId) {
		logger.info("START");
		if(service.deleteLoanRequest(loanId)) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Loan successfully rejected", HttpStatus.OK, loanId);
		}
		logger.error("Loan cannot be rejected");
		return CustomJsonResponse.generateResponse("Loan cannot be rejected", HttpStatus.EXPECTATION_FAILED, loanId);
	}
	
	@GetMapping("/pending-loans") 
	 
	public ResponseEntity<Object> getAllPendingLoanRequest() {
		logger.info("START");
		List<Loan> pendingLoans = service.getAllPendingLoanRequests();
		logger.info("END");
		return CustomJsonResponse.generateResponse("All pending loan requests", HttpStatus.OK,pendingLoans);
	}
	
	@GetMapping("/all-approved-loans") 
	public ResponseEntity<Object> getAllApprovedLoans() {
		logger.info("START");
		List<Loan> loans = loanService.getAllApprovedLoans();
		logger.info("END");
		return CustomJsonResponse.generateResponse("All approved loans", HttpStatus.OK,loans);
	}
	
	@GetMapping("/all-transactions") 
	public ResponseEntity<Object> getAllTransactions() {
		logger.info("START");
		List<Transaction> transactions = transactionService.viewAllTransactions();
		if(transactions!=null) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("All transactions", HttpStatus.OK,transactions);
		}
		logger.error("Transactions cannot be fetched");
		return CustomJsonResponse.generateResponse("Transactions cannot be fetched", HttpStatus.CONFLICT,transactions);
	}
	
	@GetMapping("/view-account/{accountNo}") 
	public ResponseEntity<Object> viewAccountWithAccountNo(@PathVariable long accountNo) {
		logger.info("START");
		Account account = accountService.checkBalance(accountNo);
		if(account!=null) {
			logger.info("END");
			return CustomJsonResponse.generateResponse("Account details fetched", HttpStatus.OK,account);
		}
		logger.warn("Account doesnot exists");
		return CustomJsonResponse.generateResponse("Account doesnot exists", HttpStatus.OK,null);
	}
}
