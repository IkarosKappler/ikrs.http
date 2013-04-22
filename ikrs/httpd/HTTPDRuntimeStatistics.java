package ikrs.httpd;

/**
 * @author Ikaros Kappler
 * @date 2013-04-22
 * @version 1.0.0
 **/

public class HTTPDRuntimeStatistics {

    private long systemStartedTime;

    private int reportedErrorCount;

    public HTTPDRuntimeStatistics( long systemStartedTime ) {
	super();
	
	this.systemStartedTime = systemStartedTime;
    }


    public long getSystemStartedTime() {
	return this.systemStartedTime;
    }

    public long getUptime_ms() {
	return System.currentTimeMillis() - this.systemStartedTime;
    }

    public int getReportedErrorCount() {
	return -1;
    }


}