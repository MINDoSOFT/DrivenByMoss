// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam;

import de.mossgrabers.framework.controller.DefaultControllerDefinition;
import de.mossgrabers.framework.utils.OperatingSystem;
import de.mossgrabers.framework.utils.Pair;

import java.util.List;
import java.util.UUID;


/**
 * Definition class for the NI Maschine Jam controller extension.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineJamControllerDefinition extends DefaultControllerDefinition
{
    private static final UUID EXTENSION_ID = UUID.fromString ("63AC1E45-B59D-4817-9F7B-8704559B4D27");


    /**
     * Constructor.
     */
    public MaschineJamControllerDefinition ()
    {
        super (EXTENSION_ID, "Maschine Jam", "Native Instruments", 1, 1);
    }


    /** {@inheritDoc} */
    @Override
    public List<Pair<String [], String []>> getMidiDiscoveryPairs (final OperatingSystem os)
    {
        return this.createDeviceDiscoveryPairs ("Maschine Jam");
    }
}
