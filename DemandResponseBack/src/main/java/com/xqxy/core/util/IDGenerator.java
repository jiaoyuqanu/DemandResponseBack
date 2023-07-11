package com.xqxy.core.util;

public class IDGenerator {
    private final long twepoch = 1546272060000L; // Start time (2019-01-01 00:00:00)
    private final long workIdBits = 4L;
    private final long datacenterIdBits = 4L;
    private final long maxWorkerId = -1L ^ (-1L << workIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceBits = 10L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = workIdBits + sequenceBits;
    private final long timestampShift = datacenterIdBits + workIdBits + sequenceBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long curWorkerIds;
    private long curDatacenterIds;
    private long curSequences = 0L;
    private long lastTimestamp = -1L;

    public IDGenerator(long workerIds, long datacenterIds) {
        if (workerIds > maxWorkerId ) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (workerIds < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterIds > maxDatacenterId ) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        if (datacenterIds < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.curWorkerIds = workerIds;
        this.curDatacenterIds = datacenterIds;
    }

    protected long GenerateTimeMills() {
        return System.currentTimeMillis();
    }

    protected long tillNextMillis(long lastTimestamp) {
        long timestamp = GenerateTimeMills();
        while (timestamp <= lastTimestamp) {
            timestamp = GenerateTimeMills();
        }
        return timestamp;
    }

    public synchronized long getNextId() {
        long timestamp = GenerateTimeMills();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            curSequences = (curSequences + 1) & sequenceMask;
            if (curSequences == 0) {
                timestamp = tillNextMillis(lastTimestamp);
            }
        }
        else {
            curSequences = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampShift) | (curDatacenterIds << datacenterIdShift)
                | (curWorkerIds << workerIdShift) | curSequences;
    }
}
