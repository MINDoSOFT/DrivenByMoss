// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.bitwig.controller.maschine.mikro.mk3;

import de.mossgrabers.bitwig.framework.BitwigSetupFactory;
import de.mossgrabers.bitwig.framework.configuration.SettingsUIImpl;
import de.mossgrabers.bitwig.framework.daw.HostImpl;
import de.mossgrabers.bitwig.framework.extension.AbstractControllerExtensionDefinition;
import de.mossgrabers.controller.maschine.mikro.mk3.MaschineMikroMk3ControllerDefinition;
import de.mossgrabers.controller.maschine.mikro.mk3.MaschineMikroMk3ControllerSetup;
import de.mossgrabers.framework.controller.IControllerSetup;

import com.bitwig.extension.controller.api.ControllerHost;


/**
 * Definition class for the NI Maschine Mikro Mk3 controller.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineMikroMk3ExtensionDefinition extends AbstractControllerExtensionDefinition
{
    /**
     * Constructor.
     */
    public MaschineMikroMk3ExtensionDefinition ()
    {
        super (new MaschineMikroMk3ControllerDefinition ());
    }


    /** {@inheritDoc} */
    @Override
    protected IControllerSetup<?, ?> getControllerSetup (final ControllerHost host)
    {
        return new MaschineMikroMk3ControllerSetup (new HostImpl (host), new BitwigSetupFactory (host), new SettingsUIImpl (host.getPreferences ()), new SettingsUIImpl (host.getDocumentState ()));
    }
}
