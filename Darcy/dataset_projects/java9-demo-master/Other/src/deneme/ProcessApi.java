package deneme;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ProcessApi {

	public static void main(String[] args) {
		thisProcess();
		allProcesses();
		onExit();
	}

	private static void thisProcess() {
		ProcessHandle handle = ProcessHandle.current();
		System.out.println("pid: " + handle.pid());
		System.out.println("parent: " + handle.parent());
		System.out.println("children count: " + handle.children().count());
	}

	private static void allProcesses() {
		ProcessHandle.allProcesses()
				.filter(p -> p.info().command().isPresent())
				.forEach(p -> System.out.println(p.pid() + ": " + p.info()));
	}

	private static void onExit() {
		Optional<ProcessHandle> notepad = ProcessHandle.of(8528);
		if (notepad.isPresent()) {
			CompletableFuture<ProcessHandle> onExit = notepad.get().onExit();
			onExit.thenAccept(p -> System.out.println(p.toString() + " is terminated"));
		}
	}
}
