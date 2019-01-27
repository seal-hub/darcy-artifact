package org.modules.pub;

import org.modules.pub.service.Bartender;
import org.modules.pub.service.BartenderImpl;

/**
 * Created by aleksandra on 05.10.17.
 */
public class Pub {

    public static void main (String[] args) {
        Bartender bartender = new BartenderImpl();
        bartender.sayHello();
    }
}
