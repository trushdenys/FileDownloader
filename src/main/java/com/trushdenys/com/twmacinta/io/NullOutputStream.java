package com.trushdenys.com.twmacinta.io;

import java.io.*;

public class NullOutputStream extends OutputStream {

  private boolean closed = false;

  public NullOutputStream() {
  }

  public void close() {
    this.closed = true;
  }

  public void flush() throws IOException {
    if (this.closed) _throwClosed();
  }

  private void _throwClosed() throws IOException {
    throw new IOException("This OutputStream has been closed");
  }

  public void write(byte[] b) throws IOException {
    if (this.closed) _throwClosed();
  }

  public void write(byte[] b, int offset, int len) throws IOException {
    if (this.closed) _throwClosed();
  }

  public void write(int b) throws IOException {
    if (this.closed) _throwClosed();
  }

}
