package com.trushdenys.email;

import com.trushdenys.db.LoadProperties;
import org.apache.log4j.Logger;
import javax.mail.*;
import javax.mail.search.FromStringTerm;
import java.util.*;

import static com.trushdenys.email.ClassNameUtil.getCurrentClassName;

class ConnectionToMail {
    private final String email;
    private final String boxname;

    public ConnectionToMail(String email, String boxname) {
        this.email = email;
        this.boxname = boxname;
    }

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public Message[] getMessagePool() {
        Message[] msg = new Message[0];
        try {
            MyAuthenticator myAuth = new MyAuthenticator(LoadProperties.loadProperties().getProperty("EMAIL"),
                    LoadProperties.loadProperties().getProperty("PASSWORD"));

            Properties props = System.getProperties();
            props.put("mail.user", LoadProperties.loadProperties().getProperty("EMAIL"));
            props.put("mail.host", LoadProperties.loadProperties().getProperty("HOST"));
            props.put("mail.debug", "false");
            props.put("mail.store.protocol", "imaps");
            props.put("mail.transport.protocol", "smtp");

            Session session = Session.getDefaultInstance(props, myAuth);
            /*logger.info("Connection to mail-server with login : " + LoadProperties.loadProperties().getProperty("EMAIL")
            + ", password : " + LoadProperties.loadProperties().getProperty("PASSWORD"));*/
            Store store = session.getStore();
            store.connect();

            Folder[] folders = store.getDefaultFolder().list("*");
            for (Folder folder : folders) {
                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                    if (folder.getFullName().equals(boxname)) {
                        folder.open(Folder.READ_ONLY);
                        msg = folder.search(new FromStringTerm(email));
                        if (msg.length == 0) {
                            logger.info(" Email " + email + " not found on mail-server or in folder " + boxname);
                        }
                    }
                }
            }
        } catch (MessagingException e) {
            logger.error("Can't load messages from mail-server, exception", e);
        }
        return msg;
    }
}

