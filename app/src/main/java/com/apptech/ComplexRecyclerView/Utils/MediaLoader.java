package com.apptech.ComplexRecyclerView.Utils;

/**
 * Created by nirob on 7/6/17.
 */

public class MediaLoader {

/*    private static MediaLoader self = null;

    private MediaLoader(){

    }

    public List<Model> getListAlreadyShared() {
        return listAlreadyShared;
    }



    private List<Model> listAlreadyShared;

    public void loadList(Context context) {
        if (listAlreadyShared == null) {
            listAlreadyShared = new ArrayList<>();
        } else {
            listAlreadyShared.clear();
        }
        DBHelper dbManager = new DBHelper(context);
        listAlreadyShared = dbManager.getAllJsonData();

    }

    public static final MediaLoader getInstance() {
        if (self == null) {
            synchronized (MediaLoader.class) {
                if (self == null) {
                    self = new MediaLoader();
                }
            }
        }
        return self;

    }

    public boolean containsMedia(String path) {
        if (listAlreadyShared == null||path==null) {
            return false;
        }
        for (Model item : listAlreadyShared) {
            if (item.getImage().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public Model getMatchItem(String path) {
        if (listAlreadyShared == null) {
            return null;
        }
        if (path == null) {
            return null;
        }
        Iterator<Model> iterator = listAlreadyShared.iterator();
        while (iterator.hasNext()) {
            Model item = iterator.next();
            if (item.getImage().equals(path)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Model sharedItem) {
        if (listAlreadyShared == null) {
            return;
        }
        listAlreadyShared.remove(sharedItem);
    }*/
}
