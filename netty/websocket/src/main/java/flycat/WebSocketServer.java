package flycat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @FileName: WebSocketServer
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/29
 */
public class WebSocketServer {
	public void run(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline channelPipeline = ch.pipeline();
						channelPipeline.addLast("http-codec", new HttpServerCodec());
						channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
						channelPipeline.addLast("http-chunked", new ChunkedWriteHandler());
						channelPipeline.addLast("handler", new WebSocketServerHandler());

					}
				});
		Channel ch = b.bind(port).sync().channel();
		System.out.println("");
		System.out.println();
		ch.closeFuture().sync();
	}


	public static void main(String[] args) {
		int port = 8080;
		try {
			new WebSocketServer().run(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
