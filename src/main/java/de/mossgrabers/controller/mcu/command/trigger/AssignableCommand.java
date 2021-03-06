// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.mcu.command.trigger;

import de.mossgrabers.controller.mcu.MCUConfiguration;
import de.mossgrabers.controller.mcu.controller.MCUControlSurface;
import de.mossgrabers.framework.command.continuous.FootswitchCommand;
import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.daw.IApplication;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.mode.ModeManager;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Command for assignable functions.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class AssignableCommand extends FootswitchCommand<MCUControlSurface, MCUConfiguration>
{
    private final int          index;
    private final ModeSwitcher switcher;


    /**
     * Constructor.
     *
     * @param index The index of the assignable button
     * @param model The model
     * @param surface The surface
     */
    public AssignableCommand (final int index, final IModel model, final MCUControlSurface surface)
    {
        super (model, surface);
        this.index = index;
        this.switcher = new ModeSwitcher (surface);
    }


    /** {@inheritDoc} */
    @Override
    public void execute (final ButtonEvent event, final int velocity)
    {
        switch (this.getSetting ())
        {
            case MCUConfiguration.FOOTSWITCH_2_PREV_MODE:
                if (event == ButtonEvent.DOWN)
                    this.switcher.scrollDown ();
                break;

            case MCUConfiguration.FOOTSWITCH_2_NEXT_MODE:
                if (event == ButtonEvent.DOWN)
                    this.switcher.scrollUp ();
                break;

            case MCUConfiguration.FOOTSWITCH_2_SHOW_MARKER_MODE:
                if (event != ButtonEvent.DOWN)
                    return;
                final ModeManager modeManager = this.surface.getModeManager ();
                if (modeManager.isActiveOrTempMode (Modes.MARKERS))
                    modeManager.restoreMode ();
                else
                    modeManager.setActiveMode (Modes.MARKERS);
                break;

            case MCUConfiguration.FOOTSWITCH_2_USE_FADERS_LIKE_EDIT_KNOBS:
                if (event == ButtonEvent.DOWN)
                    this.surface.getConfiguration ().toggleUseFadersAsKnobs ();
                break;

            default:
                super.execute (event, velocity);
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    protected int getSetting ()
    {
        return this.surface.getConfiguration ().getAssignable (this.index);
    }


    /**
     * Test if the assignable is active.
     *
     * @return True if active
     */
    public boolean isActive ()
    {
        switch (this.getSetting ())
        {
            case AbstractConfiguration.FOOTSWITCH_2_TOGGLE_PLAY:
                return this.model.getTransport ().isPlaying ();

            case AbstractConfiguration.FOOTSWITCH_2_TOGGLE_RECORD:
                return this.model.getTransport ().isRecording ();

            case AbstractConfiguration.FOOTSWITCH_2_TOGGLE_CLIP_OVERDUB:
                return this.model.getTransport ().isLauncherOverdub ();

            case AbstractConfiguration.FOOTSWITCH_2_PANEL_LAYOUT_ARRANGE:
                return IApplication.PANEL_LAYOUT_ARRANGE.equals (this.model.getApplication ().getPanelLayout ());

            case AbstractConfiguration.FOOTSWITCH_2_PANEL_LAYOUT_MIX:
                return IApplication.PANEL_LAYOUT_MIX.equals (this.model.getApplication ().getPanelLayout ());

            case AbstractConfiguration.FOOTSWITCH_2_PANEL_LAYOUT_EDIT:
                return IApplication.PANEL_LAYOUT_EDIT.equals (this.model.getApplication ().getPanelLayout ());

            case MCUConfiguration.FOOTSWITCH_2_SHOW_MARKER_MODE:
                return this.surface.getModeManager ().isActiveOrTempMode (Modes.MARKERS);

            case MCUConfiguration.FOOTSWITCH_2_USE_FADERS_LIKE_EDIT_KNOBS:
                return this.surface.getConfiguration ().useFadersAsKnobs ();

            case AbstractConfiguration.FOOTSWITCH_2_UNDO:
            case AbstractConfiguration.FOOTSWITCH_2_TAP_TEMPO:
            case AbstractConfiguration.FOOTSWITCH_2_NEW_BUTTON:
            case AbstractConfiguration.FOOTSWITCH_2_CLIP_BASED_LOOPER:
            case AbstractConfiguration.FOOTSWITCH_2_STOP_ALL_CLIPS:
            case AbstractConfiguration.FOOTSWITCH_2_ADD_INSTRUMENT_TRACK:
            case AbstractConfiguration.FOOTSWITCH_2_ADD_AUDIO_TRACK:
            case AbstractConfiguration.FOOTSWITCH_2_ADD_EFFECT_TRACK:
            case AbstractConfiguration.FOOTSWITCH_2_QUANTIZE:
            case MCUConfiguration.FOOTSWITCH_2_PREV_MODE:
            case MCUConfiguration.FOOTSWITCH_2_NEXT_MODE:
            default:
                return false;
        }
    }
}
