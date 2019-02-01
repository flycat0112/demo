package flycat.handler.util;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import org.jboss.marshalling.Marshaller;

/**
 * @FileName: MarshallingEncoder
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/30
 */
public class MarshallingEncoder {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	Marshaller marshaller;

	public MarshallingEncoder() {
		this.marshaller = Marshalling;
	}


	protected void encode(Object msg, ByteBuf out){
		int lengthPos = out.writerIndex();
		out.writeBytes(LENGTH_PLACEHOLDER);
		ChannelBufferByteOutput
	}
}
