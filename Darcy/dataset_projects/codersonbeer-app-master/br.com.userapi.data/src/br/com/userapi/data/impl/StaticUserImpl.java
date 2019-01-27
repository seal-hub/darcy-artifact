package br.com.userapi.data.impl;

import br.com.userapi.data.UserDao;

import java.util.List;

public class StaticUserImpl implements UserDao {

    List<String> names = List.of("Carolina Josefa Leopoldina Francisca Fernanda de Habsburgo-Lorena"
            , "Maria Francisca Isabel de Saboia"
            , "Teresa Cristina Maria Josefa Gaspar Baltasar Melchior Januária Rosalía Lúcia Francisca de Assis Isabel Francisca de Pádua Donata Bonosa Andréia de Avelino Rita Liutgarda Gertrude Venância Tadea Spiridione Roca Matilde");
    @Override
    public List<String> findNames() {
        return names;
    }

}
