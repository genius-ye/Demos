package com.genius.views.drawfontview;

/**
     * float类型的点
     */
    class FloatPoint
    {
        float x;
        float y;
        public void setX(Float value)
        {
            x = value;
        }
        public void setY(Float value)
        {
            y = value;
        }

        public FloatPoint() {
        }

        public FloatPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void set(Float valueX, Float valueY)
        {
            x = valueX;
            y = valueY;
        }

        @Override
        public String toString() {
            return "["+x+","+y+"]";
        }
    }