package io.oconnormi.ddfadm.config

class MalformedDistributionException extends Exception {
    MalformedDistributionException(String message) {
        super(message)
    }
    MalformedDistributionException(String message, Throwable cause) {
        super(message, cause)
    }
}
