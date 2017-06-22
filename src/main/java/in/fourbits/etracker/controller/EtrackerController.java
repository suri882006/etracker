package in.fourbits.etracker.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage.ValidationError;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.fourbits.etracker.entity.Expense;
import in.fourbits.etracker.exception.ExpenseValidationError;
import in.fourbits.etracker.exception.ValidationErrorBuilder;
import in.fourbits.etracker.response.ExpenseResponse;
import in.fourbits.etracker.service.EtrackerService;

@RestController
public class EtrackerController {

	@Autowired
	@Qualifier("EtrackerServiceHibernate")
	private EtrackerService eService;

	/**
	 * API to create a expense
	 * 
	 * @return
	 */
	@RequestMapping(value = "/expense", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ExpenseResponse<String>> addExpense(@Valid @RequestBody Expense expense,
			BindingResult bindingResult, Principal principal) {
		
		System.out.println("######### principal username ====> "+principal.getName());

		if (bindingResult.hasErrors()) {
			// return ResponseEntity.badRequest().body("sadasd");
			return new ResponseEntity<ExpenseResponse<String>>(
					new ExpenseResponse<String>("Required fields are empty", "4000", null), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ExpenseResponse<String>>(
				new ExpenseResponse<String>(eService.saveExpense(expense, principal.getName()), "2000", null), HttpStatus.OK);
	}

	// TODO: Check if/ how to use the below exception handlers

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExpenseValidationError handleException(MethodArgumentNotValidException exception) {
		return createValidationError(exception);
	}

	private ExpenseValidationError createValidationError(MethodArgumentNotValidException e) {
		return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
	}

	/**
	 * API to create a expense
	 * 
	 * @return
	 */
	@RequestMapping(value = "/expense/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ExpenseResponse<Expense>> getExpense(
			@PathVariable("id") Long id, Principal principal) {

		return new ResponseEntity<ExpenseResponse<Expense>>(
				new ExpenseResponse<Expense>(eService.getExpense(principal.getName(), id), "2000", null), HttpStatus.OK);
	}

	/**
	 * TODO: This service is under construction, handle JSON deSerialisation in
	 * EtrackerServiceImpl
	 * 
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@RequestMapping(value = "/expenses/{userid}", method = RequestMethod.GET, produces = "application/json")
	public List<Expense> getAmountSpent(@PathVariable("userid") Long userId,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "fromDate") Date fromDate,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "fromDate") Date toDate, Principal principal) {
		return eService.getAmountSpentBetweenDates(principal.getName(), fromDate, toDate);
	}

	/**
	 * API to get all the expenses for input userId
	 * 
	 * @param userId
	 * @return List of Expense
	 */
	@RequestMapping(value = "/allExpenses", method = RequestMethod.GET, produces = "application/json")
	public List<Expense> getExpenses(Principal principal) {
		return eService.getAllExpenses(principal.getName());
	}

	/**
	 * URL pattern /arraylist Response body with a ArrayList will be rendered as
	 * ["Value1","Value2","Value3"]
	 * 
	 * @return List
	 */
	@RequestMapping(value = "/arraylist", method = RequestMethod.GET)
	public List root() {

		List<String> arrList = new ArrayList<>();
		arrList.add("Value1");
		arrList.add("Value2");
		arrList.add("Value3");
		return arrList;
	}

	/**
	 * URL pattern index - /hashmap Response body with a HashMap will be
	 * rendered as {"key1":"value1","mode":"default index","key2":"value2"}
	 * 
	 * @return HashMap
	 */
	@RequestMapping(value = "/hashmap", method = RequestMethod.GET)
	public HashMap index() {

		HashMap<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("mode", "default index");
		return map;
	}

}
