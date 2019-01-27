package deneme;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FutureDeneme {

	public static void main(String[] args) {
		CompletableFuture<String> completableFuture = new CompletableFuture<>();

		Executors.newCachedThreadPool().submit(() -> {
			Thread.sleep(1000);
			completableFuture.complete("completed");
			return null;
		});

		Executors.newCachedThreadPool().submit(() -> {
			Thread.sleep(500);
			completableFuture.cancel(false);
			return null;
		});

	}
}
