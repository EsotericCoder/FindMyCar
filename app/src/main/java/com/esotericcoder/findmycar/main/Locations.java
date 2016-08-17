package com.esotericcoder.findmycar.main;

import java.util.List;

public class Locations {

    List<Item> Items;
    Integer Count;
    Integer ScannedCount;

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public Integer getScannedCount() {
        return ScannedCount;
    }

    public void setScannedCount(Integer scannedCount) {
        ScannedCount = scannedCount;
    }
}