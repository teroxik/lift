package com.mj.lift.transfer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides builder for the MultiPacket structure. Make an instance of it, then call
 * ``append`` as many times as you need, following by ``build()`` to get the
 * ``NSData`` that can be sent & decoded on the server.
 */
class MultiPacket {

    private static final Integer BUFFER_SIZE = 1000;

    private Integer count = 0;

    Map<Byte,ByteBuffer> buffer = new HashMap<Byte,ByteBuffer>();

    public void append(Byte location, byte[] data) {
        if(buffer.containsKey(location)){
            buffer.get(location).put(data);
        }
        else {
            ByteBuffer locationBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            locationBuffer.order(ByteOrder.BIG_ENDIAN);
            locationBuffer.put(data);
            count += data.length;
            buffer.put(location,locationBuffer);
        }
    }

    public byte[] data() {
        ByteBuffer result = ByteBuffer.allocate(BUFFER_SIZE);
        result.order(ByteOrder.BIG_ENDIAN);
        byte[] header = {((Integer)0xca).byteValue(), ((Integer)0xb0).byteValue(), count.byteValue()};
        result.put(header);

        for(Byte key : buffer.keySet()) {
            byte[] data = buffer.get(key).array();
            Integer sizel =  (data.length & 0xff00 >> 8);
            Integer sizeh =  (data.length >> 8);
            byte[] dHeader = {sizeh.byteValue(), sizel.byteValue(), ((Integer)data.length).byteValue()};
            result.put(dHeader);
            result.put(data);
        }
        return result.array();
    }
}
