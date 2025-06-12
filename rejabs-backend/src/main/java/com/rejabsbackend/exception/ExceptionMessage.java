package com.rejabsbackend.exception;

import java.time.Instant;

public record ExceptionMessage(
        String error,
        Instant timestamp,
        String httpStatusName) {
}
