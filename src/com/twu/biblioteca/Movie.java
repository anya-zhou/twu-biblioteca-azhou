package com.twu.biblioteca;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Movie extends LibraryItem {
    private static final String NO_RATING = "unrated";
    private String name;
    private String yearReleased;
    private String director;
    private Integer rating; // assumes ratings can only be whole numbers

    private static ArrayList<String> headerFields = new ArrayList<String>(){
        {
            add(ID_COL_NAME);
            add("Name");
            add("Year Released");
            add("Director");
            add("Rating");
        }
    };

    public Movie(String id, String name, String yearReleased, String director) {
        this.id = id;
        this.name = name;
        this.yearReleased = yearReleased;
        this.director = director;
    }

    public Movie(String id, String name, String yearReleased, String director, int rating){
        this(id, name, yearReleased, director);
        this.setRating(rating);
    }


    public void setRating(int rating) {
        if (rating >= 1 && rating <= 10) {
            this.rating = new Integer(rating);
        } else {
            throw new IllegalArgumentException("Rating must be an integer between 1 and 10");
        }
    }

    @Override
    public ArrayList<String> getPrintableHeaders() {
        return headerFields;
    }

    @Override
    public ArrayList<String> getPrintableFieldStrings() {
        ArrayList<String> fieldStrings = new ArrayList<>();
        fieldStrings.add(this.getId());
        fieldStrings.add(this.getName());
        fieldStrings.add(this.getYearReleased());
        fieldStrings.add(this.getDirector());
        fieldStrings.add(this.getRating() == null ? NO_RATING : Integer.toString(this.getRating()));
        return fieldStrings;
    }

    public String getName() {
        return name;
    }

    public String getYearReleased() {
        return yearReleased;
    }

    public String getDirector() {
        return director;
    }

    public Integer getRating() {
        return rating;
    }
}
