package com.alimsadmin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ==============================================================
 * Unique entity ID generator used by Spring.boot framework
 * ==============================================================
 **/

public class Snowflake {
    private static final long CUST_EPOC = 1293840000000L;
    private static final long WORKER_ID_BITS = 6L;
    private static final long DATACENTER_ID_BITS = 4L;
    private static final int MAX_WORKER_ID = 63;
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_SHIFT = 12L;
    private static final long DATACENTER_ID_SHIFT = 18L;
    private static final long TIMESTAMP_LEFT_SHIFT = 22L;
    private static final long SEQUENCE_MASK = 4095L;
    private static final long EPOC_OFFSET = (10L * 1461L * 24L * 60L * 60L * 1000L);
    private final Logger LOG = LoggerFactory.getLogger(Snowflake.class);
    private long sequence = 0L;

    private long lastTimestamp = -1L;

    public Snowflake() {
    }

    public synchronized long newId() {
        long timestamp = System.currentTimeMillis();
        long id = (timestamp - EPOC_OFFSET);
        if (timestamp < this.lastTimestamp) {
            LOG.error("Clock is moving backwards.  Rejecting requests until: " + this.lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", Long.valueOf(this.lastTimestamp - timestamp)));
        } else {
            if (timestamp > lastTimestamp || (this.sequence & 4096) > 0) this.sequence = 0;
            id <<= 12;
            id |= sequence;
            this.sequence++;
            this.lastTimestamp = timestamp;
            return (id);
        }
    }

    protected long tilNextMillis(long lastRunTimestamp) {
        LOG.info("Waiting for next millisecond. Number of IDs generated for this timestamp exceeded limit.");

        long timestamp;
        for (timestamp = System.currentTimeMillis(); timestamp <= lastRunTimestamp; timestamp = System.currentTimeMillis()) {
        }

        return timestamp;
    }
}
