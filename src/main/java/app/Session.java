package app;

public final class Session {
    private Session() {}

    private static UserDao.UserRecord currentUser;

    public static UserDao.UserRecord getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserDao.UserRecord user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
