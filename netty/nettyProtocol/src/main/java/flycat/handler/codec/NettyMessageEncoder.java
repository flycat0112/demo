package flycat.handler.codec;

import flycat.handler.util.MarshallingEncoder;
import flycat.model.Header;
import flycat.model.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * @FileName: NettyMessageEncoder
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/30
 */
public class NettyMessageEncoder
		extends MessageToMessageEncoder<NettyMessage> {

	MarshallingEncoder marshallingEncoder;

	public NettyMessageEncoder() {
		this.marshallingEncoder = new MarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List out) throws Exception {
		if(msg == null || msg.getHeader() ==null){
			throw new Exception("");
		}

		Header header = msg.getHeader();
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(header.getCrcCode());
		sendBuf.writeInt(header.getLength());
		sendBuf.writeLong(header.getSessionId());
		sendBuf.writeByte(header.getType());
		sendBuf.writeByte(header.getPriority());
		sendBuf.writeInt(header.getAttachment().size());
		String key = null;
		byte[] keyArray = null;

		Object value = null;

		for (Map.Entry<String, Object> param : header.getAttachment().entrySet()){
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();

		}

		key  = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null){

		}
	}
}
