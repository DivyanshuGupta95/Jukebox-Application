package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {
    Search search;

    @BeforeEach
    void setUp(){
        search = new Search();
    }
    @AfterEach
    void tearDown(){
        search = null;
    }
    @Test
    public void givenInputShouldReturnAllSorry(){
        ArrayList<Songs> testList = search.createSongList();
        List<Songs> result1 = search.searchSongByName(testList,"sorry");

        assertEquals(1,result1.size());
    }
    @Test
    public void givenInputShouldReturnAllch(){
        ArrayList<Songs> testList = search.createSongList();
        List<Songs> result1 = search.searchSongByName(testList,"ch");

        assertEquals(10,result1.size());
    }
    @Test
    public void givenInputShouldReturnAllsad(){
        ArrayList<Songs> testList = search.createSongList();
        List<Songs> result1 = search.searchSongByGenre(testList,"sad");

        assertEquals(5,result1.size());
    }

}
