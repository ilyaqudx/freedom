package freedom.study.interview.design.state.enums;

import freedom.study.interview.design.state.GameTable;

public interface State {

	public void action(GameTable table);
}
