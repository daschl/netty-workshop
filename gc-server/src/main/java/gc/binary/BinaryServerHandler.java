package gc.binary;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BinaryServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        long init = msg.readLong();
        long committed = msg.readLong();
        long max = msg.readLong();
        long used = msg.readLong();

        System.out.println("Init: " + init + ", Committed: " + committed + ", Max: " + max + ", Used: " + used);
    }

}
