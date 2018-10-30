package com.genius.lib_permissionutil;

import android.support.annotation.NonNull;

/**
 * Created by genius-ye
 */

public interface PermissionListener {

    /**
     * 通过授权
     * @param permission
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     * @param permission
     */
    void permissionDenied(@NonNull String[] permission);
}