package com.syu.dvr.observ;

import com.syu.dvr.observ.WatchedUsb.Watcher;

public abstract class Watched {
	public abstract void addWatched(Watcher watched);
	public abstract void removeWatched(Watcher watched);
	public abstract void notifyWatchers(int uddata);

}
