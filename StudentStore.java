import java.util.HashMap;
import java.util.Map;

public class StudentStore {

    // Stores: username -> String[]{ password, fullName, email, course, year }
    private static final Map<String, String[]> students = new HashMap<>();

    static {
        // Seed one demo student
        students.put("admin", new String[]{"admin123", "Admin User", "admin@portal.edu", "Computer Science", "3"});
    }

    public static boolean register(String username, String password, String fullName, String email, String course, String year) {
        if (students.containsKey(username)) return false;
        students.put(username, new String[]{password, fullName, email, course, year});
        return true;
    }

    public static boolean authenticate(String username, String password) {
        String[] data = students.get(username);
        return data != null && data[0].equals(password);
    }

    public static String[] getStudent(String username) {
        return students.get(username);
    }

    public static boolean exists(String username) {
        return students.containsKey(username);
    }
}