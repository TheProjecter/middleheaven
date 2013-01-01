package org.middleheaven.ui.models.impl;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.AbstractUIModel;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.events.UIWindowEvent;
import org.middleheaven.ui.models.UIWindowModel;

public class DefaultUIWindowModel extends AbstractUIModel implements UIWindowModel {

	private TextLocalizable title;
	
	@Override
	public void onClosed(UIPrespectiveEvent event) {
		UIClient client = UISearch.absolute(event.getSource()).self().first(UIClient.class);
		client.exit();
	}

	@Override
	public void onClosing(UIPrespectiveEvent event) {
		event.getSource().setVisible(false);
	}

	@Override
	public void onDeiconified(UIPrespectiveEvent event) {
		//no-op
	}

	@Override
	public void onIconified(UIPrespectiveEvent prespectiveEvent) {
		//no-op
	}

	@Override
	public void onOpened(UIPrespectiveEvent event) {
		//no-op
	}

	@Override
	public TextLocalizable getTitle() {
		return title;
	}

	@Override
	public void setTitle(TextLocalizable title) {
		this.title = title;
	}

	@Override
	public void onAtivated(UIWindowEvent event) {
		// no-op
	}

	@Override
	public void onDeativated(UIWindowEvent event) {
		// no-op
	}

}
