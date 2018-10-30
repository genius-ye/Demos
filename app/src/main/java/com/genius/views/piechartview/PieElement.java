package com.genius.views.piechartview;

public class PieElement implements PieChart.IPieElement {

    private float mValue;
    private String mColor;
    private String mDescription;

    public PieElement(final float value, final String color, final String description) {
        mValue = value;
        mColor = color;
        mDescription = description;
    }

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public String getColor() {
        return mColor;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }
}
