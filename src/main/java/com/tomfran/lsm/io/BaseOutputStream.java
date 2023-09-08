package com.tomfran.lsm.io;

import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

import java.io.FileOutputStream;

public class BaseOutputStream {

    private static final byte[] VBYTE_BUFFER = new byte[10];
    private final FastBufferedOutputStream fos;

    public BaseOutputStream(String filename) {
        try {
            fos = new FastBufferedOutputStream(new FileOutputStream(filename));
            fos.position(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int write(byte[] bytes) {
        try {
            fos.write(bytes);
            return bytes.length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int writeVByteInt(int n) {
        return write(intToVByte(n));
    }

    public int writeVByteLong(long n) {
        return write(longToVByte(n));
    }

    public int writeLong(long n) {
        return write(longToBytes(n));
    }

    byte[] intToVByte(int n) {
        return longToVByte(n);
    }

    private byte[] longToVByte(long n) {
        if (n == 0)
            return new byte[]{0};

        int i = 0;
        while (n > 0) {
            VBYTE_BUFFER[i++] = (byte) (n & 0x7F);
            n >>>= 7;
        }

        VBYTE_BUFFER[i - 1] |= 0x80;
        byte[] res = new byte[i];
        System.arraycopy(VBYTE_BUFFER, 0, res, 0, i);
        return res;
    }


    byte[] longToBytes(long n) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (n & 0xFF);
            n >>>= 8;
        }
        return result;
    }

    public void close() {
        try {
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
