
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

package com.richstonedt.garnet.model.message;

import io.swagger.annotations.ApiModel;

/**
 * <b><code>GarnetErrorResponseMessage</code></b>
 * <p/>
 * class_comment
 * <p/>
 * <b>Creation Time:</b> 2016/10/31 20:29.
 *
 * @author xiedongmei
 * @version $ {Revision} 2016/10/31
 * @since torinosrc-commons 0.0.1
 */
@ApiModel
public class GarnetErrorResponseMessage {

    private String errorResponseMessage;

    public GarnetErrorResponseMessage(){
        super();
    }

    public GarnetErrorResponseMessage(String errorResponseMessage){
        super();
        this.errorResponseMessage = errorResponseMessage;
    }

    public String getErrorResponseMessage() {
        return errorResponseMessage;
    }

    public void setErrorResponseMessage(String errorResponseMessage) {
        this.errorResponseMessage = errorResponseMessage;
    }

}
