package com.cts.bms.bmsapi.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.dao.AdminDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.FixedDeposit;
import com.cts.bms.bmsapi.model.InterestRate;
import com.cts.bms.bmsapi.util.FixedDepositUtil;




@Service
public class PlanService {
	private static final Logger logger=LogManager.getLogger(PlanService.class);
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	AccountDao accountDao;
	
	@Autowired
	CustomerDao custDao;
	
	public List<InterestRate> viewAllPlans() {
		logger.info("View Deatils");
		return adminDao.viewAllPlans();
	}
	
	public boolean generateFixedDeposit(FixedDeposit fd) {
		logger.info("START");
		fd.setInterest(custDao.getFdInterestRate());
		double maturedAmount = FixedDepositUtil.getMaturedAmount(fd.getAmount(), fd.getInterest(), fd.getTenure());
		fd.setMatured_amount(maturedAmount);
		try {
			custDao.applyForFd(fd);
			logger.info("END");
			return true;
		} catch (BmsException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public List<FixedDeposit> getAllFixedDeposits(long accountNo) {
		logger.info("START");
		Account account = accountDao.getAccountWithAccountNumber(accountNo);
		if(account!=null) {
			try {
				logger.info("END");
				return custDao.getAllFixedDeposits(account);
			} catch (BmsException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
		logger.info("BAD-REQUEST");
		return null;
	}
}
