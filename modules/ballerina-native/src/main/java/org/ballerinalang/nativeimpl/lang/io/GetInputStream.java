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
package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BFile;
import org.ballerinalang.model.values.BInputStream;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.util.exceptions.BallerinaException;

/**
 * Get the next byte in an inputstream.
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "readByte",
        args = {@Argument(name = "in", type = TypeEnum.FILE)},
        returnType = {@ReturnType(type = TypeEnum.INPUTSTREAM)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "Gets the inputstream from file") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "m",
        value = "The BFile reference") })
@BallerinaAnnotation(annotationName = "Return", attributes = {@Attribute(name = "int",
        value = "The inputstream of file") })
public class GetInputStream extends AbstractNativeFunction {

    @Override public BValue[] execute(Context context) {
        BInputStream result;
        BFile file = (BFile) getArgument(context, 0);
        try {
            StandardFileSystemManager fsm = new StandardFileSystemManager();
            FileObject fileObject = fsm.resolveFile(file.stringValue());
            result = new BInputStream(fileObject.getContent().getInputStream());
        }  catch (FileSystemException e) {
            throw new BallerinaException("Error occurred while getting input stream", e);
        }
        return getBValues(result);
    }
}
