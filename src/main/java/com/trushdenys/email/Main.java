package com.trushdenys.email;

import com.trushdenys.db.PriceDownload;
import com.trushdenys.db.PriceLoadDao;
import com.trushdenys.db.PriceLoadDaoJdbc;
import com.trushdenys.db.dbexceptions.DBSystemException;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

public class Main {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public static void main(String[] args) throws DBSystemException, IOException, MessagingException {

        PriceLoadDao priceLoadDao = new PriceLoadDaoJdbc();
        PriceDownload priceDownload;
        int countTab = priceLoadDao.selectCountTab();
        for (int j = 1; j < countTab + 1; j++){
                priceDownload = priceLoadDao.selectById(j);
                logger.info("Data = " + priceDownload);
                    if (priceDownload.getEmail().startsWith("http")) {
                        ReceiveFileUrl receiveFileUrl = new ReceiveFileUrl();
                        receiveFileUrl.receiveFiles(priceDownload);
                    } else {
                        ReceiveMailMsg receiveMailMsg = new ReceiveMailMsg();
                        receiveMailMsg.receiveMessage(priceDownload);
                    }
        }
    }
}
