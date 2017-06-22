package in.fourbits.etracker.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.fourbits.etracker.entity.Expense;

public class ExpenseResponse<T> extends BaseApiResponse {
	
	@JsonProperty(value="data")
	private T data;
	
	public ExpenseResponse(T data, String code, List<String> messages) {
		super(code, messages);
		this.data = data;
	}

	private T getExpense() {
		return data;
	}

	private void setExpense(T expense) {
		this.data = expense;
	}
	

}
