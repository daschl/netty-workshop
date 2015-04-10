package gc.binary;

import com.google.common.cache.Cache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.TimeUnit;

public class BinaryServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Cache<Long, String> dataCache;

    public BinaryServerHandler(Cache<Long, String> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        long init = msg.readLong();
        long committed = msg.readLong();
        long max = msg.readLong();
        long used = msg.readLong();

        long now = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
        String value = "{\"init\":" + init + ",\"committed\":" + committed + ",\"max\":" + max + ",\"used\": " + used +"}";
        dataCache.put(now, value);
    }

}
