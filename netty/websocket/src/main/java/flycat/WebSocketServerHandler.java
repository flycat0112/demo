package flycat;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @FileName: WebSocketSErverHandler
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/29
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler {

	private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getSimpleName());


	private WebSocketServerHandshaker handshaker;



	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest){
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}else if(msg instanceof WebSocketFrame){
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();

	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
		if (!"websocket".equals(req.headers().get("Upgreade"))){
				sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
				return;
		}

		WebSocketServerHandshakerFactory wsFactory =
				new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);

		if (handshaker == null){
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());

		}else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
		if (frame instanceof CloseWebSocketFrame){
			handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
			return;
		}

		if(frame instanceof PingWebSocketFrame){
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}

		if(!(frame instanceof TextWebSocketFrame)){
			throw new UnsupportedOperationException(String.format(""));

		}

		String request = ((TextWebSocketFrame) frame).text();
		if(logger.isLoggable(Level.FINE)){
			logger.fine(String.format(""));
		}
		ctx.channel().write(new TextWebSocketFrame(request + "" + new Date().toString()))
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
		ChannelFuture f = ctx.channel().writeAndFlush(res);

		if(!HttpHeaderUtil.isKeepAlive(req)){
			f.addListener(ChannelFutureListener.CLOSE);
		}


	}


}
