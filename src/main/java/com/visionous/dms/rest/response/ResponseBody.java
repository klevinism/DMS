/**
 * 
 */
package com.visionous.dms.rest.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author delimeta
 * @param <T>
 *
 */
public class ResponseBody<T> {
	private String message;
	
	private String error;
	
	private List<T> result;
	
	/**
	 * 
	 */
	public ResponseBody() {
		result = new ArrayList<>();
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * @param created
	 */
	public void addResult(T created) {
		this.result.add(created);
	}
	
	@Override
	public String toString() {
		return "ResponseBody [message=" + message + ", error=" + error + ", result=" + result + "]";
	}

}
