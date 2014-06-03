package com.mut8ed.battlemap.shared.dto.charactersheet;

import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public interface Watchable {
	
	
	public void addWatcher(Watcher watcher);

	public void informWatchers();


}
