package flycat;

import flycat.handler.EchoClientHandler;
import flycat.handler.EchoClientOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @FileName: EchoClient
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/30
 */
public class EchoClient {

	private final String host;

	private final int port;

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}


	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		try {
			b.group(group)
					.channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoClientHandler());
							ch.pipeline().addLast(new EchoClientOutboundHandler());
						}
					});

			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		}finally {
			group.shutdownGracefully().sync();
		}
	}


	public static void main(String[] args) {
		if ( args.length != 2){
			System.err.println("Usage: " + EchoClient.class.getSimpleName()
			+ "<host><port>");
		}
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		try {
			new EchoClient(host, port).start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
