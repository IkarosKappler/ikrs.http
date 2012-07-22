package ikrs.http;

/**
 * @author Ikaros Kappler
 * @date 2012-07-16
 * @version 1.0.0
 **/

public class Constants {


    public static final String NAME_DEFAULT_LOGGER                               = "DEFAULT_HTTP_LOGGER";



    public static final int HTTP_STATUS_INFORMATIONAL_CONTINUE                   = 100;
    public static final int HTTP_STATUS_INFORMATIONAL_SWITCHING_PROTOCOLS        = 101;

    public static final int HTTP_STATUS_SUCCESSFUL_OK                            = 200;
    public static final int HTTP_STATUS_SUCCESSFUL_CREATED                       = 201;
    public static final int HTTP_STATUS_SUCCESSFUL_ACCEPTES                      = 202;
    public static final int HTTP_STATUS_SUCCESSFUL_NON_AUTHORATIVE_INFORMATION   = 203;
    public static final int HTTP_STATUS_SUCCESSFUL_NO_CONTENT                    = 204;
    public static final int HTTP_STATUS_SUCCESSFUL_RESET_CONTENT                 = 205;
    public static final int HTTP_STATUS_SUCCESSFUL_PARTIAL_CONTENT               = 206;
    
    public static final int HTTP_STATUS_REDIRECION_MULTIPLE_CHOICES              = 300;
    public static final int HTTP_STATUS_REDIRECION_MOVED_PERMANENTLY             = 301;
    public static final int HTTP_STATUS_REDIRECION_FOUND                         = 302;
    public static final int HTTP_STATUS_REDIRECION_SEE_OTHER                     = 303;
    public static final int HTTP_STATUS_REDIRECION_NOT_MODIFIED                  = 304;
    public static final int HTTP_STATUS_REDIRECION_USE_PROXY                     = 305;
    //public static final int HTTP_STATUS_REDIRECION_                            = 306;   // Not in use any more
    public static final int HTTP_STATUS_REDIRECION_TEMPORARY_REDIRECT            = 307;

    public static final int HTTP_STATUS_CLIENTERROR_BAD_REQUEST                  = 400;
    public static final int HTTP_STATUS_CLIENTERROR_UNAUTHORIZED                 = 401;
    public static final int HTTP_STATUS_CLIENTERROR_PAYMENT_REQUIRED             = 402;
    public static final int HTTP_STATUS_CLIENTERROR_FORBIDDEN                    = 403;
    public static final int HTTP_STATUS_CLIENTERROR_NOT_FOUND                    = 404;
    public static final int HTTP_STATUS_CLIENTERROR_METHOD_NOT_ALLOWED           = 405;
    public static final int HTTP_STATUS_CLIENTERROR_NOT_ACCEPTABLE               = 406;
    public static final int HTTP_STATUS_CLIENTERROR_PROXY_AUTHENTICATION_REQUIRED = 407;
    public static final int HTTP_STATUS_CLIENTERROR_REQUEST_TIMED_OUT            = 408;
    public static final int HTTP_STATUS_CLIENTERROR_CONFLICT                     = 409;
    public static final int HTTP_STATUS_CLIENTERROR_GONE                         = 410;
    public static final int HTTP_STATUS_CLIENTERROR_LENGTH_REQUIRED              = 411;
    public static final int HTTP_STATUS_CLIENTERROR_PRECONDITION_FAILED          = 412;
    public static final int HTTP_STATUS_CLIENTERROR_REQUEST_ENTITY_TOO_LARGE     = 413;
    public static final int HTTP_STATUS_CLIENTERROR_REQUEST_URI_TOO_LONG         = 414;
    public static final int HTTP_STATUS_CLIENTERROR_UNSUPPORTED_MEDIA_TYPE       = 415;
    public static final int HTTP_STATUS_CLIENTERROR_REQUEST_RANGE_NOT_SATISFIABLE = 416;
    public static final int HTTP_STATUS_CLIENTERROR_EXPECTATION_FAILED           = 417;

    public static final int HTTP_STATUS_SERVERERROR_INTERNAL_SERVER_ERROR        = 500;
    public static final int HTTP_STATUS_SERVERERROR_NOT_IMPLEMENTED              = 501;
    public static final int HTTP_STATUS_SERVERERROR_BAD_GATEWAY                  = 502;
    public static final int HTTP_STATUS_SERVERERROR_SERVICE_UNAVAILABLE          = 503;
    public static final int HTTP_STATUS_SERVERERROR_GATEWAY_TIMEOUT              = 504;
    public static final int HTTP_STATUS_SERVERERROR_HTTP_VERSION_NOT_SUPPORTED   = 505;


}