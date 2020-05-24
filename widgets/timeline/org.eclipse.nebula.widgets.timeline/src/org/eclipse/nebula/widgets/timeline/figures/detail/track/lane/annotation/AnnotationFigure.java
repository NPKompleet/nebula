/*******************************************************************************
 * Copyright (c) 2020 Phenomenon and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Phenomenon - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.nebula.widgets.timeline.Timing;
import org.eclipse.nebula.widgets.timeline.jface.ITimelineStyleProvider;

/**
 * @author Phenomenon
 *
 */
public class AnnotationFigure extends Figure implements IAnnotationFigure {

	private static final int ANNOTATOR_WIDTH = 13;

	private static final int TRIANGLE_SIZE = 6;

	private final RectangleFigure fLineFigure;

	Timing timeStamp;

	public AnnotationFigure(double timeStamp, ITimelineStyleProvider styleProvider) {
		setLayoutManager(new AnnotationFigureLayout());
		this.timeStamp = new Timing(timeStamp);
		updateStyle(styleProvider);

		final Triangle topTriangle = new Triangle();
		topTriangle.setSize(TRIANGLE_SIZE, TRIANGLE_SIZE);
		topTriangle.setOpaque(true);
		topTriangle.setDirection(PositionConstants.RIGHT);
		add(topTriangle, BorderLayout.BOTTOM);

		final Triangle bottomTriangle = new Triangle();
		bottomTriangle.setSize(TRIANGLE_SIZE, TRIANGLE_SIZE);
		topTriangle.setOpaque(true);
		bottomTriangle.setDirection(PositionConstants.LEFT);
		add(bottomTriangle, BorderLayout.TOP);

		fLineFigure = new RectangleFigure();
		fLineFigure.setLineWidth(1);
		add(fLineFigure, BorderLayout.CENTER);

	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(ANNOTATOR_WIDTH, hHint);
	}

	@Override
	public void updateStyle(ITimelineStyleProvider styleProvider) {
		setForegroundColor(styleProvider.getCursorColor());
		setBackgroundColor(styleProvider.getCursorColor());
	}

	private class AnnotationFigureLayout extends BorderLayout {

		@Override
		public void layout(IFigure container) {
			super.layout(container);

			final Rectangle bounds = fLineFigure.getBounds();
			bounds.performTranslate((bounds.width() / 2), 0);
			bounds.setWidth(1);
			bounds.setY(0);
			bounds.setHeight(container.getBounds().height() + 200);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getTimeStamp()
	 */
	@Override
	public double getTimeStamp() {
		return timeStamp.getTimestamp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getAnnotatorWidth()
	 */
	@Override
	public double getAnnotatorWidth() {
		return ANNOTATOR_WIDTH;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure#getTiming()
	 */
	@Override
	public Timing getTiming() {
		return timeStamp;
	}

}
