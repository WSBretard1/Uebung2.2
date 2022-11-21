package org.texttechnologylab.project.Uebung2.exceptions;

/**
 * ParamException
 * @author Giuseppe Abrami
 */
public class ParamException extends Exception {

    public ParamException() {
    }

    public ParamException(Throwable pCause) {
        super(pCause);
    }

    public ParamException(String pMessage) {
        super(pMessage);
    }

    public ParamException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

}
