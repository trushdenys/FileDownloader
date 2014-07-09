package com.trushdenys.email;

import com.trushdenys.db.LoadProperties;
import com.trushdenys.db.PriceDownload;
import org.apache.log4j.Logger;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

class ReceiveFileUrl {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public void receiveFiles(PriceDownload priceDownload) throws IOException {
        try {
            URL connection = new URL(priceDownload.getEmail());
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            InputStream in;
            in = urlconn.getInputStream();
            OutputStream writer = new FileOutputStream(LoadProperties.loadProperties().getProperty("PATH")
                    + priceDownload.getFilename());
            byte buffer[] = new byte[4096];
            int c = in.read(buffer);
            while (c > 0) {
                writer.write(buffer, 0, c);
                c = in.read(buffer);
            }
            writer.flush();
            writer.close();
            in.close();
        } catch (IOException e) {
            logger.error("Can't download file from " + priceDownload.getEmail() + ", exception: ", e);
        }
        ParseMsgUtil pMu = new ImpParseMsgUtil();

        if(pMu.equalsFiles(priceDownload.getPath() + priceDownload.getFilename(),
                LoadProperties.loadProperties().getProperty("PATH") + priceDownload.getFilename())) {
            logger.info("Files " + priceDownload.getPath() + priceDownload.getFilename() + " and "
                    + LoadProperties.loadProperties().getProperty("PATH") + priceDownload.getFilename() + " are identical" );
            pMu.deleteTheFile(LoadProperties.loadProperties().getProperty("PATH") + priceDownload.getFilename());
        } else {
                pMu.moveFile(LoadProperties.loadProperties().getProperty("PATH") + priceDownload.getFilename(),
                        priceDownload.getPath(), priceDownload.getFilename());
        }
    }
}
