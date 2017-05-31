package org.ballerinalang.nativeimpl.lang.files;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.util.exceptions.BallerinaException;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.files",
        functionName = "copy",
        args = {@Argument(name = "source", type = TypeEnum.STRUCT, structType = "File",
                structPackage = "ballerina.lang.files"),
                @Argument(name = "destination", type = TypeEnum.STRUCT, structType = "File",
                        structPackage = "ballerina.lang.files")},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function copies a file from a given location to another") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "source",
        value = "File/Directory that should be copied") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "destination",
        value = "The location where the File/Directory should be pasted") })
public class Copy extends AbstractNativeFunction {

    @Override public BValue[] execute(Context context) {

        OutputStream outputStream = null;
        BStruct source = (BStruct) getArgument(context, 0);
        BStruct destination = (BStruct) getArgument(context, 1);

        try {
            BufferedInputStream inputStream = (BufferedInputStream) source.getNativeData("stream");
            if (inputStream == null) {
                throw new BallerinaException("The file is not opened yet");
            }
            File destinationFile = new File(destination.getValue(0).stringValue());
            File parent = destinationFile.getParentFile();
            if (parent != null) {
                if (!parent.exists()) {
                    if (!parent.mkdirs()) {
                        throw new BallerinaException("Error in writing file");
                    }
                }
            }
            if (destinationFile.exists()) {
                if (!destinationFile.delete()) {
                    throw new BallerinaException("File already Exists");
                }
            }
            if (!destinationFile.createNewFile()) {
                throw new BallerinaException("Error in writing file");
            }
            outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new BallerinaException("Error while copying file");
        } finally {
            closeQuietly(outputStream);
        }
        return VOID_RETURN;
    }

    private void closeQuietly(Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (Exception e) {
            throw new BallerinaException("Exception during Resource.close()", e);
        }
    }
}