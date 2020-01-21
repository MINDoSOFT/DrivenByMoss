// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.mode;

import de.mossgrabers.controller.maschine.jam.MaschineJamConfiguration;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.mode.AbstractMode;


/**
 * Base class for all Maschine Jam modes.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class BaseMode extends AbstractMode<MaschineJamControlSurface, MaschineJamConfiguration>
{
    /**
     * Constructor.
     *
     * @param name The name of the mode
     * @param surface The control surface
     * @param model The model
     */
    public BaseMode (final String name, final MaschineJamControlSurface surface, final IModel model)
    {
        super (name, surface, model, false);
    }
}
