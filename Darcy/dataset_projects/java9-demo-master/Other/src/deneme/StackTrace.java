package deneme;

import java.lang.StackWalker.StackFrame;
import java.util.List;
import java.util.stream.Collectors;

public class StackTrace {

	public static void main(String[] args) {
		getAllStackTrace();
		getTenLineStackTrace();
	}

	private static void getTenLineStackTrace() {
		List<StackFrame> frames = StackWalker.getInstance().walk(
				s -> s.dropWhile(f -> f.getClassName().startsWith("com.foo.")).limit(10).collect(Collectors.toList()));
		System.out.println("Ten frames : \n" + frames.toString());
	}

	private static void getAllStackTrace() {
		List<StackFrame> frames = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
				.walk((s) -> s.collect(Collectors.toList()));
		System.out.println("All frames : \n" + frames.toString());
	}
}
