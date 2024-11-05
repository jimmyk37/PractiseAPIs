package sample.base;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ApiLoggingUtil {
    private static final ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

    private static RequestLoggingFilter requestLoggingFilter;
    private static ResponseLoggingFilter responseLoggingFilter;

    public static void configureLogging() {
        if (requestLoggingFilter == null || responseLoggingFilter == null) {
            requestLoggingFilter = new RequestLoggingFilter(new PrintStream(requestOutputStream));
            responseLoggingFilter = new ResponseLoggingFilter(new PrintStream(responseOutputStream));
        }

        clearFilters(); // Clear existing filters before adding new ones
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
    }

    private static void clearFilters() {
        List<Filter> currentFilters = new ArrayList<>(RestAssured.filters());
        currentFilters.removeIf(filter -> filter instanceof RequestLoggingFilter || filter instanceof ResponseLoggingFilter);
        RestAssured.replaceFiltersWith(currentFilters);
    }

    public static String getRequestLog() {
        return requestOutputStream.toString();
    }

    public static String getResponseLog() {
        return responseOutputStream.toString();
    }

    public static void clearLogs() {
        requestOutputStream.reset();
        responseOutputStream.reset();
    }
}
