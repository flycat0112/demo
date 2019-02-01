package flycat.model;

/**
 * @FileName: NettyMessage
 * @Description:
 * @Author SuperBoy
 * @Date 2019/1/30
 */
public class NettyMessage {

	private Header header;

	private Object body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
