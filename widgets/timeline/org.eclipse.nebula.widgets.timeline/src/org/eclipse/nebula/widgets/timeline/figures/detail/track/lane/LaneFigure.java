/*******************************************************************************
 * Copyright (c) 2020 Christian Pontesegger and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Christian Pontesegger - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.timeline.figures.detail.track.lane;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.nebula.widgets.timeline.figures.IStyledFigure;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.AnnotationLayer;
import org.eclipse.nebula.widgets.timeline.figures.detail.track.lane.annotation.IAnnotationFigure;
import org.eclipse.nebula.widgets.timeline.jface.ITimelineStyleProvider;

public class LaneFigure extends LayeredPane implements IStyledFigure {

	private int fPreferredHeight;
	private final EventLayer eventLayer;
	private final AnnotationLayer annotationLayer;

	public LaneFigure(ITimelineStyleProvider styleProvider) {
		updateStyle(styleProvider);
		eventLayer = new EventLayer();
		add(eventLayer);
		annotationLayer = new AnnotationLayer();
		add(annotationLayer);
	}

	@Override
	public void updateStyle(ITimelineStyleProvider styleProvider) {
		setForegroundColor(styleProvider.getLaneColor());
		fPreferredHeight = styleProvider.getLaneHeight();
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(wHint, fPreferredHeight);
	}

	public void addEventFigure(IFigure figure, Object constraint) {
		eventLayer.add(figure, constraint);
	}

	public void annotate(IAnnotationFigure figure) {
		annotationLayer.add((IFigure) figure, figure.getTiming());
	}

	public List<EventFigure> getEventFigures() {
		return ((List<?>) eventLayer.getChildren()).stream().filter(p -> p instanceof EventFigure).map(p -> (EventFigure) p).collect(Collectors.toList());
	}

	public void revalidateChildren() {
		eventLayer.revalidate();
		annotationLayer.revalidate();
	}

	public EventLayer getEventLayer() {
		return eventLayer;
	}

}
