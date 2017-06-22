package in.fourbits.etracker.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

import in.fourbits.etracker.entity.Expense;

public interface EtrackerService {
	
	// @Secured("ADMIN") TODO: Check why this is not working
	public String saveExpense(Expense expense, String username);
	
	public String updateExpense(Expense expense, Long Id); 
	
	public String deleteExpense(Long Id);
	
	public List<Expense> getAllExpenses(String username); 
	
	public Expense getExpense(String username, Long Id); 
	
	public List<Expense> getAmountSpentBetweenDates(String username, Date fromDate, Date toDate);

}
