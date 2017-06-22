package in.fourbits.etracker.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import in.fourbits.etracker.entity.Expense;
import in.fourbits.etracker.exception.ExpenseException;

@RestControllerAdvice
public class EtrackerGlobalException implements ResponseBodyAdvice<Object> {

	public static final Logger logger = Logger.getLogger(EtrackerGlobalException.class);
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ExpenseException.class)
	public ResponseEntity<ExpenseResponse<String>> exception(ExpenseException e) {
		return new ResponseEntity<ExpenseResponse<String>>(
				new ExpenseResponse<String>("", "3000", Arrays.asList(e.getMessage())), HttpStatus.BAD_REQUEST);
	}
	
	//TODO: Handle input validation
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody Map<String, Object>
	    handleConstraintViolationException(ConstraintViolationException ex) {
	 
	    // return the errors to the client in the response body
		return null;
	}

	/**
	 * This method returns true if the methods has @ResponseBody annotation associated with it, false otherwise
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null ||
                returnType.getMethodAnnotation(ResponseBody.class) != null);
	}

	/**
	 *  this overridden method intercepts the responseBody
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if(body instanceof ExpenseResponse) {
			logger.info("##### intercepted responsebody and its an instance of ExpenseResponse");
		}
		return body;
	}

}
