package com.drizzard.annihilationdw.utils;

import com.drizzard.annihilationdw.Main;

import org.bukkit.Bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUtil {

    private static final ExecutorService async = Executors.newSingleThreadExecutor();

    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, runnable);
    }

}
