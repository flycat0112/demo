package flycat.bean;


import lombok.Data;

import java.net.InetAddress;

public class ZKConfig{
    public static String COLON = ":";

    @Data
    public static class ZKConnection{
        private String hostName;
        private int port;
        private int timeout;
        private int awaitTimeout;
        private int retryNum;
        private long sessionId;
        private byte[] passwd;
        private boolean canBeReadOnly;

        public ZKConnection() {
            this((String) Default.HOST_NAME.getVal(), (int) Default.PORT.getVal(), (int) Default.TIMEOUT.getVal());
        }

        public ZKConnection(String hostName, int port) {
            this(hostName, port, (int) Default.TIMEOUT.getVal());
        }

        public ZKConnection(String hostName, int port, int timeout) {
            this(hostName, port, timeout, (int) Default.AWAIT_TIMEOUT.getVal(), (int) Default.RETRY_NUM.getVal() );
        }

        public ZKConnection(String hostName, int port, int timeout, int awaitTimeout, int retryNum){
            this.hostName = hostName;
            this.port = port;
            this.timeout = timeout;
            this.awaitTimeout = awaitTimeout;
            this.retryNum = retryNum;
        }

        public ZKConnection(String hostName, int port, int timeout, int awaitTimeout
                , int retryNum, long sessionId, byte[] passwd) {
            this.hostName = hostName;
            this.port = port;
            this.timeout = timeout;
            this.awaitTimeout = awaitTimeout;
            this.retryNum = retryNum;
            this.sessionId = sessionId;
            this.passwd = passwd;
        }

        public ZKConnection(String hostName, int port, int timeout, int awaitTimeout
                , int retryNum, long sessionId, byte[] passwd, boolean canBeReadOnly) {
            this.hostName = hostName;
            this.port = port;
            this.timeout = timeout;
            this.awaitTimeout = awaitTimeout;
            this.retryNum = retryNum;
            this.sessionId = sessionId;
            this.passwd = passwd;
            this.canBeReadOnly = canBeReadOnly;
        }


        enum Default{
            HOST_NAME("localhost"),
            PORT(2181),
            TIMEOUT(5000),
            AWAIT_TIMEOUT(500),
            RETRY_NUM(3);

            private Object val;

            Default(Object val) {
                this.val = val;
            }

            public Object getVal() {
                return val;
            }

        }


    }
}
