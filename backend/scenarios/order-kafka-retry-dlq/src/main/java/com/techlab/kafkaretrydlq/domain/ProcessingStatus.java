package com.techlab.kafkaretrydlq.domain;

public enum ProcessingStatus {
    RECEIVED,
    RETRYING,
    PROCESSED,
    DLQ
}
