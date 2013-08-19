package org.bytefire.ld27.html;

import org.bytefire.ld27.core.LD27;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class LD27Html extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new LD27();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
