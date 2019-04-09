package flycat.dao.watcher;

import org.apache.zookeeper.WatchedEvent;

public interface KeeperStateHandler {
    default void processDisconnected(WatchedEvent event){

    }
    default void processSyncConnected(WatchedEvent event){

    }

    default void processAuthFailed(WatchedEvent event){

    }

    default void processConnectedReadOnly(WatchedEvent event){

    }

    default void processSaslAuthenticated(WatchedEvent event){

    }

    default void processExpiredBefore(WatchedEvent event){

    }

    default void processExpiredAfter(WatchedEvent event){

    }
}
