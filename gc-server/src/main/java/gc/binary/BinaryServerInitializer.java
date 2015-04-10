package gc.binary;

import com.google.common.cache.Cache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class BinaryServerInitializer extends ChannelInitializer<Channel> {

    private final Cache<Long, String> dataCache;

    public BinaryServerInitializer(Cache<Long, String> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new BinaryServerHandler(dataCache));
    }
}
