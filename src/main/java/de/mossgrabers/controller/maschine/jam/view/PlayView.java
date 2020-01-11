// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.view;

import de.mossgrabers.controller.maschine.jam.MaschineJamConfiguration;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.configuration.AbstractConfiguration;
import de.mossgrabers.framework.configuration.Configuration;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractPlayView;


/**
 * The Play view.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class PlayView extends AbstractPlayView<MaschineJamControlSurface, MaschineJamConfiguration> implements PadButtons
{
    /**
     * Constructor.
     *
     * @param surface The controller
     * @param model The model
     */
    public PlayView (final MaschineJamControlSurface surface, final IModel model)
    {
        super (surface, model, true);

        final Configuration configuration = this.surface.getConfiguration ();
        configuration.addSettingObserver (AbstractConfiguration.ACTIVATE_FIXED_ACCENT, this::initMaxVelocity);
        configuration.addSettingObserver (AbstractConfiguration.FIXED_ACCENT_VALUE, this::initMaxVelocity);
    }


    /** {@inheritDoc} */
    @Override
    public void onButton (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        switch (index)
        {
            case 0:
                this.scales.prevScale ();
                this.updateScale ();
                break;
            case 1:
                this.scales.nextScale ();
                this.updateScale ();
                break;
            case 2:
                this.onOctaveDown (event);
                break;
            case 3:
                this.onOctaveUp (event);
                break;
            default:
                // Not used
                break;
        }
    }


    protected void updateScale ()
    {
        this.surface.getDisplay ().notify (this.scales.getScale ().getName ());
        this.updateNoteMapping ();
        final MaschineJamConfiguration config = this.surface.getConfiguration ();
        config.setScale (this.scales.getScale ().getName ());
    }
}