package com.trushdenys.com.twmacinta.util;

import java.io.*;

public class MD5OutputStream extends FilterOutputStream {
    /**
     * MD5 context
     */
    private final MD5	md5;

    /**
     * Creates MD5OutputStream
     * @param out	The output stream
     */

    private MD5OutputStream(OutputStream out) {
	super(out);

	md5 = new MD5();
    }

    /**
     * Writes a byte. 
     *
     * @see java.io.FilterOutputStream
     */

    public void write (int b) throws IOException {
	out.write(b);
	md5.Update((byte) b);
    }

    /**
     * Writes a sub array of bytes.
     *
     * @see java.io.FilterOutputStream
     */

    public void write (byte b[], int off, int len) throws IOException {
	out.write(b, off, len);
	md5.Update(b, off, len);
    }

    /**
     * Returns array of bytes representing hash of the stream as finalized
     * for the current state.
     * @see MD5#Final
     */

    byte[] hash() {
	return md5.Final();
    }

  public MD5 getMD5() {
    return md5;
  }

  /**
   * This method is here for testing purposes only - do not rely
   * on it being here.
   **/
  public static void main(String[] arg) {
    try {
      MD5OutputStream out = new MD5OutputStream(new com.trushdenys.com.twmacinta.io.NullOutputStream());
      InputStream in = new BufferedInputStream(new FileInputStream(arg[0]));
      byte[] buf = new byte[65536];
      int num_read;
      long total_read = 0;
      while ((num_read = in.read(buf)) != -1) {
	total_read += num_read;
	out.write(buf, 0, num_read);
      }
      System.out.println(MD5.asHex(out.hash())+"  "+arg[0]);
      in.close();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}

