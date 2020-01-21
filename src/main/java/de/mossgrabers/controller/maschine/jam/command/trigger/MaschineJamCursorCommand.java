// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.command.trigger;

import de.mossgrabers.controller.maschine.jam.MaschineJamConfiguration;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.daw.IBrowser;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.mode.Mode;
import de.mossgrabers.framework.mode.Modes;


/**
 * Command for cursor arrow keys.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MaschineJamCursorCommand extends de.mossgrabers.framework.command.trigger.mode.CursorCommand<MaschineJamControlSurface, MaschineJamConfiguration>
{
    /**
     * Constructor.
     *
     * @param direction The direction of the kontrol1ed cursor arrow
     * @param model The model
     * @param surface The surface
     */
    public MaschineJamCursorCommand (final Direction direction, final IModel model, final MaschineJamControlSurface surface)
    {
        super (direction, model, surface);
    }
    
    private boolean isActiveOrTempMode(Modes mode) {
    	return this.surface.getModeManager().isActiveOrTempMode(mode);
    }


    /** {@inheritDoc} */
    @Override
    protected void scrollUp ()
    {
    	this.model.getHost().println("Sroll up");
        final Mode mode = this.surface.getModeManager ().getActiveOrTempMode ();
        if (mode == null) return;
        if (isActiveOrTempMode(Modes.BROWSER)) {
        	final IBrowser browser = this.model.getBrowser ();
        	browser.selectPreviousResult(); // FIXME: Depending on focused column do something else
        	return;
        }
        mode.selectNextItemPage ();
    }

	/** {@inheritDoc} */
    @Override
    protected void scrollDown ()
    {
    	this.model.getHost().println("Sroll down");
        final Mode mode = this.surface.getModeManager ().getActiveOrTempMode ();
        if (mode == null) return;
        if (isActiveOrTempMode(Modes.BROWSER)) {
        	final IBrowser browser = this.model.getBrowser ();
        	browser.selectNextResult(); // FIXME: Depending on focused column do something else
        	return;
        }
        mode.selectPreviousItemPage ();
    }
    
    @Override
	protected void scrollLeft() {
    	this.model.getHost().println("Sroll left");
    	final Mode mode = this.surface.getModeManager ().getActiveOrTempMode ();
    	if (mode == null) return;
    	if (isActiveOrTempMode(Modes.BROWSER)) {
        	final IBrowser browser = this.model.getBrowser ();
        	browser.selectPreviousFilterColumn(); // FIXME: Choose the previous filter column
        	return;
        }
    	mode.selectPreviousItem ();
	}


	@Override
	protected void scrollRight() {
		this.model.getHost().println("Sroll right");
		final Mode mode = this.surface.getModeManager ().getActiveOrTempMode ();
		if (mode == null) return;
		if (isActiveOrTempMode(Modes.BROWSER)) {
        	final IBrowser browser = this.model.getBrowser ();
        	browser.selectNextFilterColumn(); // FIXME: Choose the next filter column
        	return;
        }
		mode.selectNextItem ();
	}

}
