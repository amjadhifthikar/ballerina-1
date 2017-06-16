/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.ballerinalang.util.codegen;

import java.util.Objects;

/**
 * {@code StructInfo} contains metadata of a Ballerina struct entry in the program file.
 *
 * @since 0.87
 */
public class StructInfo extends StructureTypeInfo {

    public StructInfo(int pkgPathCPIndex, int connectorNameCPIndex) {
        super(pkgPathCPIndex, connectorNameCPIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgPathCPIndex, nameCPIndex);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StructInfo && pkgPathCPIndex == (((StructInfo) obj).pkgPathCPIndex)
                && nameCPIndex == (((StructInfo) obj).nameCPIndex);
    }
}
