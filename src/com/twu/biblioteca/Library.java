package com.twu.biblioteca;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library<T extends LibraryItem> {
    private ArrayList<T> items;
    private ArrayList<String> headers = new ArrayList<>();

    public Library() {
        this.items = new ArrayList<T>();
    }

    public Library(ArrayList<T> items) {
        this.items = items;
        if (items.size() > 0) {
            headers = items.get(0).getPrintableHeaders();
        }
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public T getItem(String itemId) {
        for (T item : this.items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<T> getAvailableItems() {
        return (ArrayList<T>) this.items.stream().filter(item -> item.isAvailable()).collect(Collectors.toList());
    }

    public ArrayList<String> getHeaderStrings() {
        return headers;
    }
}
