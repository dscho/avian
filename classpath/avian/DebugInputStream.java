/* Copyright (c) 2008-2013, Avian Contributors

   Permission to use, copy, modify, and/or distribute this software
   for any purpose with or without fee is hereby granted, provided
   that the above copyright notice and this permission notice appear
   in all copies.

   There is NO WARRANTY for this software.  See license.txt for
   details. */

package avian;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class DebugInputStream extends FilterInputStream {
  private StringBuilder builder = new StringBuilder();
  private int offset = 0;
  private PrintStream out = System.out;

  public DebugInputStream(InputStream in) {
    super(in);
  }

  public int read() throws IOException {
    int c = in.read();
    hexdump(c);
    return c;
  }

  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  public int read(byte[] b, int offset, int length) throws IOException {
    int count = in.read(b, offset, length);
    for (int i = 0; i < count; i++) {
      hexdump(b[i + offset]);
    }
    return count;
  }

  public void close() throws IOException {
    in.close();
    while ((offset & 0xf) != 0) {
      hexdump(-1);
    }
    out.flush();
  }

  protected void hexdump(int b) {
    if ((offset & 0xf) == 0) {
      out.print(pad(offset, 8) + " ");
    }
    String hex = b < 0 ? "  " : Integer.toHexString(b & 0xff);
    out.print(" " + (hex.length() == 1 ? "0" : "") + hex);
    if (b >= 0) {
      builder.append(b < 0x20 || b > 0x7f ? '.' : (char)b);
    }
    if ((offset & 0xf) == 0x7) {
      out.print(" ");
    } else if ((offset & 0xf) == 0xf) {
      out.println("  |" + builder + "|");
      builder.setLength(0);
    }
    ++offset;
  }

  private static String pad(long number, int length) {
    return pad(Long.toHexString(number), length, '0');
  }

  private static String pad(String s, int length, char padChar) {
    length -= s.length();
    if (length <= 0) {
      return s;
    }
    StringBuilder builder = new StringBuilder();
    while (length-- > 0) {
      builder.append(padChar);
    }
    return builder.append(s).toString();
  }
}
