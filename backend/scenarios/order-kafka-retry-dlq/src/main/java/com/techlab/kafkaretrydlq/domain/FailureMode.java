package com.techlab.kafkaretrydlq.domain;

public enum FailureMode {
    NONE,
    TRANSIENT_ONCE,
    PERMANENT
}
