package com.drizzard.annihilationdw.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUtil {

	private static final ExecutorService async = Executors.newSingleThreadExecutor();

	public static void run(Runnable runnable) {
		async.submit(runnable);
	}

	public static void run(Callable<?> callable) {
		async.submit(callable);
	}

	public static void stop() {
		async.shutdownNow();
	}

}
