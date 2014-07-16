package ak.logbook;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Drew on 01.06.13.
 */
public class Icon {

    public static int getIcon(int id){
        IconsEnum[] UtfArray = IconsEnum.values();
        return UtfArray[id].getIconUtfValue();
        }

    public static IconsEnum [] getArray (){
        return IconsEnum.values();
    }

    public static ArrayList <IconsEnum> getArrayList (){
        ArrayList<IconsEnum> icons = new ArrayList<IconsEnum>();
        icons.addAll(Arrays.asList(IconsEnum.values()));
        return icons;
    }

}
