package com.trushdenys.email;

import com.trushdenys.db.LoadProperties;
import com.trushdenys.db.PriceDownload;
import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

class ReceiveMailMsg {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public void receiveMessage(PriceDownload priceDownload) {
        boolean hasAnAttach;
                ConnectionToMail connectionToMail = new ConnectionToMail(priceDownload.getEmail(), priceDownload.getBoxname());
                Message[] messagePool = connectionToMail.getMessagePool();
                   for (int i = messagePool.length - 1; i >= 0 ; i --) {
                hasAnAttach = parsemessage(messagePool[i], priceDownload.getPath(),
                        priceDownload.getFilename(),
                        priceDownload.getFindname());
                if (hasAnAttach) {
                    break;
                }
            }
    }

    boolean parsemessage(Message message, String path, String filename, String findname) {
        boolean hasAnAttach = false;
        try {
            logger.info(MimeUtility.decodeText(String.valueOf("Message from: " + message.getFrom()[0]))
                   + "----- Date: " + message.getReceivedDate());
            if (!(message.getContent() instanceof String)) {
                Multipart multipart = (Multipart) message.getContent();
                for (int j = 0; j < multipart.getCount(); j++) {
                    BodyPart bodyPart = multipart.getBodyPart(j);
                    if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        if (bodyPart.getContent().getClass().equals(MimeMultipart.class)) {
                            MimeMultipart mimemultipart = (MimeMultipart) bodyPart.getContent();
                            for (int k = 0; k < mimemultipart.getCount(); k++) {
                                if (mimemultipart.getBodyPart(k).getFileName() != null) {
                                    String str = MimeUtility.decodeText(mimemultipart.getBodyPart(k).getFileName());
                                    hasAnAttach = operationsWithFiles(mimemultipart.getBodyPart(k), path, filename, findname, str, hasAnAttach);
                                }
                            }
                        }
                        continue;
                    }
                    if (bodyPart.getFileName() != null) {
                        String str = MimeUtility.decodeText(bodyPart.getFileName());
                        hasAnAttach = operationsWithFiles(bodyPart, path, filename, findname, str, hasAnAttach);
                    }
                }
            } else {
                logger.info("Message not contains an attachment files, go to next message");
            }
        } catch (MessagingException e){
            logger.error("Unable to load bodystructure, exception: " + e);
        } catch (UnsupportedEncodingException e){
            logger.error("Unsupported encoding, exception: " + e);
        } catch (IOException e) {
            logger.error("Read message problems, exception: " + e);
        }
        if (!hasAnAttach) {
            logger.info("Message not contains an attachment files, go to next message");
        }
        return hasAnAttach;
    }

    boolean operationsWithFiles(BodyPart bodyPart, String path, String filename, String findname, String str, boolean hasAnAttach) throws IOException {

        ParseMsgUtil pMu = new ImpParseMsgUtil();
        if (str.contains(findname)) {
            try {
                pMu.savefile(str, bodyPart.getInputStream());
            } catch (IOException | MessagingException e) {
                logger.error("Can't save downloaded file, exception: ", e);
            }
            hasAnAttach = true;
                if ((!(new File(path + filename).exists()))) {
                    pMu.moveFile(LoadProperties.loadProperties().getProperty("PATH") + str, path, filename);
                } else if (pMu.equalsFiles(path + filename, LoadProperties.loadProperties().getProperty("PATH") + str)) {
                    logger.info("Files " + path + filename + " and "
                            + LoadProperties.loadProperties().getProperty("PATH") + str + " are identical" );
                    pMu.deleteTheFile(LoadProperties.loadProperties().getProperty("PATH") + str);
                } else {
                    pMu.moveFile(LoadProperties.loadProperties().getProperty("PATH") + str, path, filename);
                }
        }
        return hasAnAttach;
    }
}
