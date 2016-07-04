package android.api.com.appimake.aimjsonmodelnetworking;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Nattapongr on 2/29/16 AD.
 */
public class AIMConfig {

    //========= FORMATTING =========

    /**
     * Default date format
     */
    public final static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Default date time format
     */
    public final static String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy mm:HH";

    public final static NumberFormat DEFAULT_FLOATTING_FORMAT = new DecimalFormat("#0.00");
    public final static NumberFormat DEFAULT_INTEGER_FORMAT = new DecimalFormat("#0");

    //========= REQUEST =========

    /**
     * Default request timeout for request data from web services.
     */
    public final static int RequestDefaultTimeout = 60000;


    /**
     * Default request timeout message for web services.
     */
    public final static String RequestTimeoutMessage = "Request Timeout!";
    /**
     * Default data entries for JSON Service using with IJSONEnable Interface
     */
    public static final String RESPONSE_ENTRIES = "entries";

    /**
     * Default server response message for JSON Service using with IJSONEnable Interface
     */
    public static final String RESPONSE_MESSAGE = "message";

    /**
     * Default status code for JSON Service using with IJSONEnable Interface
     */
    public static final String RESPONSE_STATUS = "status";

    /**
     * Set is the web service using milliseconds as time stamp exchange.
     */
    public final static boolean USE_MILLISECONDS_TIMESTAMP_EXCHANGE = false;

    public final static String NETWORK_ERROR = "Network is not available";

    public static String DEFAULT_REPLACE_NULL = "-";
    public static String DEFAULT_SPLIT_STRING = ".;.";
    public static long DAY = 86400;

    public static boolean FOR_TESTING = false;

}

