package in.fourbits.etracker.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import in.fourbits.etracker.dao.EtrackerUserCrudRepository;
import in.fourbits.etracker.entity.EtrackerUser;
import in.fourbits.etracker.entity.Expense;
import in.fourbits.etracker.entity.ExpenseType;
import in.fourbits.etracker.exception.ExpenseException;

@Service(value = "EtrackerServiceHibernate")
@Primary
/*
 * @Primary Indicates that a bean should be given preference when multiple
 * candidates are qualified to autowire a single-valued dependency. If exactly
 * one 'primary' bean exists among the candidates, it will be the autowired
 * value.
 * 
 * @Service(value = "EtrackerServiceHibernate") here EtrackerServiceHibernate is
 * the name given to this service so it can be as the @Qualifier name in
 * EtrackerController
 */
public class EtrackerServiceHibernateImpl implements EtrackerService {

	public static final Logger logger = Logger.getLogger(EtrackerServiceHibernateImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EtrackerUserCrudRepository userRepository;

	/**
	 * Method to save a new Expense. Hibernate transactions are manually handled
	 * here
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String saveExpense(Expense expense, String username) {

		// org.hibernate.HibernateException: Could not obtain
		// transaction-synchronized Session for current thread [When using
		// sessionFactory.getCurrentSession()]
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		List<ExpenseType> typeList = null;
		ExpenseType expenseTypeOnLoad = null;

		// Different flavors of creating query in HIBERNATE
		// typeList = getExpenseTypeWithHSqlCreateQuery(expense, session);
		// typeList = getExpenseTypeWithCreateSqlQuery(expense, session);
		// typeList = getExpenseTypeWithCriteria(expense, session);
		// expense.setExpenseType(typeList.get(0));
		// expenseTypeOnLoad = getExpenseTypewithLoad(expense, session);

		expenseTypeOnLoad = getExpenseTypewithGet(expense, session);

		expense.setExpenseType(expenseTypeOnLoad);
		expense.setEtrackerUser(userRepository.findByUserName(username));
		Long id = (Long) session.save(expense);
		logger.info("Expense saved with Id --> " + id);
		/*
		 * Force this session to flush. Must be called at the end of a unit of
		 * work, before committing the transaction and closing the session
		 * (depending on setFlushMode(FlushMode), Transaction.commit() calls
		 * this method). Flushing is the process of synchronizing the underlying
		 * persistent store with persistable state held in memory.
		 */
		session.flush();
		tx.commit();
		session.close();
		return "success";
	}

	/**
	 * Method to retrieve all expenses for a user
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Expense> getAllExpenses(String username) {
		Session session = sessionFactory.openSession();
		EtrackerUser user = userRepository.findByUserName(username);
		List<Expense> expenseList = (List<Expense>) session
				.createQuery("select exp from Expense exp where userId= :userId")
				.setParameter("userId", username).list();
		session.close();
		return expenseList;
	}

	/**
	 * Method to retrieve and individual expense for a user
	 */
	@Override
	public Expense getExpense(String username, Long id) {

		Session session = sessionFactory.openSession();
		/*
		 * If session.load is used here and since dependent objects are lazily
		 * loaded or a proxy object is returned, when serializing into json
		 * response we get "Could not write JSON document: No serializer found
		 * for class
		 * org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer" error
		 * 
		 * to solve this use session.get() / use @JsonIgnore property for the variable
		 */
		Expense expense = session.get(Expense.class, id);

		// TODO: validate of expense exists or not
		System.out.println(expense.getExpenseDescription());
		logger.info("ExpenseId " + id + " is of type --> " + expense.getExpenseType().getExpenseName());
		System.out.println("@@@@@@@@@@@@2222 response ====> " + expense.toString());
		/*
		 * If fetch=FetchType.LAZY and session is closed before accessing
		 * expense.getExpenseType().getExpenseName() then we get "exception":
		 * "org.hibernate.LazyInitializationException", "message":
		 * "could not initialize proxy - no Session"
		 */
		session.close();
		return expense;
		// return crudRepository.findOne(id);

		// HibernateTemplate hibernateTemplate = new
		// HibernateTemplate(sessionFactory);
		// @SuppressWarnings("unchecked")
		// List<Expense> typeList = (List<Expense>) hibernateTemplate.find("from
		// Expense where expenseId=? and userId=? ",
		// id, userId);
		// if (typeList == null || typeList.isEmpty()) {
		// throw new ExpenseException("Expense does not exist");
		// }
		// return typeList.get(0);
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

	@SuppressWarnings("unchecked")
	private List<ExpenseType> getExpenseTypeWithCriteria(Expense expense, Session session) {
		Criteria cr = session.createCriteria(ExpenseType.class);
		cr.add(Restrictions.eq("expenseTypeId", expense.getExpenseTypeId()));
		return validateExpenseTypeList(cr.list());
	}

	@SuppressWarnings("unchecked")
	private List<ExpenseType> getExpenseTypeWithCreateSqlQuery(Expense expense, Session session) {
		return validateExpenseTypeList(
				session.createSQLQuery("select * from tblexpensetype where expensetypeid=" + expense.getExpenseTypeId())
						.list());
	}

	@SuppressWarnings("unchecked")
	private List<ExpenseType> getExpenseTypeWithHSqlCreateQuery(Expense expense, Session session) {
		return validateExpenseTypeList(
				session.createQuery("select exp from ExpenseType exp where expenseTypeId= :expenseTypeId")
						.setParameter("expenseTypeId", expense.getExpenseTypeId()).list());
	}

	private List<ExpenseType> validateExpenseTypeList(List typeList) {

		if (typeList == null || typeList.isEmpty()) {
			throw new ExpenseException("Invalid Expense type received");
		}
		logger.info("expenseTypeSize --> " + typeList.size());
		return (List<ExpenseType>) typeList;

	}

	/**
	 * Return the persistent instance of the given entity class with the given
	 * identifier, assuming that the instance exists. This method might return a
	 * proxied instance that is initialized on-demand, when a non-identifier
	 * method is accessed.
	 * 
	 * You should not use this method to determine if an instance exists (use
	 * get() instead). Use this only to retrieve an instance that you assume
	 * exists, where non-existence would be an actual error.
	 * 
	 * In our case if there is no record in ExpenseType for the given
	 * expenseTypeId expenseTypeOnLoad object returned will never be null as
	 * load returns an instance of ExpenseType
	 * 
	 * @param expense
	 * @param session
	 * @return
	 */
	private ExpenseType getExpenseTypewithLoad(Expense expense, Session session) {
		ExpenseType expenseTypeOnLoad = session.load(ExpenseType.class, expense.getExpenseTypeId());
		if (expenseTypeOnLoad == null) {
			throw new ExpenseException("Invalid Expense type received");
		}
		return expenseTypeOnLoad;
	}

	/**
	 * Return the persistent instance of the given entity class with the given
	 * identifier, or null if there is no such persistent instance. (If the
	 * instance is already associated with the session, return that instance.
	 * This method never returns an uninitialized instance.)
	 * 
	 * In our case if there is no record in ExpenseType for the given
	 * expenseTypeId expenseTypeOnLoad object returned will be null there by
	 * throwing ExpenseException
	 * 
	 * @param expense
	 * @param session
	 * @return
	 */
	private ExpenseType getExpenseTypewithGet(Expense expense, Session session) {
		ExpenseType expenseTypeOnLoad;
		expenseTypeOnLoad = session.get(ExpenseType.class, expense.getExpenseTypeId());
		if (expenseTypeOnLoad == null) {
			throw new ExpenseException("Invalid Expense type received");
		}
		return expenseTypeOnLoad;
	}

}
