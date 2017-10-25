package com.ming.common.view;

import java.util.List;

/**
 * Created by yushijie on 2017/7/20.
 */

public interface DragGridBaseAdapter<T> {
    /**
     * @param oldPosition
     * @param newPosition
     */
    void reorderItems(int oldPosition, int newPosition);


    /**
     * @param hidePosition
     */
    void setHideItem(int hidePosition);

    /**
     * @param removePosition
     */
    void removeItem(int removePosition);

    /**
     * @param lists
     */
    void setData(List<T> lists);
}
