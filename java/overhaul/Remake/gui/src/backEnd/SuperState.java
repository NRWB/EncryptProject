package backEnd;

public enum SuperState {
	
	PAUSE, START, STOP;
	
	@Override
	public String toString() {
		String result = "";
		switch (this) {
		case PAUSE:
			result = "PAUSE";
			break;
		case START:
			result = "START";
			break;
		case STOP:
		default:
			result = "STOP";
			break;
		}
		return result;
	}

}
