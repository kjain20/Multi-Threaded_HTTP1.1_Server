package edu.upenn.cis.cis455.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to test heavy io ops
 */
public class ComputeUtil {
    final static Logger logger = LogManager.getLogger(ComputeUtil.class);

    public static void factorial(int n) {
        for(int i =0;i<n;i++)
        logger.debug("This is the value:"+logger.getClass());
    }
}
