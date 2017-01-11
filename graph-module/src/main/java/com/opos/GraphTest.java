package com.opos;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by kasia on 11.01.17.
 */
public class GraphTest {

    @Test
    public void checkIfGraphCreated() {
        //given
        Graph graph = new Graph();
        //when
        File file = new File("src/main/resources/graph.png");
        //then
        assertTrue(file.exists());
    }

}