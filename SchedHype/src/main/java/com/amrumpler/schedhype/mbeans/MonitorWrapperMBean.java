package com.amrumpler.schedhype.mbeans;

import java.util.Date;

public interface MonitorWrapperMBean {
	 double getTotal();

	    double getAvg();

	    double getMin();

	    double getMax();

	    double getHits();

	    double getStdDev();

	    Date getFirstAccess();

	    Date getLastAccess();

	    double getLastValue();

	    void reset();

	    boolean isEnabled();

	    double getActive();

	    double getMaxActive();

	    double getAvgActive();

	    boolean isPrimary();
}
