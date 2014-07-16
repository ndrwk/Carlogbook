package ak.logbook;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Drew on 13.01.14.
 */
public class TypefaceManager {
    private static TypefaceManager TypefaceInstance;
    private Typeface mTypeface;

    private TypefaceManager (Context context) {
        mTypeface = Typeface.createFromAsset(context.getAssets(), "webhostinghub-glyphs.ttf");
    }

    public static TypefaceManager getInstance (Context context) {
        if (TypefaceInstance == null){
            TypefaceInstance = new TypefaceManager(context);
        }
        return TypefaceInstance;
    }

    public Typeface getTypeface (){
        return mTypeface;
    }


}
