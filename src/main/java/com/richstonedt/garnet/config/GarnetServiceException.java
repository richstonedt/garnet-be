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

package com.richstonedt.garnet.config;

/**
 * <b><code>GarnetServiceException</code></b>
 * <p/>
 * Garnet Service Exception
 * <p/>
 * <b>Creation Time:</b> 2017年10月28日 下午5:24:39
 *
 * @author chenzechao
 * @since garnet 1.0.0
 */
public class GarnetServiceException extends RuntimeException {

    /**
     * Serialization ID
     *
     * @since garnet 1.0.0
     */
    private static final long serialVersionUID = 3894491214688661572L;

    /**
     * Error code
     *
     * @since garnet 1.0.0
     */
    private String errorCode = GarnetServiceErrorCodes.OTHER;

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param message the message
     * @since garnet 1.0.0
     */
    public GarnetServiceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param message   the message
     * @param errorCode the error code
     * @since garnet 1.0.0
     */
    public GarnetServiceException(String message, String errorCode) {
        super(message);
        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since garnet 1.0.0
     */
    public GarnetServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param message   the message
     * @param cause     the cause
     * @param errorCode the error code
     * @since garnet 1.0.0
     */
    public GarnetServiceException(String message, Throwable cause,
                                  String errorCode) {
        super(message, cause);
        setErrorCode(errorCode);
    }

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param cause the cause
     * @since garnet 1.0.0
     */
    public GarnetServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Garnet service_bk exception.
     *
     * @param cause     the cause
     * @param errorCode the error code
     * @since garnet 1.0.0
     */
    public GarnetServiceException(Throwable cause, String errorCode) {
        super(cause);
        setErrorCode(errorCode);
    }

    /**
     * Returns the errorCode
     *
     * @return the errorCode
     * @since garnet 1.0.0
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the errorCode
     *
     * @param errorCode the error code
     * @since garnet 1.0.0
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
