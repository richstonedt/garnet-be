/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.richstonedt.garnet.exception;


/**
 * <b><code>GarnetServiceException</code></b>
 * <p/>
 * GarnetServiceException
 * <p/>
 * <b>Creation Time:</b> 2016/10/31 20:29.
 *
 * @author xiedongmei
 * @version $ {Revision} 2016/10/31
 * @since torinosrc-commons 0.0.1
 */
public class GarnetServiceException extends GarnetApplicationException {

    /**
     * The Constant CONFLICT.
     */
    public static final String CONFLICT = "Conflict";

    /**
     * The Constant NOT_FOUND.
     */
    public static final String NOT_FOUND = "NotFound";

    /**
     * Serialization ID
     * @since diamond-service-commons 1.0
     */
    private static final long serialVersionUID = 3894491214688661572L;

    /**
     * Instantiates a new diamond service exception.
     */
    public GarnetServiceException(){}

    /**
     * Instantiates a new diamond service exception.
     *
     * @param message the message
     */
    public GarnetServiceException(String message){
        super(message);
    }

    /**
     * Instantiates a new diamond service exception.
     *
     * @param message   the message
     * @param errorCode the error code
     */
    public GarnetServiceException(String message, String errorCode){
        super(message);
        this.setErrorCode(errorCode);
    }

    /**
     * Instantiates a new diamond service exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public GarnetServiceException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * Instantiates a new Diamond service exception.
     *
     * @param message   the message
     * @param cause     the cause
     * @param errorCode the error code
     */
    public GarnetServiceException(String message, Throwable cause, String errorCode){
        super(message,cause);
        this.setErrorCode(errorCode);
    }

    /**
     * Instantiates a new diamond service exception.
     *
     * @param cause the cause
     */
    public GarnetServiceException(Throwable cause){
        super(cause);
    }

    /**
     * Instantiates a new diamond service exception.
     *
     * @param cause     the cause
     * @param errorCode the error code
     */
    public GarnetServiceException(Throwable cause, String errorCode){
        super(cause);
        this.setErrorCode(errorCode);
    }

}
