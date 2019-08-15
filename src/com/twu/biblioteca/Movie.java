package com.twu.biblioteca;

public class Movie extends LibraryItem {
    private String name;
    private String yearReleased;
    private String director;
    private Integer rating;

    public Movie(String id, String name, String yearReleased, String director) {
        this.id = id;
        this.name = name;
        this.yearReleased = yearReleased;
        this.director = director;
    }

    public Movie(String id, String name, String yearReleased, String director, int rating) {
        this(id, name, yearReleased, director);
        this.rating = new Integer(rating);
    }

    @Override
    public String getFormattedString(int idColWidth, int colWidth, String colDivider) {
        String movieString = formatToCol(this.getId(), idColWidth, colDivider);
        movieString += formatToCol(this.getName(), colWidth, colDivider);
        movieString += formatToCol(this.getYearReleased(), colWidth, colDivider);
        movieString += formatToCol(this.getDirector(), colWidth, colDivider);
        movieString += formatToCol(this.getRating() == null ? "N/A" : Integer.toString(this.getRating()), colWidth, colDivider);
        return movieString;
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
