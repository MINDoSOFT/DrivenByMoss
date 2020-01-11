// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.view;

import de.mossgrabers.controller.maschine.jam.MaschineJamConfiguration;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractView;


/**
 * Base class for views views.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class BaseView extends AbstractView<MaschineJamControlSurface, MaschineJamConfiguration> implements PadButtons
{
    /**
     * Constructor.
     *
     * @param name The name of the view
     * @param surface The controller
     * @param model The model
     */
    public BaseView (final String name, final MaschineJamControlSurface surface, final IModel model)
    {
        super (name, surface, model);
    }


    /** {@inheritDoc} */
    @Override
    public void onGridNote (final int note, final int velocity)
    {
        if (velocity > 0)
            this.executeFunction (note - 36);
    }


    /**
     * Implement to execute whatever function the view has.
     *
     * @param padIndex The index of the pressed pad (0-15)
     */
    protected abstract void executeFunction (int padIndex);


    /** {@inheritDoc} */
    @Override
    public void onButton (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        switch (index)
        {
            case 0:
                this.model.getCurrentTrackBank ().selectPreviousItem ();
                break;
            case 1:
                this.model.getCurrentTrackBank ().selectNextItem ();
                break;
            case 2:
                this.model.getCurrentTrackBank ().selectPreviousPage ();
                break;
            case 3:
                this.model.getCurrentTrackBank ().selectNextPage ();
                break;
            default:
                // Not used
                break;
        }
    }
}