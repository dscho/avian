/* Copyright (c) 2008-2013, Avian Contributors

   Permission to use, copy, modify, and/or distribute this software
   for any purpose with or without fee is hereby granted, provided
   that the above copyright notice and this permission notice appear
   in all copies.

   There is NO WARRANTY for this software.  See license.txt for
   details. */

package avian;

import static avian.Stream.write1;
import static avian.Stream.write2;
import static avian.Stream.write4;

import avian.ConstantPool.PoolEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembler {
  public static final int ACC_PUBLIC       = 1 <<  0;
  public static final int ACC_STATIC       = 1 <<  3;

  public static final int aaload = 0x32;
  public static final int aastore = 0x53;
  public static final int aload = 0x19;
  public static final int aload_0 = 0x2a;
  public static final int aload_1 = 0x2b;
  public static final int astore_0 = 0x4b;
  public static final int anewarray = 0xbd;
  public static final int areturn = 0xb0;
  public static final int dload = 0x18;
  public static final int dreturn = 0xaf;
  public static final int dup = 0x59;
  public static final int fload = 0x17;
  public static final int freturn = 0xae;
  public static final int getfield = 0xb4;
  public static final int goto_ = 0xa7;
  public static final int iload = 0x15;
  public static final int invokeinterface = 0xb9;
  public static final int invokespecial = 0xb7;
  public static final int invokestatic = 0xb8;
  public static final int invokevirtual = 0xb6;
  public static final int ireturn = 0xac;
  public static final int jsr = 0xa8;
  public static final int ldc_w = 0x13;
  public static final int lload = 0x16;
  public static final int lreturn = 0xad;
  public static final int new_ = 0xbb;
  public static final int pop = 0x57;
  public static final int putfield = 0xb5;
  public static final int ret = 0xa9;
  public static final int return_ = 0xb1;

  public static void writeClass(OutputStream out,
                                List<PoolEntry> pool,
                                int name,
                                int super_,
                                int[] interfaces,
                                MethodData[] methods)
    throws IOException
  {
    int codeAttributeName = ConstantPool.addUtf8(pool, "Code");

    // Write the methods' attributes first so that all required constants
    // are in the constant pool before the pool is written out
    Map<MethodData, byte[]> methodAttributes =
      new HashMap<MethodData, byte[]>();
    for (MethodData m: methods) {
      int attributeCount = 1;
      ByteArrayOutputStream out2 = new ByteArrayOutputStream();

      write2(out2, attributeCount);

      write2(out2, codeAttributeName + 1);
      write4(out2, m.code.length);
      out2.write(m.code);

      out2.close();
      byte[] array = out2.toByteArray();
      if (attributeCount != 1) {
        array[1] = (byte)attributeCount;
      }
      methodAttributes.put(m, array);
    }

    write4(out, 0xCAFEBABE);
    write2(out, 0); // minor version
    write2(out, 0); // major version

    write2(out, pool.size() + 1);
    for (PoolEntry e: pool) {
      e.writeTo(out);
    }

    write2(out, ACC_PUBLIC); // flags
    write2(out, name + 1);
    write2(out, super_ + 1);
    
    write2(out, interfaces.length);
    for (int i: interfaces) {
      write2(out, i + 1);
    }

    write2(out, 0); // field count

    write2(out, methods.length);
    for (MethodData m: methods) {
      write2(out, m.flags);
      write2(out, m.nameIndex + 1);
      write2(out, m.specIndex + 1);

      out.write(methodAttributes.get(m));
    }

    write2(out, 0); // attribute count
  }

  public static class MethodData {
    public final int flags;
    public final int nameIndex;
    public final int specIndex;
    public final byte[] code;

    public MethodData(int flags, int nameIndex, int specIndex, byte[] code) {
      this.flags = flags;
      this.nameIndex = nameIndex;
      this.specIndex = specIndex;
      this.code = code;
    }
  }
}
