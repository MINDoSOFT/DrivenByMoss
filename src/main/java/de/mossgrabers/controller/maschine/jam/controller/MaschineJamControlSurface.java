// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.controller;

import de.mossgrabers.controller.maschine.jam.MaschineJamConfiguration;
import de.mossgrabers.controller.push.controller.PaletteEntry;
import de.mossgrabers.framework.controller.AbstractControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.controller.grid.PadGridImpl;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.midi.DeviceInquiry;
import de.mossgrabers.framework.daw.midi.IMidiInput;
import de.mossgrabers.framework.daw.midi.IMidiOutput;
import de.mossgrabers.framework.daw.midi.MidiSysExCallback;
import de.mossgrabers.framework.utils.StringUtils;


/**
 * The NI Maschine Jam control surface.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
@SuppressWarnings("javadoc")
public class MaschineJamControlSurface extends AbstractControlSurface<MaschineJamConfiguration>
{
    // Midi CC
	// Transport section
	public static final int JAM_PLAY = 108;
	public static final int JAM_REC = 109;
	public static final int JAM_LEFT = 107;
	public static final int JAM_RIGHT = 104;
	public static final int JAM_TEMPO = 110;
	public static final int JAM_GRID = 113;
	public static final int JAM_SOLO = 111;
	public static final int JAM_MUTE = 112;

	// Encoder buttons
	public static final int JAM_MACRO = 90;
	public static final int JAM_LEVEL = 91;
	public static final int JAM_AUX = 92;
	public static final int JAM_CONTROL = 97;
	public static final int JAM_AUTO = 98;

	// Main buttons
	public static final int JAM_ARRANGE = 30;
	public static final int JAM_STEP = 31;
	public static final int JAM_PAD = 32;
	public static final int JAM_CLEAR = 95;
	public static final int JAM_DUPLICATE = 96;
	public static final int JAM_NOTE_REPEAT = 94;
	public static final int JAM_SELECT = 80;
	public static final int JAM_BROWSE = 44;
	public static final int JAM_LOCK = 47;
	public static final int JAM_IN_BUTTON = 62;
	public static final int JAM_PERFORM = 45;
	public static final int JAM_SWING = 49;
	public static final int JAM_TUNE = 48;

	// Master section
	public static final int JAM_MST = 60;
	public static final int JAM_GRP = 61;
	public static final int JAM_CUE = 63;
	public static final int JAM_ENCODER = 86;
	public static final int JAM_ENCODER_BUTTON = 87;
	public static final int JAM_ENCODER_TOUCH = 88;
	
	// Direction Pad
	public static final int JAM_DPAD_TOP = 40;
	public static final int JAM_DPAD_LEFT = 42;
	public static final int JAM_DPAD_RIGHT = 43;
	public static final int JAM_DPAD_DOWN = 41;

	
	// Control Groups
	public static final int JAM_BUTTONMATRIX = 22;
	public static final int JAM_SCENE_ROW = 0;  // These are Channel 2
	public static final int JAM_GROUP_ROW = 8;  // These are Channel 2
	public static final int JAM_ENCODERS = 8;
	public static final int JAM_ENCODERS_TOUCH = 20;

	// MIDI
	public static final int JAM_CC = 11;
	public static final int JAM_NOTE = 9;


    private int             ribbonValue              = -1;
    private boolean 		jamShiftValue = false;

    
    private static final int []    SYSEX_SHIFT_ON                  =
        {
                0xF0,
                0x00,
                0x21,
                0x09,
                0x15,
                0x00,
                0x4D,
                0x50,
                0x00,
                0x01,
                0x4D,
                0x01,
                0xF7
        };
    private static final int []    SYSEX_SHIFT_OFF                  =
        {
                0xF0,
                0x00,
                0x21,
                0x09,
                0x15,
                0x00,
                0x4D,
                0x50,
                0x00,
                0x01,
                0x4D,
                0x00,
                0xF7
        };
    // Don't know what button sends this sysex is but should trigger mode and controls update
    private static final int []    SYSEX_UNKNOWN                  =
        {
            0xF0,
            0x00,
            0x21,
            0x09,
            0x15,
            0x00,
            0x4D,
            0x50,
            0x00,
            0x01,
            0x46,
            0x01,
            0xF7
        };

    /**
     * Constructor.
     *
     * @param host The host
     * @param colorManager The color manager
     * @param configuration The configuration
     * @param output The midi output
     * @param input The midi input
     */
    public MaschineJamControlSurface (final IHost host, final ColorManager colorManager, final MaschineJamConfiguration configuration, final IMidiOutput output, final IMidiInput input)
    {
        super (host, configuration, colorManager, output, input, new PadGridImpl (colorManager, output, 8, 8, 22), 800, 800);
     
        this.host.println("HandleSysEx callback registered");
        this.input.setSysexCallback (this::handleSysEx);
    }
    

    /** {@inheritDoc} */
    @Override
    public void setTrigger (final int channel, final int cc, final int state)
    {
        this.output.sendCCEx (channel, cc, state);
    }


    /**
     * Set the display value of the ribbon on the controller.
     *
     * @param value The value to set
     */
    public void setRibbonValue (final int value)
    {
        if (this.ribbonValue == value)
            return;
        this.ribbonValue = value;
        this.output.sendCC (1, value);
    }
    
    /**
     * Handle incoming sysex data.
     *
     * @param data The data
     */
    private void handleSysEx (final String data)
    {
    	this.host.println("HandleSysEx");
        final int [] byteData = StringUtils.fromHexStr (data);
        this.host.println("Sysex: " + data);

        if (isShiftOn(byteData)) this.jamShiftValue = true;
        if (isShiftOff(byteData)) this.jamShiftValue = false;
        if (isUnknownOn(byteData)) ; // FIXME: Trigger a refresh ?
    }
    
    private static boolean isShiftOn (final int [] data)
    {
        return isSysex(data, SYSEX_SHIFT_ON);
    }
    
    private static boolean isShiftOff (final int [] data)
    {
        return isSysex(data, SYSEX_SHIFT_OFF);
    }
    
    private static boolean isUnknownOn (final int [] data)
    {
        return isSysex(data, SYSEX_UNKNOWN);
    }
    
    private static boolean isSysex (final int [] data, final int [] sysex)
    {
        if (data.length + 1 < sysex.length)
            return false;

        for (int i = 0; i < sysex.length; i++)
        {
            if (sysex[i] != data[i])
                return false;
        }

        return true;
    }
    
    public boolean isJamShiftPressed ()
    {
        return this.jamShiftValue;
    }
}