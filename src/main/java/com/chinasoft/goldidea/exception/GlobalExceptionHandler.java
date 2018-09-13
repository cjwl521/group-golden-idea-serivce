package com.chinasoft.goldidea.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 统一异常处理类
 * 
 * @author Mango
 *
 */
@ControllerAdvice(basePackages = { "group.chinasofti.ekyc" })
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String TIMESTAMP = "timestamp";

	private static final String STATUS = "status";

	private static final String ERROR = "error";

	private static final String EXCEPTION = "exception";

	private static final String MESSAGE = "message";

	/**
	 * 处理 IllegalArgumentException
	 * 
	 * @param e
	 *            IllegalArgumentException
	 * @return 错误信息
	 */
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = IllegalArgumentException.class)
	public Map<String, String> lllegalArgumentExceptionHandler(Exception e) {
		logger.error(e.getMessage(), e);
		Map<String, String> map = new HashMap<>();
		map.put(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		map.put(STATUS, String.valueOf(HttpStatus.BAD_REQUEST));
		map.put(ERROR, "IllegalArgument");
		map.put(EXCEPTION, e.getClass().getName());
		map.put(MESSAGE, e.getMessage());
		return map;
	}

	/**
	 * 处理 HttpMessageConversionException
	 * 
	 * @param e
	 *            HttpMessageConversionException
	 * @return 异常信息
	 */
	@ResponseBody
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HttpMessageConversionException.class)
	public Map<String, String> httpMessageConversionHandler(Exception e) {
		logger.error(e.getMessage(), e);
		Map<String, String> map = new HashMap<>();
		map.put(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		map.put(STATUS, String.valueOf(HttpStatus.BAD_REQUEST));
		map.put(ERROR, "HttpMessageConversion");
		map.put(EXCEPTION, e.getClass().getName());
		map.put(MESSAGE, e.getMessage());
		return map;
	}

	/**
	 * 处理 Exception
	 * 
	 * @param e
	 *            Exception
	 * @return 异常信息
	 */
	@ResponseBody
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public Map<String, String> businessExceptionHandler(Exception e) {
		logger.error(e.getMessage(), e);
		Map<String, String> map = new HashMap<>();
		map.put(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		map.put(STATUS, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		map.put(ERROR, "InternalServerError");
		map.put(EXCEPTION, e.getClass().getName());
		map.put(MESSAGE, "InternalServerError");
		return map;
	}

}
