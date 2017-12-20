package com.vividprojects.protoplanner.Interface;

import javax.inject.Inject;

/**
 * Created by Smile on 20.12.2017.
 */

public class NavigationController {
    public final static int MODE_PHONE = 1;
    public final static int MODE_TABLET = 2;
    private int mode;

    @Inject
    public NavigationController() {
        this.mode = MODE_PHONE;
    }

    public void init(int mode) {
        this.mode = mode;
    }

    public void openRecord(String id) {
        switch (mode) {
            case MODE_PHONE:
                // open new ViewRecord (id)
                break;
            case MODE_TABLET:
                // replace fragment record (id)
                break;
        }
    }
}
