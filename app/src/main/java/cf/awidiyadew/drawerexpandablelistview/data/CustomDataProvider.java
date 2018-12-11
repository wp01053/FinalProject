package cf.awidiyadew.drawerexpandablelistview.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cf.awidiyadew.drawerexpandablelistview.MainActivity;
import cf.awidiyadew.drawerexpandablelistview.dto.StaticVariable;

import static android.content.Context.MODE_PRIVATE;
import static com.estimote.sdk.cloud.internal.ApiUtils.getSharedPreferences;

/**
 * Created by awidiyadew on 15/09/16.
 */
public class CustomDataProvider {

    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;

    private static List<BaseItem> mMenu = new ArrayList<>();

    public static List<BaseItem> getInitialItems() {
        //return getSubItems(new GroupItem("root"));

        Log.d("123123", StaticVariable.loginType);

        List<BaseItem> rootMenu = new ArrayList<>();

        /*
         * ITEM = TANPA CHILD
         * GROUPITEM = DENGAN CHILD
         * */
        rootMenu.add(new GroupItem("MY PAGE"));
        rootMenu.add(new Item("오시는 길"));
        rootMenu.add(new Item("학원 소개"));
        rootMenu.add(new Item("공지사항"));
        rootMenu.add(new Item("출석 체크"));

        if(StaticVariable.loginType.equals( "TEACHER" )){
            rootMenu.add(new Item("선생님용 출석체크"));
        }

        rootMenu.add(new Item("반 채팅"));
        rootMenu.add(new Item("메세지 보내기"));
        rootMenu.add(new Item("로그아웃"));

        return rootMenu;
    }


    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();

        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        /*
         * HANYA UNTUK GROUP-ITEM
         * */
        switch (level) {
            case LEVEL_1:
                switch (menuItem.toUpperCase()) {
                    case "MY PAGE":
                        result = getListKategoriLainnya();
                        break;
//                    case "게시판":
////                        result = getListKategori();
////                        break;
//                    case "출석 체크":
//                        result = getcheck();
//                        break;



                }
                break;

            case LEVEL_2:
                switch (menuItem) {
                    case "GROUP 1":
                        result = getListGroup1();
                        break;
                    case "GROUP X":
                        result = getListGroupX();
                        break;
                }
                break;
        }

        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }



//    private static List<BaseItem> getListKategori() {
//
//        List<BaseItem> list = new ArrayList<>();
//
//        // Setiap membuat groupItem harus di set levelnya
//        GroupItem groupItem = new GroupItem("GROUP 1");
//        groupItem.setLevel(groupItem.getLevel() + 1);
//
//        list.add(new Item("Q&A"));
//        list.add(new Item("자료공유"));
//        list.add(new Item("건의 게시판"));
//
//        list.add(groupItem);
//
//        return list;
//    }


    private static List<BaseItem> getListKategoriLainnya() {

        List<BaseItem> list = new ArrayList<>();
        GroupItem groupItem = new GroupItem("GROUP X");
        groupItem.setLevel(groupItem.getLevel() + 1);

        list.add(new Item("출석 현황"));
        list.add(new Item("교육 일정"));
        list.add(groupItem);

        return list;
    }

//    private static List<BaseItem> getcheck() {
//
//        List<BaseItem> list = new ArrayList<>();
//        GroupItem groupItem = new GroupItem("GROUP X");
//        groupItem.setLevel(groupItem.getLevel() + 1);
//
//        list.add(new Item("item1"));
//        list.add(new Item("item2"));
//        list.add(groupItem);
//
//        return list;
//    }

    private static List<BaseItem> getListGroup1() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("CHILD OF G1-A"));
        list.add(new Item("CHILD OF G1-B"));

        return list;
    }

    private static List<BaseItem> getListGroupX() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("CHILD OF GX-A"));
        list.add(new Item("CHILD OF GX-B"));

        return list;
    }

}
