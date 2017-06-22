package in.fourbits.etracker.response;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import in.fourbits.etracker.entity.Expense;

public class ExpenseTypeDeserializer extends JsonDeserializer<Expense>  {

	@Override
	public Expense deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = p.getCodec();
	    JsonNode node = oc.readTree(p);
	    
	    final Long expenseId = node.get("expenseId").asLong();
	    final String expenseName = node.get("expenseName").asText();
	    final Long userId = node.get("userId").asLong();
	    final String expenseDescription = node.get("expenseDescription").asText();
	    final float amount = (float)node.get("amount").asDouble();
	    //final Date expenseDate = (Date)node.get("expenseDate").asText();
	    final short isRegular = (short)node.get("isRegular").asInt();
	    
	    return new Expense(expenseId, null, null, expenseDescription, amount,
				null, isRegular, null);
	}

}
