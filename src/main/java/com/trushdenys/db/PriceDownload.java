package com.trushdenys.db;

public class PriceDownload {

    private final int id;
    private final String filename;
    private final String email;
    private final String path;
    private final String findname;
    private final String boxname;

    public PriceDownload(int id, String email, String filename, String path, String findname, String boxname) {
        this.id = id;
        this.email = email;
        this.filename = filename;
        this.path = path;
        this.findname = findname;
        this.boxname = boxname;
    }

    public String getBoxname() {
        return boxname;
    }

    public String getFindname() {
        return findname;
    }

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public String getEmail() {
        return email;
    }

    @Override
        public String toString(){
            return "[id = " + id + ", email = " + email + ", filename = " + filename + ", path = " + path + ", findname = " + findname + "]";
        }

}
