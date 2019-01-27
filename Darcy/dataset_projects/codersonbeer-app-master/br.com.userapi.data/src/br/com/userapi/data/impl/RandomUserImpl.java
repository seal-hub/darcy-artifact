package br.com.userapi.data.impl;

import br.com.userapi.data.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUserImpl implements UserDao{
    final String alphabet = "abcdefghijklmnopqrstuvxwyzABCDEFGHIJKLMNOPQRSTUVXWYZ";

    @Override
    public List<String> findNames() {
        final int N = alphabet.length();
        List<String> names = new ArrayList<>();
        Random r = new Random();
        StringBuilder randomWord;
        for (Long a = 0l; a < 20l; a++) {
            randomWord = new StringBuilder();
            while (randomWord.length() < 8) {
                randomWord = randomWord.append(String.valueOf(alphabet.charAt(r.nextInt(N))));
            }
            names.add(randomWord.toString());
        }
        return names;
    }
}
