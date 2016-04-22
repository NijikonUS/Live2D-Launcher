package us.nijikon.livelylauncher.models;

/**
 * Created by mjwei on 4/7/16.
 */
public class Type {
    long typeId;
    private String categoryName;

    public Type(String name){
        categoryName = name;
    }

    public Type(){

    }

    public void setTypeId(long typeId){
        this.typeId = typeId;
    }

    public void setTypeName(String categoryName){
        this.categoryName = categoryName;
    }

    public long getCategoryId(){
        return typeId;
    }

    public String getCategoryName(){
        return categoryName;
    }

}
