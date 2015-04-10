package gc.http;

import com.google.common.cache.Cache;
import com.google.common.io.Resources;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import java.util.Map;


public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Cache<Long, String> dataCache;

    public HttpServerHandler(Cache<Long, String> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (msg.getUri().equals("/")) {
            serveStatic(ctx, "/index.html");
        } else if (msg.getUri().equals("/data")) {
            serveData(ctx);
        } else {
           serveStatic(ctx, msg.getUri());
        }
    }

    private void serveData(ChannelHandlerContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int num = 0;
        for (Map.Entry<Long, String> entry : dataCache.asMap().entrySet()) {
            sb.append(entry.getValue());
            num++;
            if (num < dataCache.asMap().size()) {
                sb.append(",");
            }
        }
        sb.append("]");

        ByteBuf content = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
    }

    private void serveStatic(ChannelHandlerContext ctx, String path) throws Exception {
        try {
            byte[] raw = Resources.toByteArray(Resources.getResource(path.substring(1)));
            ByteBuf content = Unpooled.wrappedBuffer(raw);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
            ctx.write(response);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            serve404(ctx);
        }

    }

    private void serve404(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, 0);
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
