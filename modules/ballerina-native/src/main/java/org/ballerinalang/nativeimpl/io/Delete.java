package org.ballerinalang.nativeimpl.io;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.io",
        functionName = "delete",
        args = {@Argument(name = "target", type = TypeEnum.STRING)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function acknowledges to the message sender that processing of the file has finished.") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "target",
        value = "File/Directory that should be copied") })
public class Delete extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(Delete.class);
    @Override public BValue[] execute(Context context) {

        BString target = (BString) getArgument(context, 0);

        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            if (targetObj.exists()) {
                boolean isDeleted = targetObj.delete();
                if (!isDeleted) {
                    log.error("Error while deleting file");
                }
            }

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        }
        return VOID_RETURN;
    }
}
