package gc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Client {

    private static final int BINARY_PORT = 9090;
    private static MemoryMXBean MEM_MX_BEAN = ManagementFactory.getMemoryMXBean();

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler())
                .remoteAddress("127.0.0.1", BINARY_PORT);

            Channel channel = bootstrap.connect().sync().channel();

            while(true) {
                channel.writeAndFlush(memoryUsage());
                Thread.sleep(1000);
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static MemoryUsage memoryUsage() {
        return MEM_MX_BEAN.getHeapMemoryUsage();
    }
}
