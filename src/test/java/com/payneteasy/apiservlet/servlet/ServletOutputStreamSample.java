package com.payneteasy.apiservlet.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ServletOutputStreamSample extends ServletOutputStream {

    private final byte[] bytes;
    private       int    position;

    public ServletOutputStreamSample(int aCount) {
        bytes = new byte[aCount];
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        bytes[position++] = (byte) (b & 0xff);
    }

    public byte[] getBytes() {
        byte[] ret = new byte[position];
        System.arraycopy(bytes, 0, ret, 0, position);
        return ret;
    }
}
