package handler;

public class LogoutHandler {
    private final service.UserService userService = new service.UserService();

//    public String handleRequest(Context ctx, AuthDAO authDAO) throws DataAccessException {
//        LogoutRequest logout = new Gson().fromJson(ctx.body(), LogoutRequest.class);
//        LogoutResult logoutResult = userService.logout(logout, authDAO);
//        return new Gson().toJson(logoutResult);
//    }
}
