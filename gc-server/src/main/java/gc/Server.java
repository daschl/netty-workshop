package gc;

import gc.binary.BinaryServerInitializer;
import gc.http.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

    private static final int HTTP_PORT = 8090;
    private static final int BINARY_PORT = 9090;

    public static void main(String... args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap httpBoostrap = new ServerBootstrap()
                .group(eventLoopGroup)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new HttpServerInitializer())
                .channel(NioServerSocketChannel.class);

            ServerBootstrap binaryBootstrap = new ServerBootstrap()
                .group(eventLoopGroup)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new BinaryServerInitializer())
                .channel(NioServerSocketChannel.class);


            Channel httpChannel = httpBoostrap.bind(HTTP_PORT).sync().channel();
            Channel binaryChannel = binaryBootstrap.bind(BINARY_PORT).sync().channel();

            httpChannel.closeFuture().sync();
            binaryChannel.close().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
