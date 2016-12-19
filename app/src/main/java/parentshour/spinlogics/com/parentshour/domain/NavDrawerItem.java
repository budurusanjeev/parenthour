package parentshour.spinlogics.com.parentshour.domain;

/**
 * Created by Goutam ic_on 8/26/2015.
 */
public class NavDrawerItem extends BaseDomain
{

    public String title;
    public int icon;
    public String count;
    // boolean to set visiblity of the counter
    public boolean isCounterVisible = false;

    public NavDrawerItem(String title, int icon){

        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

}
