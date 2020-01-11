// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.maschine.jam.view;

import de.mossgrabers.controller.maschine.jam.controller.MaschineJamColorManager;
import de.mossgrabers.controller.maschine.jam.controller.MaschineJamControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.controller.grid.IPadGrid;
import de.mossgrabers.framework.daw.DAWColor;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.ISceneBank;
import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.framework.mode.AbstractMode;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * The Scene view.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SceneView extends BaseView
{
    /**
     * Constructor.
     *
     * @param surface The controller
     * @param model The model
     */
    public SceneView (final MaschineJamControlSurface surface, final IModel model)
    {
        super ("Scene", surface, model);
    }


    /** {@inheritDoc} */
    @Override
    protected void executeFunction (final int padIndex)
    {
        final IScene scene = this.model.getSceneBank ().getItem (padIndex);

        if (this.surface.isPressed (ButtonID.DUPLICATE))
        {
            this.surface.setTriggerConsumed (ButtonID.DUPLICATE);
            scene.duplicate ();
            return;
        }

        if (this.surface.isDeletePressed ())
        {
            this.surface.setTriggerConsumed (ButtonID.DELETE);
            scene.remove ();
            return;
        }

        if (this.surface.getConfiguration ().isSelectClipOnLaunch ())
            scene.select ();
        scene.launch ();
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
                this.model.getCurrentTrackBank ().selectPreviousItem ();
                break;
            case 1:
                this.model.getCurrentTrackBank ().selectNextItem ();
                break;
            case 2:
                this.model.getSceneBank ().selectPreviousPage ();
                break;
            case 3:
                this.model.getSceneBank ().selectNextPage ();
                break;
            default:
                // Not used
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void drawGrid ()
    {
        final IPadGrid padGrid = this.surface.getPadGrid ();

        final ISceneBank sceneBank = this.model.getSceneBank ();
        for (int i = 0; i < 16; i++)
        {
            final IScene item = sceneBank.getItem (i);
            final int x = i % 4;
            final int y = 3 - i / 4;
            if (item.doesExist ())
            {
                if (item.isSelected ())
                    padGrid.lightEx (x, y, MaschineJamColorManager.COLOR_WHITE);
                else
                    padGrid.lightEx (x, y, DAWColor.getColorIndex (item.getColor ()));
            }
            else
                padGrid.lightEx (x, y, AbstractMode.BUTTON_COLOR_OFF);
        }
    }
}