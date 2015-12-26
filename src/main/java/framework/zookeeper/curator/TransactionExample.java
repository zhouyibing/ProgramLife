package framework.zookeeper.curator;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionBridge;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

public class TransactionExample {

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.80.138:2181,192.168.80.138:2182,192.168.80.138:2183")
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE,1000)).connectionTimeoutMs(5000).build();
        try {
            client.start();
            transaction(client);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
        // this example shows how to use ZooKeeper's new transactions
        CuratorTransaction  clientTransaction = client.inTransaction();
        Collection<CuratorTransactionResult> results = clientTransaction.create().forPath("/curator/transaction/a", "some data".getBytes())
                .and().setData().forPath("/curator/transaction/b", "other data".getBytes())
                .and().delete().forPath("/curator/transaction/c")
                .and().commit(); // IMPORTANT!
        // called
        for (CuratorTransactionResult result : results) {
            System.out.println(result.getForPath() + " - " + result.getType());
        }
        return results;
    }

    /*
     * These next four methods show how to use Curator's transaction APIs in a
     * more traditional - one-at-a-time - manner
     */
    public static CuratorTransaction startTransaction(CuratorFramework client) {
        // start the transaction builder
        return client.inTransaction();
    }

    public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
        // add a create operation
        return transaction.create().forPath("/a/path", "some data".getBytes()).and();
    }

    public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
        // add a delete operation
        return transaction.delete().forPath("/another/path").and();
    }

    public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
        // commit the transaction
        transaction.commit();
    }
}