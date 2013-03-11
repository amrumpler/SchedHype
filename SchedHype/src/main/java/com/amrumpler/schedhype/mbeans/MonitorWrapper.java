package com.amrumpler.schedhype.mbeans;

import java.util.Date;

import lombok.AllArgsConstructor;

import com.jamonapi.Monitor;

@AllArgsConstructor
public class MonitorWrapper implements MonitorWrapperMBean {

    private final Monitor monitor;

    @Override
    public double getTotal() {
        return monitor.getTotal();
    }

    @Override
    public double getAvg() {
        return monitor.getAvg();
    }

    @Override
    public double getMin() {
        return monitor.getMin();
    }

    @Override
    public double getMax() {
        return monitor.getMax();
    }

    @Override
    public double getHits() {
        return monitor.getHits();
    }

    @Override
    public double getStdDev() {
        return monitor.getStdDev();
    }

    @Override
    public Date getFirstAccess() {
        return monitor.getFirstAccess();
    }

    @Override
    public Date getLastAccess() {
        return monitor.getLastAccess();
    }

    @Override
    public double getLastValue() {
        return monitor.getLastValue();
    }

    @Override
    public void reset() {
        monitor.reset();
    }

    @Override
    public boolean isEnabled() {
        return monitor.isEnabled();
    }

    @Override
    public double getActive() {
        return monitor.getActive();
    }

    @Override
    public double getMaxActive() {
        return monitor.getMaxActive();
    }

    @Override
    public double getAvgActive() {
        return monitor.getAvgActive();
    }

    @Override
    public boolean isPrimary() {
        return monitor.isPrimary();
    }

}
