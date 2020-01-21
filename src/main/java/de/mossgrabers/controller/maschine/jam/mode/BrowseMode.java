// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.mode;

import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.daw.IBrowser;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * The browse mode.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class BrowseMode extends BaseMode
{
    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public BrowseMode (final MaschineJamControlSurface surface, final IModel model)
    {
        super ("Browser", surface, model);

        this.isTemporary = true;
    }

    @Override
	public void onKnobTouch(int index, boolean isTouched) {
    	this.model.getHost().println("Index: " + index);
		this.model.getHost().println("Touched: " + isTouched);
		
		super.onKnobTouch(index, isTouched);
	}

	/** {@inheritDoc} */
	@Override
	public void onButton(int row, int index, ButtonEvent event) {
		this.model.getHost().println("Row: " + row);
		this.model.getHost().println("Index: " + index);
		this.model.getHost().println("Button event: " + event);
		
        if (event != ButtonEvent.UP)
            return;

        final IBrowser browser = this.model.getBrowser ();
        
        
	}

    

    /** {@inheritDoc} */
    @Override
    public void onKnobValue (final int index, final int value)
    {
		this.model.getHost().println("Index: " + index);
		this.model.getHost().println("Value: " + value);
    	
        final int speed = (int) this.model.getValueChanger ().calcKnobSpeed (value);
        final IBrowser browser = this.model.getBrowser ();
        if (speed < 0)
            browser.selectPreviousResult ();
        else
            browser.selectNextResult ();
    }
}