package flycat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Logger;

/**
 * @FileName: TimeClient
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/28
 */
public class TimeClient {
	public static void main(String[] args) {
		try {
			new TimeClient().connection(8848, "127.0.0.1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connection(int port, String host) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});
		ChannelFuture f = b.connect(host, port).sync();
		f.channel().closeFuture().sync();
	}

	public static class TimeClientHandler extends ChannelHandlerAdapter{
		private static final Logger log = Logger.getLogger(TimeClientHandler.class.getSimpleName());

		private final ByteBuf firstMessage;


		public TimeClientHandler() {
			byte[] req = "query time order".getBytes();
			this.firstMessage = Unpooled.buffer(req.length);
			firstMessage.writeBytes(req);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush(firstMessage);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "UTF-8");
			System.out.println("Now is :" + body);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			log.warning("");
			ctx.close();
		}
	}
}
