package in.fourbits.etracker.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import in.fourbits.etracker.response.ExpenseTypeDeserializer;

@Entity
@Table(name="tblexpenses")
//@JsonDeserialize(using = ExpenseTypeDeserializer.class)
public class Expense {

	public Expense() {
		
	}

	public Expense(Long expenseId, ExpenseType expenseType, EtrackerUser etrackerUser, String expenseDescription, Float amount,
			Date expenseDate, short isRegular, Long expenseTypeId) {
		super();
		this.expenseId = expenseId;
		this.expenseType = expenseType;
		this.etrackerUser = etrackerUser;
		this.expenseDescription = expenseDescription;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.isRegular = isRegular;
		this.expenseTypeId = expenseTypeId;
	}

	@Id
	@Column(name="expenseid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="expensetypeid")
	private ExpenseType expenseType;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userid")
	private EtrackerUser etrackerUser;
	
	@NotNull
	@NotBlank
	@Column(name="expensedescription")
	private String expenseDescription;
	
	@Column(name="amount")
	private Float amount;
	
	@Column(name="date")
	private Date expenseDate;
	
	@Column(name="isregular")
	private short isRegular;
	
	@Transient
	//@JsonIgnore
	private Long expenseTypeId;

	public Long getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public String getExpenseDescription() {
		return expenseDescription;
	}

	public void setExpenseDescription(String expenseDescription) {
		this.expenseDescription = expenseDescription;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Date getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public short getIsRegular() {
		return isRegular;
	}

	public void setIsRegular(short isRegular) {
		this.isRegular = isRegular;
	}

	public Long getExpenseTypeId() {
		return expenseTypeId;
	}

	public void setExpenseTypeId(Long expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
	}
	
	@Override
	public String toString() {
		return "Expense [expenseId=" + expenseId + ", expenseType=" + expenseType + ", etrackerUser=" + etrackerUser
				+ ", expenseDescription=" + expenseDescription + ", amount=" + amount + ", expenseDate=" + expenseDate
				+ ", isRegular=" + isRegular + ", expenseTypeId=" + expenseTypeId + "]";
	}

	public EtrackerUser getEtrackerUser() {
		return etrackerUser;
	}

	public void setEtrackerUser(EtrackerUser etrackerUser) {
		this.etrackerUser = etrackerUser;
	}
}
