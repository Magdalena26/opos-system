package com.opos;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by kasia on 11.01.17.
 */
public class GEXFFileCreatorTest {

    ArrayList<String> names = new ArrayList<String>() {{
        add("Anne Fine");
        add("Adam Berton");
        add("James Azam");
        add("John Besson");
        add("Jill Murphy");
        add("Jenny Dale");
        add("Barbara Park");
        add("Tony Bradman");
        add("Humphrey Carpenter");
    }};
    //GEXFFileCreator gexfFileCreator = new GEXFFileCreator(names);

    @Test
    public void checkIfFileCreated() {
        //given
        GEXFFileCreator gexfFileCreator = new GEXFFileCreator("Alice", 50);
        //when
        File file = new File("src/main/resources/graphData.gexf");
        //then
        assertTrue(file.exists());
    }

}