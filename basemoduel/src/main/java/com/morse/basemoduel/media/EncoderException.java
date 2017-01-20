package com.morse.basemoduel.media;

/**
 * Created by Administrator on 2017/1/18.
 */

public class EncoderException extends Exception {

    public EncoderException() {
    }

    public EncoderException(String message) {
        super(message);
    }

    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderException(Throwable cause) {
        super(cause);
    }
}
