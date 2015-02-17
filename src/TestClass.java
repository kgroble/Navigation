//hello
public class TestClass {

	public static void main(String[] args) {
		boolean a = isInt("111");
		if (a)
			System.out.println("ya");
	}

	// Christian's test comment
	// Coleman's test comment
	public static boolean isInt(String s) {
		for (int i = 0; i < s.length() - 1; i++) {
			char c = s.charAt(i);
			if (c != '1' && c != '2' && c != '3' && c != '4' && c != '5'
					&& c != '6' && c != '7' && c != '8' && c != '9' && c != '0')
				return false;
		}
		return true;
	}
}
