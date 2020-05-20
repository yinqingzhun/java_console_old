package com.yqz.console.tech.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
;

public class ReturnValue<T> implements Serializable {

	public int returncode;

	public String message;

	public T result;

	public int getReturncode() {
		return returncode;
	}

	public void setReturncode(int returnCode) {
		this.returncode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public static <T> ReturnValue<T> buildSuccessResult(T obj) {
		ReturnValue<T> rt = new ReturnValue<>();
		rt.setReturncode(0);
		rt.setResult(obj);
		rt.setMessage("");
		return rt;
	}

	public static ReturnValue buildErrorResult(int returnCode, String message) {
		ReturnValue rt = new ReturnValue();
		rt.setReturncode(returnCode);
		rt.setMessage(message);
		return rt;
	}

	public static void main(String[] args) throws JsonProcessingException {

		HashMap<String, Object> map = new HashMap<>();
		map.put("id", 1001);
		Object c = ReturnValue.buildSuccessResult(map);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(c));

		String s = "{\"returnCode\":0,\"message\":\"\",\"result\":[{\"id\":1001},{\"id\":1002}]}";

		try {

			ReturnValue rv = mapper.readValue(s, ReturnValue.class);

			System.out.println(rv);
		} catch (IOException e) {
			e.printStackTrace();

		}

		
	}

}
