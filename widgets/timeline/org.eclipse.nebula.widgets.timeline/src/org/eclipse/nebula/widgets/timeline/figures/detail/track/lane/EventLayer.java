/*******************************************************************************
 * Copyright (c) 2020 Philip Okonkwo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Philip Okonkwo - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.timeline.figures.detail.track.lane;

import java.util.ListIterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.nebula.widgets.timeline.ITimelineEvent;
import org.eclipse.nebula.widgets.timeline.TimeBaseConverter;
import org.eclipse.nebula.widgets.timeline.Timing;
import org.eclipse.nebula.widgets.timeline.figures.RootFigure;

/**
 * @author Philip Okonkwo
 *
 */
public class EventLayer extends Layer {

	public EventLayer() {
		setLayoutManager(new EventLayout());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);

		getChildren().sort((o1, o2) -> {
			return ((EventFigure) o1).compareTo((EventFigure) o2);
		});
	}

	private class EventLayout extends XYLayout {

		private Rectangle getConstraintAsRectangle(IFigure figure) {

			final ITimelineEvent event = (ITimelineEvent) getConstraint(figure);

			return new PrecisionRectangle(event.getStartTimestamp(), 0, event.getDuration(), 1);
		}

		@Override
		public void layout(IFigure parent) {
			final TimeBaseConverter timeViewDetails = RootFigure.getRootFigure(parent).getTimeViewDetails();

			for (final Object figure : getChildren()) {
				final Rectangle screenBounds;
				final ITimelineEvent event = (ITimelineEvent) getConstraint((IFigure) figure);

				final Timing screenCoordinates = timeViewDetails.toDetailCoordinates(event.getTiming());
				screenBounds = new PrecisionRectangle(screenCoordinates.getTimestamp(), getBounds().y(), screenCoordinates.getDuration(), getBounds().height());

				if (screenBounds.width() == 0)
					screenBounds.setWidth(1);

				((IFigure) figure).setBounds(screenBounds);
			}
		}

		/**
		 * This is a copy of the parent method. Only change is that we call getContraintAsRectangle() instead of directly accessing the constraints member.
		 */
		@Override
		protected Dimension calculatePreferredSize(IFigure f, int wHint, int hHint) {
			final Rectangle rect = new Rectangle();
			final ListIterator children = f.getChildren().listIterator();
			while (children.hasNext()) {
				final IFigure child = (IFigure) children.next();
				Rectangle r = getConstraintAsRectangle(child);
				if (r == null)
					continue;

				if ((r.width == -1) || (r.height == -1)) {
					final Dimension preferredSize = child.getPreferredSize(r.width, r.height);
					r = r.getCopy();
					if (r.width == -1)
						r.width = preferredSize.width;
					if (r.height == -1)
						r.height = preferredSize.height;
				}
				rect.union(r);
			}
			final Dimension d = rect.getSize();
			final Insets insets = f.getInsets();
			return new Dimension(d.width + insets.getWidth(), d.height + insets.getHeight()).union(getBorderPreferredSize(f));
		}
	}

}
