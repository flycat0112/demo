package flycat;

import flycat.bean.AuthScheme;
import flycat.bean.ZKConfig;
import flycat.dao.ZKDataSource;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>Title: ZKCreateNode</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 4/3/19
 */
public class ZKCreateNode {
    private static ZooKeeper zooKeeper;

    static {
        try {
            ZKConfig.ZKConnection config = new ZKConfig.ZKConnection();
            zooKeeper = ZKDataSource.getInstance(config).doGetZooKeeper();
        } catch (IOException e) {
        } catch (TimeoutException e) {
        }
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        System.out.println(zooKeeper.getState());
        System.out.println(zooKeeper.create("/zk-book", "I love you!".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL
                , CreateMode.EPHEMERAL));
    }
}

