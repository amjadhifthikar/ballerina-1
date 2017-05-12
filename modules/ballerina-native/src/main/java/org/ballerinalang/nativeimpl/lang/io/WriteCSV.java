package org.ballerinalang.nativeimpl.lang.io;

import com.opencsv.CSVWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BArray;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BFile;
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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Copy Function
 */
@BallerinaFunction(
        packageName = "ballerina.lang.io",
        functionName = "writeCSV",
        args = {@Argument(name = "arr", type = TypeEnum.ARRAY, elementType = TypeEnum.STRING),
                @Argument(name = "file", type = TypeEnum.FILE),
                @Argument(name = "charset", type = TypeEnum.STRING),
                @Argument(name = "applyQuotes", type = TypeEnum.BOOLEAN)},
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "This function writes an array to a given locationas a CSV file") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "array",
        value = "String") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "file",
        value = "Path of the file") })
@BallerinaAnnotation(annotationName = "Param", attributes = {@Attribute(name = "encoding",
        value = "Charset to used in writing CSV") })
@BallerinaAnnotation(annotationName = "Param", attributes = { @Attribute(name = "applyQuotes",
        value = "Apply quotes to all data") })
public class WriteCSV extends AbstractNativeFunction {

    private static final Logger log = LoggerFactory.getLogger(WriteCSV.class);
    @Override public BValue[] execute(Context context) {

        CSVWriter csvWriter = null;
        OutputStream outputStream = null;
        BArray<BString> arr = (BArray) getArgument(context, 0);
        BFile target = (BFile) getArgument(context, 1);
        BString charset = (BString) getArgument(context, 2);
        BBoolean applyQuotes = (BBoolean) getArgument(context, 3);
        try {
            FileSystemManager fsManager = VFS.getManager();
            FileObject targetObj = fsManager.resolveFile(target.stringValue());
            outputStream = targetObj.getContent().getOutputStream(true);

            csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, charset.stringValue()));
            csvWriter.writeNext(toStringArray(arr), applyQuotes.booleanValue());

        } catch (FileSystemException e) {
            throw new BallerinaException("Error while resolving file", e);
        } catch (UnsupportedEncodingException e) {
            throw new BallerinaException("Unsupported Encoding", e);
        } finally {
            IOUtils.closeQuietly(csvWriter);
            IOUtils.closeQuietly(outputStream);
        }
        return VOID_RETURN;
    }

    private String[] toStringArray(BArray<BString> arr) {
        String[] stringArray = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            stringArray[i] = arr.get(i).stringValue();
        }
        return stringArray;
    }
}
