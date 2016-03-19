package me.xhawk87.ThrottleMobSpawn;

import java.util.*;

/**
 *
 * @author XHawk87
 */
public class SpawnMonitor {

    private Deque<Long> spawns = new ArrayDeque<>();
    private int maxSpawns;
    private int interval;

    public SpawnMonitor(int maxSpawns, int interval) {
        this.maxSpawns = maxSpawns;
        this.interval = interval;
    }

    public void spawn() {
        spawns.add(System.currentTimeMillis());
    }

    public boolean canSpawn() {
        if (maxSpawns == ThrottleMobSpawn.UNLIMITED) {
            return true;
        }
        return getTotalSpawns() < maxSpawns;
    }

    public int getInterval() {
        return interval;
    }

    public int getMaxSpawns() {
        return maxSpawns;
    }

    public int getTotalSpawns() {
        long current = System.currentTimeMillis();

        long recent = current - (interval * 60000L);
        while (!spawns.isEmpty() && spawns.peek() < recent) {
            spawns.removeFirst();
        }

        return spawns.size();
    }

    @Override
    public String toString() {
        return getTotalSpawns() + " / " + getMaxSpawns() + " spawns in " + getInterval() + " minutes";
    }
}
