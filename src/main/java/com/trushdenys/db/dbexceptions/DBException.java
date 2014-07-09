package com.trushdenys.db.dbexceptions;

class DBException extends Exception {

    DBException(Throwable cause){
        super("Can't get connection", cause);
    }

}
