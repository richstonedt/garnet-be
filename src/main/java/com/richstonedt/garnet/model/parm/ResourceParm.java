
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

package com.richstonedt.garnet.model.parm;

import com.richstonedt.garnet.model.Resource;

/**
 * The type Resource parm.
 */
public class ResourceParm extends BaseParm{

    private Resource resource;

    private String type;

    private String resourcePathWildCard;

    public String getResourcePathWildCard() {
        return resourcePathWildCard;
    }

    public void setResourcePathWildCard(String resourcePathWildCard) {
        this.resourcePathWildCard = resourcePathWildCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * Gets resource.
     *
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets resource.
     *
     * @param resource the resource
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
