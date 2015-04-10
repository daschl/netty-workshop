package gc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.management.MemoryUsage;

public class ClientHandler extends MessageToByteEncoder<MemoryUsage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MemoryUsage msg, ByteBuf out) throws Exception {
        out.writeLong(msg.getInit()); // initial memory
        out.writeLong(msg.getCommitted()); // committed memory
        out.writeLong(msg.getMax()); // max memory
        out.writeLong(msg.getUsed()); // used memory
    }

}
