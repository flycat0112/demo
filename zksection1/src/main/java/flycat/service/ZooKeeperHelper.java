package flycat.service;

import flycat.bean.AuthScheme;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetAddress;

/**
 * <p>Title: AclProxy</p>
 * <p>Description: </p>
 * <p>Company: </p>
 *
 * @Author superbody
 * @Date 4/9/19
 */
public class ZooKeeperHelper {

    private ZooKeeper zooKeeper;

    public ZooKeeperHelper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void addAuthInfo(String scheme, byte auth[]){
        zooKeeper.addAuthInfo(scheme, auth);
    }
    public void addAuthInfo(String acl){
        zooKeeper.addAuthInfo(AuthScheme.WORLD.getAlias(), ("anyone:"+acl).getBytes());
    }

    public void addAuthInfo(String user, String acl){
        zooKeeper.addAuthInfo(AuthScheme.AUTH.getAlias(), (user+":"+acl).getBytes());
    }

    public void addAuthInfo(InetAddress ip, String acl){
        zooKeeper.addAuthInfo(AuthScheme.IP.getAlias(), (ip.getHostAddress()+":" +acl).getBytes());
    }

    public void addAuthInfo(String user, String digest, String acl){
        zooKeeper.addAuthInfo(AuthScheme.DIGEST.getAlias(), (user+":" + digest +":"+acl).getBytes());
    }
}

