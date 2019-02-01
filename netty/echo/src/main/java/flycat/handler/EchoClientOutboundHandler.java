package flycat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * @FileName: EchoClientOutboundHandler
 * @Description:
 * @Author SuperBoy
 * @Date 2019/2/1
 */
public class EchoClientOutboundHandler
	extends ChannelOutboundHandlerAdapter {


	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("bind");
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		System.out.println("connect");
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("disconnect");
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("close");
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		System.out.println("deregister");
	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		System.out.println("read");
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("write");
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		System.out.println("flush");
	}
}
