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

import com.opencsv.CSVReader;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BArray;
import org.ballerinalang.model.values.BReader;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.connectors.jms.client.Send;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Get the next record of a csv file as an array.
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "readCSVRecord",
        args = {@Argument(name = "reader", type = TypeEnum.READER)},
        returnType = {@ReturnType(type = TypeEnum.ARRAY, elementType = TypeEnum.STRING)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "Get the next record of a csv file as an array") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "reader",
        value = "The reader instance to read record from") })
@BallerinaAnnotation(annotationName = "Return", attributes = {@Attribute(name = "line",
        value = "The array containing the record") })
public class ReadCSVRecord extends AbstractNativeFunction {
    private static final Logger log = LoggerFactory.getLogger(Send.class);
    @Override
    public BValue[] execute(Context context) {
        BArray<BString> result = new BArray<>(BString.class);
        try {
        //todo allow custom seperators and quotes.
        BReader reader = (BReader) getArgument(context, 0);
        CSVReader csvReader = new CSVReader(reader);

            String[] myEntries = csvReader.readNext();
            int i = 0;
            for (String record : myEntries) {
                result.add(i++, new BString(record));
            }
        } catch (IOException e) {
            throw new BallerinaException("Unsupported Encoding of Blob", e);
        }

        return getBValues(result);
    }
}
