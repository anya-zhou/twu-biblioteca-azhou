package com.twu.biblioteca;

import java.util.ArrayList;
import java.util.stream.Collectors;

class Library<T extends LibraryItem> {
    private ArrayList<T> items;
    private ArrayList<String> headers = new ArrayList<>();
    private String itemDescription;

    Library() {
        this.items = new ArrayList<T>();
    }

    Library(ArrayList<T> items) {
        this.items = items;
        if (items.size() > 0) {
            itemDescription = items.get(0).getDescription();
            headers = items.get(0).getPrintableHeaders();
        }
    }

    ArrayList<T> getItems() {
        return items;
    }

    T getItem(String itemId) {
        for (T item : this.items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    ArrayList<T> getAvailableItems() {
        return (ArrayList<T>) this.items.stream().filter(item -> item.isAvailable()).collect(Collectors.toList());
    }

    ArrayList<String> getHeaderStrings() {
        return headers;
    }

    String getItemDescription() {
        return itemDescription;
    }
}
