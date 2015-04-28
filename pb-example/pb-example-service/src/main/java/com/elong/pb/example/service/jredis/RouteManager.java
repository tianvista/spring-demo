package com.elong.pb.example.service.jredis;

import com.elong.pb.common.util.log.PlatformLogger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * project：pb-monitor-common
 * Date: 15/4/24
 * Time: 15:13
 * file: RouteManager.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class RouteManager {
    private volatile RouteSet routeSet;
    Random random = new Random();
    final Map<String, RouteData> singleCache = new HashMap<>();
    ExecutorService executor_retry = Executors.newSingleThreadExecutor();
    final Retry retry = new Retry();
    private static AtomicIntegerArray RANDOM_COUNT = new AtomicIntegerArray(3);

    public RouteManager(List<ServerInfo> infos) {
        routeSet = createRouteSet(infos);
        startRetry();
    }

    synchronized RouteData getAtomic(ServerInfo info) {
        RouteData route = singleCache.get(info.generateKey());
        if (route == null) {
            route = new RouteData(info);
            singleCache.put(info.generateKey(), route);
        }

        return route;
    }

    private RouteSet createRouteSet(List<ServerInfo> infos) {
        List<RouteData> routes = new ArrayList<RouteData>();
        for (int i = 0; i < infos.size(); i++) {
            routes.add(getAtomic(infos.get(i)));
        }
        return new RouteSet(infos, routes);
    }

    public AtomicIntegerArray getRandomCount() {
        return RANDOM_COUNT;
    }

    private RouteData route(RouteSet routeSet, List<RouteData> selectedRouteDatas) {
        int[] weights = routeSet.getWeights();
        int max_random = weights[weights.length - 1];
        int preRandom = -1;
        int x = random.nextInt(max_random);

        for (int count = 0; count < 5; count++) {
            x = random.nextInt(max_random);
            while (x == preRandom) {
                x = random.nextInt(max_random);
            }
            preRandom = x;
            RANDOM_COUNT.incrementAndGet(x);

            for (int i = 0; i < weights.length; i++) {
                if (x < weights[i]) {
                    RouteData data = routeSet.getRoutes().get(i);
                    if ((selectedRouteDatas != null) && (selectedRouteDatas.contains(data))) {
                        break;
                    }
                    return routeSet.getRoutes().get(i);
                }
            }
        }

        throw new JedisConnectionException("read node is empty");
    }

    public RouteData route() {
        return route(routeSet, null);
    }

    public RouteData route(List<RouteData> selectedRouteDatas) {
        return route(routeSet, selectedRouteDatas);
    }

    public List<RouteData> getWritableServers() {
        return routeSet.getRoutes();
    }

    public List<RouteData> getReadableServers() {
        return routeSet.getRoutes();
    }

    private void sendMsg(String errorMsg) {
//        AlarmEntity entity = new AlarmEntity();
//        entity.setEventTypeName("jredis");
//        entity.setEventInstanceName("jredis-monitor");
//        entity.setAlertTitle(errorMsg);
//        entity.setAlertMessage(errorMsg);
//        entity.setValue(500);
//        SendToLogServerUtil.sendLog(entity);

    }

    public synchronized void handleError(RouteData routeData) {
        if (!routeData.isAvailable()) {
            return;
        }

        routeData.handleError();

        if (!routeData.isAvailable()) {
            retry.addRetry(routeData);
            sendMsg(routeData.getInfo() + " is down now");
            PlatformLogger.error("start to detect server " + routeData.getInfo());
        }
    }

    public synchronized void onError(RouteData route) {
        List<ServerInfo> new_infos = (List<ServerInfo>)((ArrayList)routeSet.getInfos()).clone();
        if (new_infos.remove(route.getInfo())) {
            routeSet = createRouteSet(new_infos);
            retry.addRetry(route);
            PlatformLogger.error("start to detect server " + route.getInfo());
        }
    }

    private synchronized void handleSucces(RouteData routeData) {
        routeData.handleSuccess();
    }

    private synchronized void onReturn(RouteData route) {
        List<ServerInfo> new_infos = (List<ServerInfo>)((ArrayList)routeSet.getInfos()).clone();

        if (!new_infos.contains(route.getInfo())) {
            new_infos.add(route.getInfo());
        }
        routeSet = createRouteSet(new_infos);
        PlatformLogger.error("server " + route.getInfo() + " alive again");
    }

    public void destroy() {
        retry.exit = true;
        synchronized (retry) {
            retry.notify();
        }
        executor_retry.shutdownNow();
        synchronized (singleCache) {
            for (Map.Entry<String, RouteData> entry : singleCache.entrySet()) {
                entry.getValue().destroy();
            }
        }
    }

    final void startRetry() {
        executor_retry.execute(retry);
    }

    private class Retry implements Runnable {
        volatile boolean exit = false;
        private final static int RETRY_INTERVAL = 1; // unit is second
        CopyOnWriteArraySet<RouteData> set = new CopyOnWriteArraySet<RouteData>();

        public void addRetry(RouteData single) {
            set.add(single);
            synchronized (Retry.this) {
                this.notify();
            }
        }

        @Override
        public void run() {
            while (!exit) {
                for (RouteData s : set) {
                    try {
                        synchronized (singleCache) {
                            RouteData route = singleCache.remove(s.getInfo().generateKey());
                            if (route != null) {
                                route.destroy();
                                PlatformLogger.message(route.getInfo().toString() + " destory successly");
                            }
                        }
                    } catch (Exception e) {
                        PlatformLogger.error("", e);
                    }
                    //RouteData routeData = new RouteData(s.getInfo());
                    JedisPool pool = s.createJedisPool();
                    Jedis jedis = null;
                    boolean error = true;
                    try {
                        jedis = pool.getResource();
                        pool.getResource().ping();
                        synchronized (singleCache) {
                            singleCache.put(s.getInfo().generateKey(), s);
                        }
                        error = false;
                        s.setPool(pool);
                        s.handleSuccess();
                        // success
                        //onReturn(routeData);

                        set.remove(s);
                        sendMsg(s.getInfo() + " recover now");
                        PlatformLogger.message(s.getInfo().toString() + " recover now");
                    } catch (Throwable t) {
                        if (jedis != null) {
                            pool.returnBrokenResource(jedis);
                        }
                        try {
                            pool.destroy();
                            //routeData.destroy();
                        } catch (Exception e) {
                        }
                    } finally {
                        if (jedis != null && error == false) {
                            pool.returnResource(jedis);
                        }
                    }
                }
                try {
                    synchronized (Retry.this) {
                        wait(1000 * RETRY_INTERVAL);
                    }
                } catch (InterruptedException ex) {
                    PlatformLogger.error("Retry Thread InterruptedException", ex);
                }
            }
        }
    }
}
