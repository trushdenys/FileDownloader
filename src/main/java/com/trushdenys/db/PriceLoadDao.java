package com.trushdenys.db;

import com.trushdenys.db.dbexceptions.DBSystemException;

public interface PriceLoadDao{

        public PriceDownload selectById(int id) throws DBSystemException;

        public int selectCountTab() throws DBSystemException;

}
