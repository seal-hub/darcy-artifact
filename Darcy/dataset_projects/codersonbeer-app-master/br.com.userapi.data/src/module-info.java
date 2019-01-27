module br.com.userapi.data {
    exports br.com.userapi.data;
    provides br.com.userapi.data.UserDao with
            br.com.userapi.data.impl.StaticUserImpl
            ,br.com.userapi.data.impl.RandomUserImpl;
}