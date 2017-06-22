package in.fourbits.etracker.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import in.fourbits.etracker.dao.EtrackerCrudRepository;
import in.fourbits.etracker.entity.Expense;
import in.fourbits.etracker.entity.ExpenseType;
import in.fourbits.etracker.exception.ExpenseException;

@Service
public class EtrackerServiceCrudRepoImpl implements EtrackerService {

	public static final Logger logger = Logger.getLogger(EtrackerServiceCrudRepoImpl.class);

	@Autowired
	private EtrackerCrudRepository crudRepository;

	protected HibernateTemplate template = null;

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Method to save a new Expense
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String saveExpense(Expense expense, String username) {

		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		ExpenseType type = (ExpenseType) hibernateTemplate.get(ExpenseType.class, expense.getExpenseTypeId());
		if (type == null) {
			throw new ExpenseException("Invalid Expense type received");
		}
		expense.setExpenseType(type);
		crudRepository.save(expense);
		return "success";
	}

	/**
	 * Method to retrieve all expenses for a user
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Expense> getAllExpenses(String username) {
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		List<Expense> typeList = (List<Expense>) hibernateTemplate.find("from Expense where userId=? ", username);
		if (typeList == null || typeList.isEmpty()) {
			throw new ExpenseException("Data does not exist");
		}
		return typeList;
	}

	/**
	 * Method to retrieve and individual expense for a user
	 */
	@Override
	public Expense getExpense(String username, Long id) {
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		@SuppressWarnings("unchecked")
		List<Expense> typeList = (List<Expense>) hibernateTemplate.find("from Expense where expenseId=? and userId=? ",
				id, username);
		if (typeList == null || typeList.isEmpty()) {
			throw new ExpenseException("Expense does not exist");
		}
		return typeList.get(0);
		// return crudRepository.findOne(id);
	}

	/**
	 * TODO: This method is still under construction Method to get the sum of
	 * all expenses between dates for a user
	 */
	@Override
	public List<Expense> getAmountSpentBetweenDates(String username, Date fromDate, Date toDate) {
		// TODO: Call the getExpenses for UserId using restclient, parse the
		// response and calculate the amount

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Expense> entity = new HttpEntity<Expense>(new Expense());
		ResponseEntity<List<Expense>> responseEntity = restTemplate.exchange(
				"http://localhost:8080/etracker/allExpenses", HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Expense>>() {
				});
		logger.info("@@@@@@@@@@@ retrieved  all expes for user id" + responseEntity.getBody());
		ObjectMapper mapper = new ObjectMapper();
		try {
			// TODO: Use custom deserializer
			// TODO: Explore more restTemplate options
			return mapper.readValue(responseEntity.getBody().toString(),
					TypeFactory.defaultInstance().constructCollectionType(List.class, Expense.class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: handle null gracefully
		return null;
		// Float amount responseEntity.getBody();
	}

	/**
	 * Method to update an existing expense
	 */
	@Override
	public String updateExpense(Expense expense, Long Id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method to delete an expense
	 */
	@Override
	public String deleteExpense(Long Id) {
		// TODO Auto-generated method stub
		return null;
	}
}
