package deneme;

public class Resource {

	public static void main(String[] args) {
		final Resource r = new Resource("resource1");
		r.read();
		r.close();
	}

	public Resource(String string) {
		Thread t = new Thread();
		t.stop();
	}

	public void read() {
	}

	public void close() {
	}
}
