package com.payneteasy.apiservlet.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class ServletInputStreamSample extends ServletInputStream {

    private byte[] bytes;
    private int position;

    public ServletInputStreamSample(byte[] bytes) {
        this.bytes = bytes;
        position = 0;
    }

    @Override
    public boolean isFinished() {
        return position < bytes.length;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        if(position >= bytes.length) {
            return -1;
        }
        byte ret = bytes[position++];
        return ret;
    }
}
