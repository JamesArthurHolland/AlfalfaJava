package com.github.jamesarthurholland.alfalfa;

import com.google.common.collect.Maps;
import com.hubspot.jinjava.Jinjava;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class JinJavaTests {

    @Test
    public void render() {
        Jinjava jinjava = new Jinjava();
        Map<String, String> context = Maps.newHashMap();

        context.put("name", "user1");

        String template = "hello {{name}}";

        assertTrue(jinjava.render(template, context).equals("hello user1"));
    }
}
