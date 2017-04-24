package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BArray;
import org.ballerinalang.model.values.BFile;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "writeCSV",
        args = {@Argument(name = "arr", type = TypeEnum.ARRAY, elementType = TypeEnum.STRING),
                @Argument(name = "file", type = TypeEnum.FILE)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function acknowledges to the message sender that processing of the file has finished.") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "array",
        value = "String") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "file",
        value = "Path of the file") })
public class WriteCSV extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(WriteCSV.class);
    @Override public BValue[] execute(Context context) {
        OutputStream outputStream = null;
        BFile target = (BFile) getArgument(context, 1);

        BArray arr = (BArray) getArgument(context, 0);
        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append(arr.get(0).stringValue());
        for (int i = 1; i != arr.size(); i++) {
            stringBuffer.append(",");
            stringBuffer.append(arr.get(i).stringValue());
        }
        String content = stringBuffer.toString();
        content += "\n";
        InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(content).array());

        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            outputStream = targetObj.getContent().getOutputStream(true);
            IOUtils.copy(is, outputStream);
            outputStream.flush();

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        } catch (IOException e) {
            throw new BallerinaException("Error while writing file", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(outputStream);
        }
        return VOID_RETURN;
    }
}
