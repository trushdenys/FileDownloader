package com.trushdenys.email;

import com.trushdenys.com.twmacinta.util.MD5;
import org.apache.log4j.Logger;
import java.io.*;
import java.nio.channels.FileChannel;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

public class ImpParseMsgUtil implements ParseMsgUtil {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public void savefile(String fileName, InputStream is) throws IOException {
        File f = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(f)) {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
                fos.flush();
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't find the file " + fileName + ", exception: ", e);
        } finally {
            is.close();
        }

    }

    public void moveFile(String destFrom, String destTo, String newFileName) {
        try (FileChannel source = new FileInputStream(new File(destFrom)).getChannel();
             FileChannel dest = new FileOutputStream(new File(destTo + newFileName), false).getChannel()) {
            source.transferTo(0, source.size(), dest);
        } catch (FileNotFoundException e) {
            logger.error("Can't find the file " + destFrom + ", exception: ", e);
        } catch (IOException e) {
            logger.error("Some input/output exception: ", e);
        }
        logger.info("Downloaded file from '" + destFrom + "' moved to '" + destTo + "' with name " + newFileName);
        File file = new File(destFrom);
        file.delete();
    }

    public boolean equalsFiles(String oldFile, String newFile) throws IOException {
        Boolean equalsFile = true;
        try {
            File of = new File(oldFile);
            File nf = new File(newFile);
            long rof = of.length();
            long rnf = nf.length();
            if (rof == rnf) {
                return true;
            } else if (of.exists() && nf.exists()) {
                String hashOld = MD5.asHex(MD5.getHash(of));
                String hashNew = MD5.asHex(MD5.getHash(nf));
                equalsFile = (hashOld.equals(hashNew));
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found, exception: ", e);
        }
        return equalsFile;
    }

    public void deleteTheFile(String destFrom){
        File file = new File(destFrom);
        file.delete();
    }

}
