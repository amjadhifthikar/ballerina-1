package ballerina.net.file;

annotation FileSource attach service {
    string protocol;
    string fileURI;
    string pollingInterval;
    string fileNamePattern;
    string actionAfterProcess;
    string moveTimestampFormat;
    string moveAfterProcess;
    string createFolder;
}