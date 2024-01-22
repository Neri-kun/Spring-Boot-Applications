package com.libraryapp.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorPagesController implements ErrorController {
    private static final Map<Integer, String> ERROR_PAGES = new HashMap<>();

    static {
        ERROR_PAGES.put(HttpStatus.BAD_REQUEST.value(), "error-pages/400-error.html");
        ERROR_PAGES.put(HttpStatus.FORBIDDEN.value(), "error-pages/403-error.html");
        ERROR_PAGES.put(HttpStatus.NOT_FOUND.value(), "error-pages/404-error.html");
        ERROR_PAGES.put(HttpStatus.REQUEST_TIMEOUT.value(), "error-pages/408-error.html");
        ERROR_PAGES.put(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error-pages/500-error.html");
        ERROR_PAGES.put(HttpStatus.BAD_GATEWAY.value(), "error-pages/502-error.html");
        ERROR_PAGES.put(HttpStatus.SERVICE_UNAVAILABLE.value(), "error-pages/503-error.html");
        ERROR_PAGES.put(HttpStatus.GATEWAY_TIMEOUT.value(), "error-pages/504-error.html");
    }

    @RequestMapping(value = "/error")
    public String errorHandler(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            return ERROR_PAGES.getOrDefault(statusCode, "error/general-error-page.html");
            
        }

        return "error/general-error-page.html";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}