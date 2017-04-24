package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
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
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "writeInputStream",
        args = {@Argument(name = "inputStream", type = TypeEnum.INPUTSTREAM),
                @Argument(name = "file", type = TypeEnum.FILE)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function acknowledges to the message sender that processing of the file has finished.") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "inputStream",
        value = "Input Stream") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "file",
        value = "Path of the file") })
public class WriteInputStream extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(WriteInputStream.class);
    @Override public BValue[] execute(Context context) {

        OutputStream outputStream = null;
        BFile target = (BFile) getArgument(context, 1);
        BInputStream inputStream = (BInputStream) getArgument(context, 0);
        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            outputStream = targetObj.getContent().getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        } catch (IOException e) {
            throw new BallerinaException("Error while writing file", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return VOID_RETURN;
    }
}
