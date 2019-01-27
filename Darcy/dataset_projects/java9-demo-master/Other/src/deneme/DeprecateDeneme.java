package deneme;

@Deprecated(forRemoval = true, since = "9")
public class DeprecateDeneme {

	@Deprecated(forRemoval = true, since = "9")
	private void doo() {
		// old algorithm
	}

	public static void main(String[] args) {
		DeprecateDeneme deneme = new DeprecateDeneme();
		deneme.doo();
	}
}
