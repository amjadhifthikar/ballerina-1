package org.ballerinalang.nativeimpl.lang.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "copy",
        args = {@Argument(name = "target", type = TypeEnum.FILE),
                @Argument(name = "destination", type = TypeEnum.FILE)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function acknowledges to the message sender that processing of the file has finished.") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "target",
        value = "File/Directory that should be copied") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "destination",
        value = "The location where the File/Directory should be pasted") })
public class Copy extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(Copy.class);
    @Override public BValue[] execute(Context context) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BFile target = (BFile) getArgument(context, 0);
        BFile destination = (BFile) getArgument(context, 1);

        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            if (targetObj.exists()) {
                inputStream = targetObj.getContent().getInputStream();
                FileObject destinationObj = fsManager.resolveFile(destination.stringValue());
                outputStream = destinationObj.getContent().getOutputStream();
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
            }

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
