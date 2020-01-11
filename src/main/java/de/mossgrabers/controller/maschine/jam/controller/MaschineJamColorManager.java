// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.controller;

import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.color.ColorEx;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.daw.DAWColor;
import de.mossgrabers.framework.mode.AbstractMode;
import de.mossgrabers.framework.scale.Scales;
import de.mossgrabers.framework.view.AbstractDrumView;
import de.mossgrabers.framework.view.AbstractPlayView;


/**
 * Color states to use for the Maschine Jam buttons.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
@SuppressWarnings("javadoc")
public class MaschineJamColorManager extends ColorManager
{
	public static final int COLOR_BLACK        = 0; // Off
	public static final int COLOR_LIGHT_ORANGE=12;
	public static final int COLOR_WARM_YELLOW=16;
	public static final int COLOR_YELLOW=20;
	public static final int COLOR_MINT=32;
	public static final int COLOR_PLUM=48;
	public static final int COLOR_WHITE=68;
	
	public static final int COLOR_GREY = 66; 
	public static final int COLOR_TURQUOISE=42;
	public static final int COLOR_TURQUOISE_LO=40;
	public static final int COLOR_BLUE=46;
	public static final int COLOR_BLUE_LO=44;
	public static final int COLOR_PURPLE=58;
	public static final int COLOR_PURPLE_LO=56;
	public static final int COLOR_LIME=26;
	public static final int COLOR_LIME_LO=24;
	public static final int COLOR_GREEN=30;
	public static final int COLOR_GREEN_LO=28;
	public static final int COLOR_RED = 6;
	public static final int COLOR_RED_LO = 4;
	public static final int COLOR_ORANGE=10;
	public static final int COLOR_ORANGE_LO=8;
	public static final int COLOR_CYAN=38;
	public static final int COLOR_CYAN_LO=36;
	public static final int COLOR_FUCHSIA=66;
	public static final int COLOR_FUCHSIA_LO=64;
	public static final int COLOR_MAGENTA_LO=62;
	public static final int COLOR_MAGENTA=60;
	public static final int COLOR_VIOLET=54;
	public static final int COLOR_VIOLET_LO=52;
	
	public static final int COLOR_AMBER = COLOR_LIGHT_ORANGE; 
	public static final int COLOR_AMBER_LO = COLOR_ORANGE;
	public static final int COLOR_PINK = COLOR_FUCHSIA;
	public static final int COLOR_PINK_LO = COLOR_FUCHSIA_LO;
	public static final int COLOR_SKY = COLOR_CYAN;
	public static final int COLOR_SKY_LO = COLOR_CYAN_LO;
	public static final int COLOR_SKIN = COLOR_PLUM;
	public static final int COLOR_SPRING = COLOR_VIOLET;
	public static final int COLOR_SPRING_LO = COLOR_VIOLET_LO;
	public static final int COLOR_ROSE = COLOR_PLUM;
	
	/*
		var JAMColorVars = {
		    DIM:0,
		    DIMFL:1,
		    BRIGHT:2,
		    BRIGHTFL:3
		};
	*/
	
//    public static final int COLOR_RED          = 6;
//    public static final int COLOR_RED_LO       = 5;


    /**
     * Constructor.
     */
    public MaschineJamColorManager ()
    {
        this.registerColorIndex (ColorManager.BUTTON_STATE_OFF, 0);
        this.registerColorIndex (ColorManager.BUTTON_STATE_ON, 0);
        this.registerColorIndex (ColorManager.BUTTON_STATE_HI, 127);

        this.registerColorIndex (Scales.SCALE_COLOR_OFF, COLOR_BLACK);
        this.registerColorIndex (Scales.SCALE_COLOR_OCTAVE, COLOR_BLUE);
        this.registerColorIndex (Scales.SCALE_COLOR_NOTE, COLOR_WHITE);
        this.registerColorIndex (Scales.SCALE_COLOR_OUT_OF_SCALE, COLOR_BLACK);

        this.registerColorIndex (AbstractMode.BUTTON_COLOR_OFF, 0);
        this.registerColorIndex (AbstractMode.BUTTON_COLOR_ON, 0);
        this.registerColorIndex (AbstractMode.BUTTON_COLOR_HI, 127);

        this.registerColorIndex (AbstractPlayView.COLOR_PLAY, COLOR_GREEN);
        this.registerColorIndex (AbstractPlayView.COLOR_RECORD, COLOR_RED);
        this.registerColorIndex (AbstractPlayView.COLOR_OFF, COLOR_BLACK);

        this.registerColorIndex (AbstractDrumView.COLOR_PAD_OFF, COLOR_BLACK);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_RECORD, COLOR_RED);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_PLAY, COLOR_GREEN);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_SELECTED, COLOR_BLUE);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_MUTED, COLOR_AMBER);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_HAS_CONTENT, COLOR_GREY);
        this.registerColorIndex (AbstractDrumView.COLOR_PAD_NO_CONTENT, COLOR_BLACK);

        this.registerColorIndex (DAWColor.COLOR_OFF, COLOR_BLACK);
        this.registerColorIndex (DAWColor.DAW_COLOR_GRAY_HALF, COLOR_GREY);
        this.registerColorIndex (DAWColor.DAW_COLOR_DARK_GRAY, COLOR_GREY);
        this.registerColorIndex (DAWColor.DAW_COLOR_GRAY, COLOR_GREY);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_GRAY, COLOR_GREY);
        this.registerColorIndex (DAWColor.DAW_COLOR_SILVER, COLOR_GREY);
        this.registerColorIndex (DAWColor.DAW_COLOR_DARK_BROWN, COLOR_AMBER_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_BROWN, COLOR_AMBER_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_DARK_BLUE, COLOR_BLUE_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_PURPLE_BLUE, COLOR_PURPLE_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_PURPLE, COLOR_PURPLE);
        this.registerColorIndex (DAWColor.DAW_COLOR_PINK, COLOR_PINK);
        this.registerColorIndex (DAWColor.DAW_COLOR_RED, COLOR_RED);
        this.registerColorIndex (DAWColor.DAW_COLOR_ORANGE, COLOR_ORANGE);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_ORANGE, COLOR_YELLOW);
        this.registerColorIndex (DAWColor.DAW_COLOR_MOSS_GREEN, COLOR_LIME_LO);
        //this.registerColorIndex (DAWColor.DAW_COLOR_GREEN, COLOR_SPRING);
        this.registerColorIndex (DAWColor.DAW_COLOR_GREEN, COLOR_GREEN);
        this.registerColorIndex (DAWColor.DAW_COLOR_COLD_GREEN, COLOR_TURQUOISE);
        this.registerColorIndex (DAWColor.DAW_COLOR_BLUE, COLOR_BLUE);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_PURPLE, COLOR_MAGENTA);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_PINK, COLOR_PINK);
        this.registerColorIndex (DAWColor.DAW_COLOR_SKIN, COLOR_SKIN);
        this.registerColorIndex (DAWColor.DAW_COLOR_REDDISH_BROWN, COLOR_AMBER);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_BROWN, COLOR_AMBER_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_GREEN, COLOR_SPRING);
        this.registerColorIndex (DAWColor.DAW_COLOR_BLUISH_GREEN, COLOR_LIME);
        this.registerColorIndex (DAWColor.DAW_COLOR_GREEN_BLUE, COLOR_TURQUOISE_LO);
        this.registerColorIndex (DAWColor.DAW_COLOR_LIGHT_BLUE, COLOR_SKY);

        this.registerColor (COLOR_BLACK, ColorEx.BLACK);
        this.registerColor (COLOR_GREY, ColorEx.GRAY);
        this.registerColor (COLOR_WHITE, ColorEx.WHITE);
        this.registerColor (COLOR_ROSE, DAWColor.DAW_COLOR_SKIN.getColor ());
        this.registerColor (COLOR_RED, DAWColor.DAW_COLOR_REDDISH_BROWN.getColor ());
        this.registerColor (COLOR_RED_LO, ColorEx.fromRGB (39, 4, 1));
        this.registerColor (COLOR_AMBER, DAWColor.DAW_COLOR_REDDISH_BROWN.getColor ());
        this.registerColor (COLOR_AMBER_LO, DAWColor.DAW_COLOR_DARK_BROWN.getColor ());
        this.registerColor (COLOR_YELLOW, ColorEx.fromRGB (107, 105, 1));
        this.registerColor (COLOR_LIME, ColorEx.fromRGB (29, 104, 1));
        this.registerColor (COLOR_LIME_LO, DAWColor.DAW_COLOR_MOSS_GREEN.getColor ());
        this.registerColor (COLOR_GREEN, ColorEx.fromRGB (1, 104, 1));
        this.registerColor (COLOR_GREEN_LO, ColorEx.fromRGB (1, 36, 1));
        this.registerColor (COLOR_SPRING, DAWColor.DAW_COLOR_GREEN.getColor ());
        this.registerColor (COLOR_SPRING_LO, ColorEx.fromRGB (1, 36, 1));
        this.registerColor (COLOR_TURQUOISE_LO, ColorEx.fromRGB (1, 248, 75));
        this.registerColor (COLOR_TURQUOISE, DAWColor.DAW_COLOR_COLD_GREEN.getColor ());
        this.registerColor (COLOR_SKY, ColorEx.fromRGB (1, 82, 100));
        this.registerColor (COLOR_SKY_LO, ColorEx.fromRGB (1, 26, 37));
        this.registerColor (COLOR_BLUE, ColorEx.fromRGB (4, 23, 110));
        this.registerColor (COLOR_BLUE_LO, ColorEx.fromRGB (1, 8, 38));
        this.registerColor (COLOR_MAGENTA, ColorEx.fromRGB (110, 28, 109));
        this.registerColor (COLOR_MAGENTA_LO, ColorEx.fromRGB (39, 9, 38));
        this.registerColor (COLOR_PINK, ColorEx.fromRGB (110, 20, 40));
        this.registerColor (COLOR_PINK_LO, ColorEx.fromRGB (48, 9, 26));
        this.registerColor (COLOR_ORANGE, DAWColor.DAW_COLOR_ORANGE.getColor ());
        this.registerColor (COLOR_ORANGE_LO, ColorEx.DARK_ORANGE);
        this.registerColor (COLOR_PURPLE, ColorEx.PURPLE);
        this.registerColor (COLOR_PURPLE_LO, ColorEx.PURPLE);
        this.registerColor (COLOR_SKIN, ColorEx.SKIN);
    }


    /** {@inheritDoc} */
    @Override
    public ColorEx getColor (final int colorIndex, final ButtonID buttonID)
    {
        if (colorIndex < 0)
            return ColorEx.BLACK;

        if (buttonID == null)
            return ColorEx.GRAY;

        switch (buttonID)
        {
            case PLAY:
                return colorIndex > 0 ? ColorEx.GREEN : ColorEx.DARK_GREEN;
            case RECORD:
                return colorIndex > 0 ? ColorEx.RED : ColorEx.DARK_RED;
            case PAD1:
            case PAD2:
            case PAD3:
            case PAD4:
            case PAD5:
            case PAD6:
            case PAD7:
            case PAD8:
            case PAD9:
            case PAD10:
            case PAD11:
            case PAD12:
            case PAD13:
            case PAD14:
            case PAD15:
            case PAD16:
                return super.getColor (colorIndex, buttonID);
            default:
                return colorIndex > 0 ? ColorEx.WHITE : ColorEx.GRAY;
        }
    }
}