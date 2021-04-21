package com.dummy.myerp.business.utils;

import com.dummy.myerp.technical.exception.FunctionalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.validation.ConstraintViolationException;

public class LoggerUtils {

    private static final Logger LOGGER = LogManager.getLogger(LoggerUtils.class);

    public static void logFunctionalException(FunctionalException ex) {

        //Elle log les functionnal ex et les sous ex des func ex
        LOGGER.error(ex.getMessage());
        if (ex.getCause() != null && ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            LOGGER.error(cause.getMessage());
            cause.getConstraintViolations().forEach(cv -> LOGGER.error(cv.getMessage()));
        }
    }

}
