// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam;

import de.mossgrabers.controller.maschine.jam.controller.MaschineJamColorManager;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.controller.maschine.jam.view.PlayView;
import de.mossgrabers.controller.maschine.jam.view.SceneView;
import de.mossgrabers.controller.maschine.mikro.mk3.command.continuous.TouchstripCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.AddDeviceCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.GridButtonCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.MaschineStopCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.ProjectButtonCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.RibbonCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.ToggleFixedVelCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.command.trigger.VolumePanSendCommand;
import de.mossgrabers.controller.maschine.mikro.mk3.mode.BrowseMode;
import de.mossgrabers.controller.maschine.mikro.mk3.mode.PositionMode;
import de.mossgrabers.controller.maschine.mikro.mk3.mode.TempoMode;
import de.mossgrabers.controller.maschine.mikro.mk3.view.ClipView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.DrumView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.MuteView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.NoteRepeatView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.ParameterView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.SelectView;
import de.mossgrabers.controller.maschine.mikro.mk3.view.SoloView;
import de.mossgrabers.framework.command.continuous.KnobRowModeCommand;
import de.mossgrabers.framework.command.core.NopCommand;
import de.mossgrabers.framework.command.trigger.BrowserCommand;
import de.mossgrabers.framework.command.trigger.application.PanelLayoutCommand;
import de.mossgrabers.framework.command.trigger.application.UndoCommand;
import de.mossgrabers.framework.command.trigger.clip.NewCommand;
import de.mossgrabers.framework.command.trigger.clip.NoteRepeatCommand;
import de.mossgrabers.framework.command.trigger.clip.QuantizeCommand;
import de.mossgrabers.framework.command.trigger.mode.ModeSelectCommand;
import de.mossgrabers.framework.command.trigger.transport.MetronomeCommand;
import de.mossgrabers.framework.command.trigger.transport.PlayCommand;
import de.mossgrabers.framework.command.trigger.transport.RecordCommand;
import de.mossgrabers.framework.command.trigger.transport.ToggleLoopCommand;
import de.mossgrabers.framework.command.trigger.transport.WriteArrangerAutomationCommand;
import de.mossgrabers.framework.command.trigger.transport.WriteClipLauncherAutomationCommand;
import de.mossgrabers.framework.command.trigger.view.ViewMultiSelectCommand;
import de.mossgrabers.framework.configuration.ISettingsUI;
import de.mossgrabers.framework.controller.AbstractControllerSetup;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.ContinuousID;
import de.mossgrabers.framework.controller.ISetupFactory;
import de.mossgrabers.framework.controller.grid.IPadGrid;
import de.mossgrabers.framework.controller.hardware.BindType;
import de.mossgrabers.framework.controller.hardware.IHwRelativeKnob;
import de.mossgrabers.framework.controller.valuechanger.DefaultValueChanger;
import de.mossgrabers.framework.daw.ICursorDevice;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.ISendBank;
import de.mossgrabers.framework.daw.ITrackBank;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.framework.daw.ModelSetup;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.IMidiAccess;
import de.mossgrabers.framework.daw.midi.IMidiInput;
import de.mossgrabers.framework.daw.midi.IMidiOutput;
import de.mossgrabers.framework.mode.Mode;
import de.mossgrabers.framework.mode.ModeManager;
import de.mossgrabers.framework.mode.Modes;
import de.mossgrabers.framework.mode.device.SelectedDeviceMode;
import de.mossgrabers.framework.mode.track.SelectedPanMode;
import de.mossgrabers.framework.mode.track.SelectedSendMode;
import de.mossgrabers.framework.mode.track.SelectedVolumeMode;
import de.mossgrabers.framework.scale.Scales;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.ViewManager;
import de.mossgrabers.framework.view.Views;


/**
 * Support for the NI Maschine Jam controller.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineJamControllerSetup extends AbstractControllerSetup<MaschineJamControlSurface, MaschineJamConfiguration>
{
    // @formatter:off
    /** The drum grid matrix. */
    private static final int [] DRUM_MATRIX =
    {
         0,  1,  2,  3,  4,  5,  6,  7,
         8,  9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23,
        24, 25, 26, 27, 28, 29, 30, 31,
        32, 33, 34, 35, 36, 37, 38, 39,
        40, 41, 42, 43, 44, 45, 46, 47,
        48, 49, 50, 51, 52, 53, 54, 55,
        56, 57, 58, 59, 60, 61, 62, 63
    };
    // @formatter:on

    private static final int NUM_OF_TRACK_BUTTONS = 8;
    private static final int NUM_OF_SENDS = 8;

    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param factory The factory
     * @param globalSettings The global settings
     * @param documentSettings The document (project) specific settings
     */
    public MaschineJamControllerSetup (final IHost host, final ISetupFactory factory, final ISettingsUI globalSettings, final ISettingsUI documentSettings)
    {
        super (factory, host, globalSettings, documentSettings);
        this.colorManager = new MaschineJamColorManager ();
        this.valueChanger = new DefaultValueChanger (128, 8, 1);
        this.configuration = new MaschineJamConfiguration (host, this.valueChanger, factory.getArpeggiatorModes ());
    }


    /** {@inheritDoc} */
    @Override
    protected void createScales ()
    {
        this.scales = new Scales (this.valueChanger, 22, 86, 8, 8);
        this.scales.setDrumMatrix (DRUM_MATRIX);
    }


    /** {@inheritDoc} */
    @Override
    protected void createModel ()
    {
        final ModelSetup ms = new ModelSetup ();
        ms.setNumTracks (NUM_OF_TRACK_BUTTONS);
        ms.setNumDevicesInBank (8);
        ms.setNumScenes (8);
        ms.setNumSends (NUM_OF_SENDS);
        ms.setNumParams (8);
        this.model = this.factory.createModel (this.colorManager, this.valueChanger, this.scales, ms);
        final ITrackBank trackBank = this.model.getTrackBank ();
        trackBank.setIndication (true);
        trackBank.addSelectionObserver ( (index, isSelected) -> this.handleTrackChange (isSelected));
    }


    /** {@inheritDoc} */
    @Override
    protected void createSurface ()
    {
        final IMidiAccess midiAccess = this.factory.createMidiAccess ();
        final IMidiOutput output = midiAccess.createOutput ();
        final IMidiInput input = midiAccess.createInput ("Maschine Jam", "80????", "90????");
        this.colorManager.registerColorIndex (IPadGrid.GRID_OFF, 0);
        final MaschineJamControlSurface surface = new MaschineJamControlSurface (this.host, this.colorManager, this.configuration, output, input);
        this.surfaces.add (surface);
    }


    /** {@inheritDoc} */
    @Override
    protected void createObservers ()
    {
        final MaschineJamControlSurface surface = this.getSurface ();

        surface.getViewManager ().addViewChangeListener ( (previousViewId, activeViewId) -> this.updateMode (null));
        surface.getModeManager ().addModeListener ( (previousModeId, activeModeId) -> this.updateMode (activeModeId));

        this.createScaleObservers (this.configuration);
        this.createNoteRepeatObservers (this.configuration, surface);
    }


    /** {@inheritDoc} */
    @Override
    protected void createModes ()
    {
        final MaschineJamControlSurface surface = this.getSurface ();
        final ModeManager modeManager = surface.getModeManager ();

        modeManager.registerMode (Modes.VOLUME, new SelectedVolumeMode<> (surface, this.model));
        
        modeManager.setDefaultMode (Modes.VOLUME);
        
        /*
        modeManager.registerMode (Modes.BROWSER, new BrowseMode (surface, this.model));

        modeManager.registerMode (Modes.PAN, new SelectedPanMode<> (surface, this.model));
        for (int i = 0; i < 8; i++)
            modeManager.registerMode (Modes.get (Modes.SEND1, i), new SelectedSendMode<> (i, surface, this.model));

        modeManager.registerMode (Modes.POSITION, new PositionMode (surface, this.model));
        modeManager.registerMode (Modes.TEMPO, new TempoMode (surface, this.model));

        modeManager.registerMode (Modes.DEVICE_PARAMS, new SelectedDeviceMode<> (surface, this.model));

        */
    }


    /** {@inheritDoc} */
    @Override
    protected void createViews ()
    {
        final MaschineJamControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();

        viewManager.registerView (Views.PLAY, new PlayView (surface, this.model));
        viewManager.registerView (Views.SCENE_PLAY, new SceneView (surface, this.model));
        
        /*
        viewManager.registerView (Views.CLIP, new ClipView (surface, this.model));

        viewManager.registerView (Views.DRUM, new DrumView (surface, this.model));

        viewManager.registerView (Views.DEVICE, new ParameterView (surface, this.model));

        viewManager.registerView (Views.TRACK_SELECT, new SelectView (surface, this.model));
        viewManager.registerView (Views.TRACK_SOLO, new SoloView (surface, this.model));
        viewManager.registerView (Views.TRACK_MUTE, new MuteView (surface, this.model));

        viewManager.registerView (Views.REPEAT_NOTE, new NoteRepeatView (surface, this.model));
        */
    }


    /** {@inheritDoc} */
    @Override
    protected void registerTriggerCommands ()
    {
        final MaschineJamControlSurface surface = this.getSurface ();
        final ModeManager modeManager = surface.getModeManager ();
        final ViewManager viewManager = this.getSurface ().getViewManager ();
        final ITransport t = this.model.getTransport ();

        // Transport
        this.addButton (ButtonID.PLAY, "Play", new PlayCommand<> (this.model, surface), MaschineJamControlSurface.JAM_PLAY, t::isPlaying);
        this.addButton (ButtonID.RECORD, "Record", new RecordCommand<> (this.model, surface), MaschineJamControlSurface.JAM_REC, t::isRecording);
        /*
        this.addButton (ButtonID.STOP, "Stop", new MaschineStopCommand (this.model, surface), MaschineJamControlSurface.JAM_STOP, () -> !t.isPlaying ());
        this.addButton (ButtonID.LOOP, "Loop", new ToggleLoopCommand<> (this.model, surface), MaschineJamControlSurface.JAM_RESTART, t::isLoop);
        this.addButton (ButtonID.DELETE, "Erase", new UndoCommand<> (this.model, surface), MaschineJamControlSurface.JAM_ERASE);
        this.addButton (ButtonID.METRONOME, "Metronome", new MetronomeCommand<> (this.model, surface), MaschineJamControlSurface.JAM_METRO, t::isMetronomeOn);
        this.addButton (ButtonID.QUANTIZE, "Quantize", new QuantizeCommand<> (this.model, surface), MaschineMikroMk3ControlSurface.JAM_FOLLOW);

        // Automation
        this.addButton (ButtonID.NEW, "New", new NewCommand<> (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_GROUP);
        this.addButton (ButtonID.AUTOMATION, "Automation", new WriteClipLauncherAutomationCommand<> (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_AUTO, t::isWritingClipLauncherAutomation);
        this.addButton (ButtonID.AUTOMATION_WRITE, "Write", new WriteArrangerAutomationCommand<> (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_LOCK, t::isWritingArrangerAutomation);
        this.addButton (ButtonID.REPEAT, "Repeat", new NoteRepeatCommand<> (this.model, surface, false), MaschineMikroMk3ControlSurface.MIKRO_3_NOTE_REPEAT, this.configuration::isNoteRepeatActive);

        // Ribbon
        this.addButton (ButtonID.F1, "Pitch", new RibbonCommand (this.model, surface, MaschineJamConfiguration.RIBBON_MODE_PITCH_DOWN, MaschineJamConfiguration.RIBBON_MODE_PITCH_UP, MaschineJamConfiguration.RIBBON_MODE_PITCH_DOWN_UP), MaschineMikroMk3ControlSurface.MIKRO_3_PITCH, () -> this.isRibbonMode (MaschineJamConfiguration.RIBBON_MODE_PITCH_DOWN, MaschineJamConfiguration.RIBBON_MODE_PITCH_DOWN_UP, MaschineJamConfiguration.RIBBON_MODE_PITCH_UP));
        this.addButton (ButtonID.F2, "Mod", new RibbonCommand (this.model, surface, MaschineJamConfiguration.RIBBON_MODE_CC_1, MaschineJamConfiguration.RIBBON_MODE_CC_11), MaschineMikroMk3ControlSurface.MIKRO_3_MOD, () -> this.isRibbonMode (MaschineJamConfiguration.RIBBON_MODE_CC_1, MaschineJamConfiguration.RIBBON_MODE_CC_11));
        this.addButton (ButtonID.F3, "Perform", new RibbonCommand (this.model, surface, MaschineJamConfiguration.RIBBON_MODE_MASTER_VOLUME), MaschineMikroMk3ControlSurface.MIKRO_3_PERFORM, () -> this.isRibbonMode (MaschineJamConfiguration.RIBBON_MODE_MASTER_VOLUME));
        this.addButton (ButtonID.F4, "Notes", new RibbonCommand (this.model, surface, MaschineJamConfiguration.RIBBON_MODE_NOTE_REPEAT_PERIOD, MaschineJamConfiguration.RIBBON_MODE_NOTE_REPEAT_LENGTH), MaschineMikroMk3ControlSurface.MIKRO_3_NOTES, () -> this.isRibbonMode (MaschineJamConfiguration.RIBBON_MODE_NOTE_REPEAT_PERIOD, MaschineJamConfiguration.RIBBON_MODE_NOTE_REPEAT_LENGTH));

        this.addButton (ButtonID.FADER_TOUCH_1, "Encoder Press", (event, velocity) -> {

            if (event != ButtonEvent.DOWN)
                return;

            switch (modeManager.getActiveOrTempModeId ())
            {
                case TEMPO:
                    t.tapTempo ();
                    break;

                case BROWSER:
                    this.model.getBrowser ().stopBrowsing (true);
                    modeManager.restoreMode ();
                    break;

                default:
                    this.valueChanger.setSpeed (!this.valueChanger.isSlow ());
                    break;
            }

        }, MaschineMikroMk3ControlSurface.MIKRO_3_ENCODER_PUSH);

        // Encoder Modes
        this.addButton (ButtonID.VOLUME, "Volume", new VolumePanSendCommand (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_VOLUME, () -> Modes.isTrackMode (modeManager.getActiveOrTempModeId ()));
        this.addButton (ButtonID.TAP_TEMPO, "Swing", new ModeSelectCommand<> (this.model, surface, Modes.POSITION), MaschineMikroMk3ControlSurface.MIKRO_3_SWING, () -> modeManager.isActiveOrTempMode (Modes.POSITION));
        this.addButton (ButtonID.USER, "Tempo", new ModeSelectCommand<> (this.model, surface, Modes.TEMPO), MaschineMikroMk3ControlSurface.MIKRO_3_TEMPO, () -> modeManager.isActiveOrTempMode (Modes.TEMPO));
        this.addButton (ButtonID.DEVICE, "Plugin", new ModeSelectCommand<> (this.model, surface, Modes.DEVICE_PARAMS), MaschineMikroMk3ControlSurface.MIKRO_3_PLUGIN, () -> modeManager.isActiveMode (Modes.DEVICE_PARAMS));
        this.addButton (ButtonID.DEVICE_ON_OFF, "Sampling", new PanelLayoutCommand<> (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_SAMPLING, this.model.getCursorDevice ()::isWindowOpen);

        // Browser
        this.addButton (ButtonID.ADD_TRACK, "Project", new ProjectButtonCommand (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_PROJECT);
        this.addButton (ButtonID.ADD_EFFECT, "Favorites", new AddDeviceCommand (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_FAVORITES);
        this.addButton (ButtonID.BROWSE, "Browser", new BrowserCommand<> (Modes.BROWSER, this.model, surface)
        {
            @Override
            protected boolean getCommit ()
            {
                // Discard browser, confirmation is via encoder
                return false;
            }
        }, MaschineMikroMk3ControlSurface.MIKRO_3_BROWSER, this.model.getBrowser ()::isActive);

        // Pad modes
        this.addButton (ButtonID.ACCENT, "Accent", new ToggleFixedVelCommand (this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_FIXED_VEL, this.configuration::isAccentActive);

        this.addButton (ButtonID.SCENE1, "Scene", new ViewMultiSelectCommand<> (this.model, surface, true, Views.SCENE_PLAY), MaschineMikroMk3ControlSurface.MIKRO_3_SCENE, () -> viewManager.isActiveView (Views.SCENE_PLAY));
        this.addButton (ButtonID.CLIP, "Pattern", new ViewMultiSelectCommand<> (this.model, surface, true, Views.CLIP), MaschineMikroMk3ControlSurface.MIKRO_3_PATTERN, () -> viewManager.isActiveView (Views.CLIP));
        this.addButton (ButtonID.NOTE, "Events", new ViewMultiSelectCommand<> (this.model, surface, true, Views.PLAY, Views.DRUM), MaschineMikroMk3ControlSurface.MIKRO_3_EVENTS, () -> viewManager.isActiveView (Views.PLAY) || viewManager.isActiveView (Views.DRUM));
        this.addButton (ButtonID.TOGGLE_DEVICE, "Variation", new ViewMultiSelectCommand<> (this.model, surface, true, Views.DEVICE), MaschineMikroMk3ControlSurface.MIKRO_3_VARIATION, () -> viewManager.isActiveView (Views.DEVICE));
        this.addButton (ButtonID.DUPLICATE, "Duplicate", NopCommand.INSTANCE, MaschineMikroMk3ControlSurface.MIKRO_3_DUPLICATE);

        this.addButton (ButtonID.TRACK, "Select", new ViewMultiSelectCommand<> (this.model, surface, true, Views.TRACK_SELECT), MaschineMikroMk3ControlSurface.MIKRO_3_SELECT, () -> viewManager.isActiveView (Views.TRACK_SELECT));
        this.addButton (ButtonID.SOLO, "Solo", new ViewMultiSelectCommand<> (this.model, surface, true, Views.TRACK_SOLO), MaschineMikroMk3ControlSurface.MIKRO_3_SOLO, () -> viewManager.isActiveView (Views.TRACK_SOLO));
        this.addButton (ButtonID.MUTE, "Mute", new ViewMultiSelectCommand<> (this.model, surface, true, Views.TRACK_MUTE), MaschineMikroMk3ControlSurface.MIKRO_3_MUTE, () -> viewManager.isActiveView (Views.TRACK_MUTE));

        this.addButton (ButtonID.ROW1_1, "Mode", new GridButtonCommand (0, this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_PAD_MODE);
        this.addButton (ButtonID.ROW1_2, "Keyboard", new GridButtonCommand (1, this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_KEYBOARD);
        this.addButton (ButtonID.ROW1_3, "Chords", new GridButtonCommand (2, this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_CHORDS);
        this.addButton (ButtonID.ROW1_4, "Step", new GridButtonCommand (3, this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_STEP);
        */
    }


    /** {@inheritDoc} */
    @Override
    protected void registerContinuousCommands ()
    {
    	/*
        final MaschineMikroMk3ControlSurface surface = this.getSurface ();
        final ModeManager modeManager = surface.getModeManager ();

        final IHwRelativeKnob knob = this.addRelativeKnob (ContinuousID.KNOB1, "Encoder", new KnobRowModeCommand<> (0, this.model, surface), MaschineMikroMk3ControlSurface.MIKRO_3_ENCODER);
        knob.bindTouch ( (event, velocity) -> {
            final Mode mode = modeManager.getActiveOrTempMode ();
            if (mode != null)
                mode.onKnobTouch (0, event == ButtonEvent.DOWN);
        }, surface.getMidiInput (), BindType.CC, MaschineMikroMk3ControlSurface.MIKRO_3_ENCODER_TOUCH);

        final TouchstripCommand touchstripCommand = new TouchstripCommand (this.model, surface);
        this.addFader (ContinuousID.CROSSFADER, "Touchstrip", touchstripCommand, BindType.CC, MaschineMikroMk3ControlSurface.MIKRO_3_TOUCHSTRIP, false);
        surface.getContinuous (ContinuousID.CROSSFADER).bindTouch (touchstripCommand, surface.getMidiInput (), BindType.CC, MaschineMikroMk3ControlSurface.MIKRO_3_TOUCHSTRIP_TOUCH);
        */
    }


    /** {@inheritDoc} */
    @Override
    protected void layoutControls ()
    {
    	/*
        final MaschineMikroMk3ControlSurface surface = this.getSurface ();

        surface.getButton (ButtonID.PAD1).setBounds (427.0, 336.0, 76.25, 79.0);
        surface.getButton (ButtonID.PAD2).setBounds (517.75, 336.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD3).setBounds (608.25, 336.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD4).setBounds (696.25, 336.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD5).setBounds (427.0, 246.5, 76.25, 79.0);
        surface.getButton (ButtonID.PAD6).setBounds (517.75, 247.0, 76.25, 79.0);
        surface.getButton (ButtonID.PAD7).setBounds (608.25, 247.25, 76.25, 79.0);
        surface.getButton (ButtonID.PAD8).setBounds (696.25, 247.0, 76.25, 79.0);
        surface.getButton (ButtonID.PAD9).setBounds (427.0, 156.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD10).setBounds (517.75, 157.5, 76.25, 79.0);
        surface.getButton (ButtonID.PAD11).setBounds (608.25, 157.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD12).setBounds (696.25, 157.0, 76.25, 79.0);
        surface.getButton (ButtonID.PAD13).setBounds (427.0, 67.25, 76.25, 79.0);
        surface.getButton (ButtonID.PAD14).setBounds (517.75, 67.75, 76.25, 79.0);
        surface.getButton (ButtonID.PAD15).setBounds (608.25, 68.25, 76.25, 79.0);
        surface.getButton (ButtonID.PAD16).setBounds (696.25, 67.25, 76.25, 79.0);
        surface.getButton (ButtonID.PLAY).setBounds (26.25, 381.5, 55.75, 32.0);
        surface.getButton (ButtonID.RECORD).setBounds (98.25, 381.5, 55.75, 32.0);
        surface.getButton (ButtonID.STOP).setBounds (167.75, 381.5, 55.75, 32.0);
        surface.getButton (ButtonID.LOOP).setBounds (26.25, 350.5, 56.0, 18.0);
        surface.getButton (ButtonID.DELETE).setBounds (98.25, 350.5, 56.0, 18.0);
        surface.getButton (ButtonID.METRONOME).setBounds (167.75, 350.5, 56.0, 18.0);
        surface.getButton (ButtonID.QUANTIZE).setBounds (238.0, 350.5, 56.0, 18.0);
        surface.getButton (ButtonID.NEW).setBounds (26.25, 280.5, 55.75, 32.0);
        surface.getButton (ButtonID.AUTOMATION).setBounds (98.25, 280.5, 55.75, 32.0);
        surface.getButton (ButtonID.AUTOMATION_WRITE).setBounds (167.75, 280.5, 55.75, 32.0);
        surface.getButton (ButtonID.REPEAT).setBounds (238.0, 280.5, 55.75, 32.0);
        surface.getButton (ButtonID.F1).setBounds (26.25, 170.0, 58.0, 19.0);
        surface.getButton (ButtonID.F2).setBounds (98.25, 170.75, 57.25, 18.25);
        surface.getButton (ButtonID.F3).setBounds (167.75, 170.75, 57.25, 18.25);
        surface.getButton (ButtonID.F4).setBounds (238.0, 170.75, 57.25, 18.25);
        surface.getButton (ButtonID.FADER_TOUCH_1).setBounds (74.0, 21.25, 69.5, 22.75);
        surface.getButton (ButtonID.VOLUME).setBounds (166.0, 25.75, 58.0, 19.0);
        surface.getButton (ButtonID.TAP_TEMPO).setBounds (166.0, 56.75, 58.0, 19.0);
        surface.getButton (ButtonID.USER).setBounds (166.0, 85.75, 58.0, 19.0);
        surface.getButton (ButtonID.DEVICE).setBounds (238.0, 25.75, 58.0, 19.0);
        surface.getButton (ButtonID.DEVICE_ON_OFF).setBounds (238.0, 56.75, 58.0, 19.0);
        surface.getButton (ButtonID.ADD_TRACK).setBounds (26.25, 22.75, 25.5, 25.0);
        surface.getButton (ButtonID.ADD_EFFECT).setBounds (26.25, 51.75, 25.5, 25.0);
        surface.getButton (ButtonID.BROWSE).setBounds (26.25, 80.75, 25.5, 25.0);
        surface.getButton (ButtonID.ACCENT).setBounds (346.25, 25.75, 58.0, 20.75);
        surface.getButton (ButtonID.SCENE1).setBounds (348.0, 67.0, 58.0, 34.0);
        surface.getButton (ButtonID.CLIP).setBounds (348.0, 110.75, 58.0, 34.0);
        surface.getButton (ButtonID.NOTE).setBounds (348.0, 154.5, 58.0, 34.0);
        surface.getButton (ButtonID.TOGGLE_DEVICE).setBounds (348.0, 198.25, 58.0, 41.25);
        surface.getButton (ButtonID.DUPLICATE).setBounds (348.75, 246.5, 58.0, 34.0);
        surface.getButton (ButtonID.TRACK).setBounds (348.0, 293.0, 58.0, 34.0);
        surface.getButton (ButtonID.SOLO).setBounds (348.0, 336.75, 58.0, 34.0);
        surface.getButton (ButtonID.MUTE).setBounds (348.0, 380.5, 58.0, 34.0);
        surface.getButton (ButtonID.ROW1_1).setBounds (427.0, 25.75, 78.0, 20.75);
        surface.getButton (ButtonID.ROW1_2).setBounds (517.75, 25.75, 78.0, 20.75);
        surface.getButton (ButtonID.ROW1_3).setBounds (608.25, 25.75, 78.0, 20.75);
        surface.getButton (ButtonID.ROW1_4).setBounds (696.25, 25.75, 78.0, 20.75);

        surface.getContinuous (ContinuousID.KNOB1).setBounds (75.0, 50.0, 64.0, 63.0);
        surface.getContinuous (ContinuousID.CROSSFADER).setBounds (26.25, 204.0, 268.0, 50.0);
        */
    }


    /** {@inheritDoc} */
    @Override
    public void startup ()
    {
        final MaschineJamControlSurface surface = this.getSurface ();
        surface.getModeManager ().setActiveMode (Modes.VOLUME);
        surface.getViewManager ().setActiveView (Views.PLAY);
    }


    /** {@inheritDoc} */
    @Override
    public void flush ()
    {
        super.flush ();

        /* 
         * Caused a null pointer java.lang.NullPointerException
    at de.mossgrabers.controller.maschine.jam.MaschineJamControllerSetup.flush(MaschineJamControllerSetup.java:421)
    at de.mossgrabers.bitwig.framework.extension.GenericControllerExtension.flush(GenericControllerExtension.java:63)
        final TouchstripCommand command = (TouchstripCommand) this.getSurface ().getContinuous (ContinuousID.CROSSFADER).getCommand ();
        if (command != null)
            command.updateValue ();
        */
    }


    private void updateMode (final Modes mode)
    {
        final Modes m = mode == null ? this.getSurface ().getModeManager ().getActiveOrTempModeId () : mode;
        if (this.currentMode != null && this.currentMode.equals (m))
            return;
        this.currentMode = m;
        this.updateIndication (m);
    }


    /** {@inheritDoc} */
    @Override
    protected void updateIndication (final Modes mode)
    {
        final ITrackBank tb = this.model.getTrackBank ();
        final ITrackBank tbe = this.model.getEffectTrackBank ();
        final MaschineJamControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();

        final boolean isSession = viewManager.isActiveView (Views.SCENE_PLAY) || viewManager.isActiveView (Views.CLIP);
        final boolean isEffect = this.model.isEffectTrackBankActive ();

        final boolean isVolume = Modes.VOLUME.equals (mode);
        final boolean isPan = Modes.PAN.equals (mode);

        tb.setIndication (!isEffect && isSession);
        if (tbe != null)
            tbe.setIndication (isEffect && isSession);

        final ITrack selectedTrack = tb.getSelectedItem ();
        final int selIndex = selectedTrack == null ? -1 : selectedTrack.getIndex ();

        final ICursorDevice cursorDevice = this.model.getCursorDevice ();
        for (int i = 0; i < NUM_OF_TRACK_BUTTONS; i++)
        {
            final ITrack track = tb.getItem (i);
            track.setVolumeIndication (selIndex == i && !isEffect && isVolume);
            track.setPanIndication (selIndex == i && !isEffect && isPan);
            final ISendBank sendBank = track.getSendBank ();
            for (int j = 0; j < NUM_OF_SENDS; j++)
                sendBank.getItem (j).setIndication (selIndex == i && !isEffect && (Modes.SEND1.equals (mode) && j == 0 || Modes.SEND2.equals (mode) && j == 1 || Modes.SEND3.equals (mode) && j == 2 || Modes.SEND4.equals (mode) && j == 3 || Modes.SEND5.equals (mode) && j == 4 || Modes.SEND6.equals (mode) && j == 5 || Modes.SEND7.equals (mode) && j == 6 || Modes.SEND8.equals (mode) && j == 7));

            if (tbe != null)
            {
                final ITrack fxTrack = tbe.getItem (i);
                fxTrack.setVolumeIndication (selIndex == i && isEffect && isVolume);
                fxTrack.setPanIndication (selIndex == i && isEffect && isPan);
            }

            cursorDevice.getParameterBank ().getItem (i).setIndication (true);
        }
    }


    /**
     * Handle a track selection change.
     *
     * @param isSelected Has the track been selected?
     */
    private void handleTrackChange (final boolean isSelected)
    {
        if (!isSelected)
            return;

        final MaschineJamControlSurface surface = this.getSurface ();
        final ViewManager viewManager = surface.getViewManager ();
        if (viewManager.isActiveView (Views.PLAY))
            viewManager.getActiveView ().updateNoteMapping ();

        // Reset drum octave because the drum pad bank is also reset
        this.scales.resetDrumOctave ();
        if (viewManager.isActiveView (Views.DRUM))
            viewManager.getView (Views.DRUM).updateNoteMapping ();

        this.updateIndication (this.currentMode);
    }


    private boolean isRibbonMode (int... modes)
    {
        final int ribbonMode = this.configuration.getRibbonMode ();
        for (int i = 0; i < modes.length; i++)
        {
            if (ribbonMode == modes[i])
                return true;
        }
        return false;
    }
}
