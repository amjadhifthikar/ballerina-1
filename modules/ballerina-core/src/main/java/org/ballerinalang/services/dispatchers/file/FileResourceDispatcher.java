/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.services.dispatchers.file;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.Resource;
import org.ballerinalang.model.Service;
import org.ballerinalang.services.dispatchers.ResourceDispatcher;
import org.ballerinalang.services.dispatchers.filesystem.FileSystemResourceDispatcher;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;

/**
 * Resource level dispatchers handler for file protocol.
 */
public class FileResourceDispatcher implements ResourceDispatcher {
    private static final Logger log = LoggerFactory.getLogger(FileSystemResourceDispatcher.class);

    @Override
    public Resource findResource(Service service, CarbonMessage cMsg, CarbonCallback callback,
                                           Context balContext) throws BallerinaException {
        if (log.isDebugEnabled()) {
            log.debug("Starting to find resource in the file service " + service.getSymbolName().toString() + " to " +
                      "deliver the message");
        }
        Resource resource;
        if (cMsg.getProperty(Constants.FILE_TRANSPORT_EVENT_NAME).equals(Constants.FILE_UPDATE)) {
            resource = getResource(service, Constants.ANNOTATION_NAME_ON_UPDATE);
        } else {
            resource = getResource(service, Constants.ANNOTATION_NAME_ON_ROTATE);
        }
        if (resource == null) {
            throw new BallerinaException("Unable to find resource to dispatch message");
        }
        return resource;
    }

    private Resource getResource(Service service, String annotationName) {
        for (Resource resource : service.getResources()) {
            if (resource.getAnnotation(Constants.PROTOCOL_FILE, annotationName) != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public String getProtocol() {
        return Constants.PROTOCOL_FILE;
    }
}
