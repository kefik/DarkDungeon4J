package cz.dd4j.simulation.result;

public enum SimResultType {
	
	HERO_WIN(true, false),
	HEROES_LOSE(false, false),
	TIMEOUT(false, false),
	SIMULATION_EXCEPTION(false, true),
	AGENT_EXCEPTION(false, true);
	
	public final boolean victory;
	public final boolean failure;	
	
	private SimResultType(boolean victory, boolean failure) {
		this.victory = victory;
		this.failure = failure;
	}
	
}
