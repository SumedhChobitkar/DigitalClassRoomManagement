package com.DigitalClassRoomManagement.Exception;

public class FeedbackNotFoundException extends RuntimeException{
    public FeedbackNotFoundException() {super();
    }
    public FeedbackNotFoundException(String message) {super(message);
    }
    public FeedbackNotFoundException(String message, Throwable cause) {super(message, cause);
    }
    public FeedbackNotFoundException(Throwable cause) {super(cause);
    }
}
