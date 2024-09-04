package com.iamyanbing.service.impl;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.req.LockRequest;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service("zookeeperLock")
@Slf4j
public class ZookeeperLock implements LockService {

    @Value("${zookeeper.address}")
    private String address;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Override
    public ResponseResult execute(LockRequest req) {
        ResponseResult grab = null;
        try {
            // 获取锁
            // 第一步：连接Zookeeper客户端
            CountDownLatch countDownLatch = new CountDownLatch(1);

            // Watcher 作用？
            // 实现 ZooKeeper 连接创建成功之后 才执行后面代码。 需要 CountDownLatch 协助完成。
            ZooKeeper zooKeeper = new ZooKeeper(address, timeout, new Watcher() {
                /**
                 * ZooKeeper 连接创建成功之后， 事件回调
                 *
                 * @param watchedEvent
                 */
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                        countDownLatch.countDown();
                    }
                }
            });
            // ZooKeeper 连接创建不成功，就一直等待
            countDownLatch.await();

            // 第二步：创建持久节点
            Long orderId = req.getOrderId();
            String parentNode = "/order-" + orderId;

            Stat exists = zooKeeper.exists(parentNode, false);
            if (exists == null) {
                // OPEN_ACL_UNSAFE：这个节点谁都可以操作，是一个开放的。
                zooKeeper.create(parentNode, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // 第三步：创建临时顺序节点
            String seq = "/seq";
            String s = zooKeeper.create(parentNode + seq, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // 第四步：判断我的节点是不是 所有子节点中最小的
            boolean ifLock = false;
            // orderId = 138
            // /order-138/seq0000000001
            if (StringUtils.isNotBlank(s)) {
                List<String> children = zooKeeper.getChildren(parentNode, false);
                Collections.sort(children);
                if ((children.size() > 0) && ((parentNode + "/" + children.get(0)).equals(s))) {
                    ifLock = true;

                    log.info("获取锁成功");

                    //TODO 第五步：执行业务代码

                    log.info("任务执行完成");
                }

            }
            if (!ifLock) {
                grab = ResponseResult.fail(CommonStatusEnum.FAIL.getCode(), CommonStatusEnum.FAIL.getMessage());
            }

            // TODO 因为 /order-138 是持久节点，所以使用完最好删除

            // 第六步：关闭链接
            // 第六步：释放锁
            zooKeeper.close();

        } catch (IOException e) {
            log.error("ZooKeeper 连接创建失败", e);
        } catch (InterruptedException e) {
            log.error("操作节点失败或者关闭zookeeper连接失败", e);
        } catch (KeeperException e) {
            log.error("操作节点失败", e);
        }
        return grab;
    }
}
