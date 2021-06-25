package com.cts.bms.bmsapi.dao;

import java.util.List;

import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.DebitCard;
import com.cts.bms.bmsapi.model.FixedDeposit;
import com.cts.bms.bmsapi.model.Loan;

public interface CustomerDao {
	public double getFdInterestRate();

	public List<FixedDeposit> getAllFixedDeposits(Account account) throws BmsException;

	public Customer addCustomer(Customer customer) throws BmsException;

	public Loan applyForLoan(Loan loan) throws BmsException;

	public boolean applyForFd(FixedDeposit fixedDeposit) throws BmsException;

	public Account getActiveAccount(long accountNo) throws BmsException;

	public List<Loan> viewAllApprovedLoans(String userId);

	public boolean applyForDebitCard(DebitCard debitCard) throws BmsException;

	public List<DebitCard> viewAllApprovedDebitCards(Account account) throws BmsException;

	public DebitCard getDebitCard(long cardNo, int cvvNo, int pin);

	public Customer loadCustomer(String username) throws BmsException;
}
