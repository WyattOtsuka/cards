package com.otsuka.cards;

import android.view.View;
import android.view.ViewGroup;

public class helper {
    public static void sendViewToBack(final View child, final ViewGroup parent) {
        //ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public static void bringToFront(final View child, final ViewGroup parent) {
        if (parent != null) {
            parent.bringChildToFront(child);
        }
    }
}
