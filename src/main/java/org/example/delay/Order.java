package org.example.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Order implements Delayed {

    /**
     * 延迟时间
     */

    private long time;

    String name;

    public Order(String name, long time,TimeUnit unit) {
        this.name = name;
        this.time = System.currentTimeMillis()  + (time > 0 ?unit.toMillis(time) : 0);
    }

    /**
     *
     * @param unit the time unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        Order order = (Order) o;
        long diff = this.time - order.time;
        if (diff <= 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
