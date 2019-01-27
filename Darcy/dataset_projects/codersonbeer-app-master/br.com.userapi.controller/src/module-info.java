module br.com.userapi.controller {
    requires br.com.userapi.data;
    uses br.com.userapi.data.UserDao;

    exports br.com.userapi.controller to br.com.userapi.data, br.com.userapi.main;
}