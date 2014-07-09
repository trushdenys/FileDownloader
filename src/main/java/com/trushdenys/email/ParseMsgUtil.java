package com.trushdenys.email;

import java.io.*;

interface ParseMsgUtil {

    public void savefile(String fileName, InputStream is) throws IOException;

    public void moveFile(String destFrom, String destTo, String newFileName);

    public boolean equalsFiles(String oldFile, String newFile) throws IOException;

    public void deleteTheFile(String destFrom);

}
