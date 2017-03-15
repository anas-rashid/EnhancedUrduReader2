package com.example.manan.enhancedurdureader.ApplicationEntities;

import java.util.Stack;

/**
 * Created by manan on 3/10/17.
 */

public class PageOffset {
    public int pageNumber = 0;
    public int startingLigatureIndex = 0;
    public int endingLigatureIndex = 0;
    public Stack<Integer> pageLigaturesCount = new Stack<Integer>();

}
