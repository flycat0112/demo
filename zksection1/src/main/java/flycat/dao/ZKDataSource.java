package flycat.dao;

import flycat.bean.ZKConfig;
import flycat.dao.watcher.DefaultKeeperStateHandler;
import flycat.dao.watcher.KeeperStateHandler;
import flycat.exception.RetryTimeOutException;
import flycat.exception.ZKException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>Title: ZKConnector</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 4/3/19
 */
public class ZKDataSource {

    private static Logger LOGGER = LoggerFactory.getLogger(ZKDataSource.class);

    private volatile ZKConfig.ZKConnection config;
    //一个ZKDataSource可能对应多个，以前的是过时的
    private ZooKeeper zooKeeper;

    private volatile CountDownLatch connectedSemaphore;

    //一个ZKDataSource对应一个watcher
    private Watcher watcher;


    private volatile int retryNum = 0;

    private volatile KeeperStateHandler keeperStateHandler;

    private ZKDataSource(ZKConfig.ZKConnection config) {
        this.config = config;
    }

    public static ZKDataSource getInstance() {
        return new ZKDataSource(new ZKConfig.ZKConnection());
    }

    public static ZKDataSource getInstance(ZKConfig.ZKConnection config){
        return new ZKDataSource(config);
    }

    private ZKDataSource getZKDataSource() {
        return this;
    }

    public void setConfig(ZKConfig.ZKConnection config) {
        synchronized (this) {
            this.config = config;
        }
    }

    public ZKConfig.ZKConnection getConfig() {
        return config;
    }

    public KeeperStateHandler getKeeperStateHandler() {
        return keeperStateHandler;
    }

    public void setKeeperStateHandler(KeeperStateHandler keeperStateHandler) {
        if (this.keeperStateHandler == null) {
            synchronized (this) {
                if (this.keeperStateHandler == null) {
                    this.keeperStateHandler = keeperStateHandler;
                }
            }
        }
    }

    public synchronized ZooKeeper doGetZooKeeper() throws IOException, TimeoutException {
        initialize();
        return zooKeeper;
    }


    /**
     * 处理zooKeeper初始化，如果连接失败会重试，超过时间才会返回，中间的过程会阻塞
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void initialize() throws IOException, TimeoutException {
        if (config == null) {
            throw new ZKException("ZooKeeper config is null！please check it");
        }
        if (zooKeeper == null && config != null) {
            if (config.getPasswd() == null) {
                zooKeeper = new ZooKeeper(config.getHostName() + ZKConfig.COLON + config.getPort()
                        , config.getTimeout(), watcher = (watcher == null ? new WatcherProxy() : watcher));
            } else {
                zooKeeper = new ZooKeeper(config.getHostName() + ZKConfig.COLON + config.getPort()
                        , config.getTimeout(), watcher = (watcher == null ? new WatcherProxy() : watcher)
                        , config.getSessionId(), config.getPasswd());
            }
            connectedSemaphore = new CountDownLatch(1);
            try {
                connectedSemaphore.await(config.getAwaitTimeout(), TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                throw new TimeoutException();
            }
            ZooKeeper.States states = zooKeeper.getState();
            if (states.isConnected()) {

            } else if(config.getRetryNum() > retryNum){
                retryNum++;
                initialize();
            } else if (config.getRetryNum() <= retryNum) {
                throw new RetryTimeOutException();
            }
        }
    }

    class WatcherProxy implements Watcher {
        @Override
        public void process(WatchedEvent event) {
            if (keeperStateHandler == null) {
                synchronized (getZKDataSource()) {
                    if (keeperStateHandler == null) {
                        keeperStateHandler = new DefaultKeeperStateHandler();
                    }
                }
            }
            Event.KeeperState keeperState = event.getState();
            if (keeperState == Event.KeeperState.Disconnected) {
                LOGGER.warn("ZooKeeper has been disconnected!");
                keeperStateHandler.processDisconnected(event);
            } else if (keeperState == Event.KeeperState.SyncConnected) {
                LOGGER.info("ZooKeeper been connected successfully!");
                synchronized (getZKDataSource()) {
                    retryNum = 0;
                }
                connectedSemaphore.countDown();
                keeperStateHandler.processSyncConnected(event);
            } else if (keeperState == Event.KeeperState.AuthFailed) {
                LOGGER.error("ZooKeeper has been authenticated failed!");
                connectedSemaphore.countDown();
                keeperStateHandler.processAuthFailed(event);
            } else if (keeperState == Event.KeeperState.SaslAuthenticated) {
                LOGGER.debug("ZooKeeper start Sasl Authentication");
                keeperStateHandler.processSaslAuthenticated(event);
            } else if (keeperState == Event.KeeperState.Expired) {
                LOGGER.warn("ZooKeeper has Expired");
                synchronized (getZKDataSource()) {
                    //需要重新生成当前的zookeeper,并且需要确定上一个zookeeper是否是connected
                    keeperStateHandler.processExpiredBefore(event);
                    zooKeeper = null;
                    try {
                        initialize();
                    } catch (IOException e) {

                    } catch (TimeoutException e) {
                    }
                    keeperStateHandler.processExpiredAfter(event);
                }
            } else if (keeperState == Event.KeeperState.ConnectedReadOnly) {
                LOGGER.info("ZooKeeper been connected successfully! it is only read.");
                synchronized (getZKDataSource()) {
                    retryNum = 0;
                }
                keeperStateHandler.processConnectedReadOnly(event);
            }
        }
    }
}

