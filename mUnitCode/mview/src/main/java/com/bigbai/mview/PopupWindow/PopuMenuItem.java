package com.bigbai.mview.PopupWindow;

import java.util.List;

public class PopuMenuItem {
    public List<String> titles;  // 菜单分类标题
    public List<List<String>> item_names; // 选项名称
    public List<List<Integer>> item_images; // 选项图标

    public PopuMenuItem(List<String> titles, List<List<String>> item_names, List<List<Integer>> item_images) {
        this.titles = titles;
        this.item_names = item_names;
        this.item_images = item_images;
    }
}
