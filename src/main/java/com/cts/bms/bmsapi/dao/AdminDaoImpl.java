package com.cts.bms.bmsapi.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.bmsapi.dto.AccountMapper;
import com.cts.bms.bmsapi.dto.AdminMapper;
import com.cts.bms.bmsapi.dto.CustomerMapper;
import com.cts.bms.bmsapi.dto.DebitCardMapper;
import com.cts.bms.bmsapi.dto.EmployeeMapper;
import com.cts.bms.bmsapi.dto.InterestRateMapper;
import com.cts.bms.bmsapi.dto.LoanMapper;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Admin;
import com.cts.bms.bmsapi.model.Branch;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.DebitCard;
import com.cts.bms.bmsapi.model.Employee;
import com.cts.bms.bmsapi.model.InterestRate;
import com.cts.bms.bmsapi.model.Loan;




@Repository
public class AdminDaoImpl implements AdminDao {
	private static final Logger logger=LogManager.getLogger(AdminDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Admin loadAdmin(String username) {
		logger.info("START");
		String query = "select * from admin where emp_id = ?";
		try {
			Admin admin  = jdbcTemplate.queryForObject(query, new AdminMapper(), new Object[] {
				username	
			});
			logger.info("END");
			return admin;
		}catch(DataAccessException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
	
	@Override
	public Admin addAdmin(Admin admin) throws BmsException{
		logger.info("START");
		String query = "insert into admin (emp_id,password,adhaar_no,phone_number)"
				+ " values(?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { 
					admin.getEmpId(),
					admin.getPassword(),
					admin.getAdhaarNo(),
					admin.getPhoneNo()
			});
			logger.info("END");
			return admin;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Insertion not possible");
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public Employee getEmployee(long empId,long adhaarNo,long phoneNo) throws BmsException {
		logger.info("START");
		String query = "select * from employee where emp_id=? and adhaar_no = ? and phone_number=?";
		try {
			Employee emp = jdbcTemplate.queryForObject(query, new Object[] { 
					empId,
					adhaarNo,
					phoneNo
			},new EmployeeMapper());
			logger.info("END");
			return emp;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Selection not possible");
		}
	}
	
	@Override
	public List<Customer> viewAllCustomer() {
		logger.info("START");
		String sqlQuery = "select * from customer where approved=1 order by fname,lname";
		List<Customer> customers = jdbcTemplate.query(sqlQuery, new CustomerMapper());
		logger.info("END");
		return customers;
	}

	@Override
	public boolean createAccount(Account account) throws BmsException {
		logger.info("START");
		String query = "insert into account (adhaar_no,account_type,active,ifsc_code,phonenumber,introducerAccountNo,nomineeAdhaarNo)"
				+ " values(?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(query,
					new Object[] { account.getAdhaarNumber(), account.getAccType(), account.isActive(),
							account.getIfscCode(), account.getPhoneNumber(), account.getIntroducerAccountNo(),
											account.getNomineeAdhaarNo() });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public boolean enableAccount(long accountNo) throws BmsException {
		logger.info("START");
		String sqlQuery = "update account set active = true where account_no =" + accountNo;
		try {
			jdbcTemplate.update(sqlQuery);
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Updation not possible");
		}
	}

	@Override
	public boolean disableAccount(long accountNo) throws BmsException {
		logger.info("START");
		String sqlQuery = "update account set active = false where account_no =" + accountNo;
		try {
			jdbcTemplate.update(sqlQuery);
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Updation not possible");
		}
	}

	@Override
	public boolean approveLoanRequest(long loanId, boolean isLoanApproved) throws BmsException {
		logger.info("START");
		String sqlQuery = "update loan set approved = ? where loan_id = ? ";

		try {
			jdbcTemplate.update(sqlQuery, new Object[] { isLoanApproved, loanId });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Approve not Possible");
		}
	}

	@Override
	public boolean changeInterestRate(InterestRate irate) throws BmsException {
		logger.info("START");
		String sqlQuery = "update interest_rate set rate = ? where plan=?";

		try {

			jdbcTemplate.update(sqlQuery, new Object[] { irate.getRate(), irate.getPlan() });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Provide appropriate value");
		}

	}

	@Override
	public boolean addBankBranch(Branch branch) throws BmsException {
		logger.info("START");
		String query = "insert into branch (ifsc_code,branch_name,address,pincode,branch_fund)" + " values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { branch.getIfscCode(), branch.getBranchName(), branch.getAddress(),
					branch.getPincode(), branch.getBranchFund() });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public List<Loan> viewAllPendingLoanRequests() {
		logger.info("START");
		String sqlQuery = "select * from loan where approved = 0 order by loan_id desc";
		List<Loan> pendingLoans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		logger.info("END");
		return pendingLoans;
	}

	@Override
	public List<Loan> viewAllApprovedLoans() {
		logger.info("START");
		String sqlQuery = "select * from loan where approved = 1 order by loan_id desc";
		List<Loan> loans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		logger.info("END");
		return loans;

	}

	@Override
	public boolean deleteRejectedLoanRequest(long loanId) throws BmsException {
		logger.info("START");
		String query = "delete from loan where loan_id = " + loanId;
		try {
			jdbcTemplate.update(query);
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Deletion of loan request not possible");
		}
	}

	@Override
	public List<Account> viewAllAccountsOfCustomer(Customer customer) {
		logger.info("START");
		String sqlQuery = "select * from account where adhaar_no = " + customer.getAdhaarNo();
		List<Account> accounts = jdbcTemplate.query(sqlQuery, new AccountMapper());
		logger.info("END");
		return accounts;
	}

	@Override
	public List<InterestRate> viewAllPlans() {
		logger.info("START");
		String query = "select * from interest_rate";
		List<InterestRate> plans = jdbcTemplate.query(query, new InterestRateMapper());
		logger.info("END");
		return plans;
	}

	public List<DebitCard> viewAllPendingDebitCards() throws BmsException {
		logger.info("START");
		String query = "select * from debit_card where approved = " + false + " order by card_no desc";
		try {
			List<DebitCard> pendingCards = jdbcTemplate.query(query, new DebitCardMapper());
			logger.info("END");
			return pendingCards;
		} catch (DataAccessException e) {  
			logger.error(e.getMessage());
			throw new BmsException("Selection failed");
		}
	}

	public boolean approveDebitCard(DebitCard card) throws BmsException {
		logger.info("START");
		String query = "update debit_card set approved = ? where card_no = ?";
		try {
			jdbcTemplate.update(query, new Object[] { true, card.getCardNo() });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Updation failed");
		}
	}

	public boolean deleteRejectedDebitCard(long cardNo) throws BmsException {
		logger.info("START");
		String query = "delete from debit_card where card_no = ?";
		try {
			jdbcTemplate.update(query, new Object[] { cardNo });
			logger.info("END");
			return true;
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			throw new BmsException("Deletion failed");
		}
	}

}
