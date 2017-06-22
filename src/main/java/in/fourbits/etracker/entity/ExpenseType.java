package in.fourbits.etracker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tblexpensetype")
public class ExpenseType {
	
	@Id
	@Column(name="expensetypeid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseTypeId;
	
	@Column(name="expensename")
	private String expenseName;
	
	@Column(name="expensetype")
	private String expenseType;	// local/global

	public Long getExpenseTypeId() {
		return expenseTypeId;
	}

	public void setExpenseTypeId(Long expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
	}

	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

}
