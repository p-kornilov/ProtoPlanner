package com.vividprojects.protoplanner.Interface;

import javax.inject.Inject;

/**
 * Created by Smile on 20.12.2017.
 */

public class NavigationController {
    public final static int MODE_PHONE = 0;
    public final static int MODE_TABLET = 1;
    private final int mode;

    @Inject
    public NavigationController(int mode) {
        this.mode = mode;
    }
}
