package cz.dd4j.agents.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.cuni.amis.utils.eh4j.AsEnumClass;
import cz.cuni.amis.utils.eh4j.Enums;
import cz.cuni.amis.utils.eh4j.shortcut.EH;
import cz.dd4j.domain.EElement;
import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EFeature;
import cz.dd4j.domain.EPlace;

@AsEnumClass
public class CommandParam {
	
	public static final CommandParam PARAM_ROOM_REQUIRED = new CommandParam("Room", true, EPlace.ROOM);
	
	public static final CommandParam PARAM_ENTITY_REQUIRED = new CommandParam("Entity", true, EElement.ENTITY);
	
	public static final CommandParam PARAM_HERO_OR_MONSTER_REQUIRED = new CommandParam("HeroOrMonster", true, EEntity.HERO, EEntity.MONSTER);
	
	public static final CommandParam PARAM_FEATURE_REQUIRED = new CommandParam("Feature", true, EEntity.FEATURE);
	
	public static final CommandParam PARAM_ITEM_REQUIRED = new CommandParam("Item", true, EElement.ITEM);

	// ==================
	// ACTION PARAM CLASS
	// ==================

	public final String name;
	
	public final boolean required;
	
	public final Set<EElement> validTargets;
	
	/**
	 * KEY: EElement
	 * VALUE: valid target?
	 */
	private Map<EElement, Boolean> targetCache = new HashMap<EElement, Boolean>();
	
	public CommandParam(String name, boolean required, EElement... validTargets) {
		this.name = name;
		this.required = required;
		this.validTargets = new HashSet<EElement>();
		for (EElement validTarget : validTargets) {
			this.validTargets.add(validTarget);
		}
	}
	
	/**
	 * Whether 'element' is valid target (hierarchy wise...).
	 * @param element
	 * @return
	 */
	public boolean isValidTarget(EElement element) {
		if (element == null) return false;
		
		Boolean result = targetCache.get(element);
		if (result != null) return result;
		
		if (validTargets.contains(element)) {
			targetCache.put(element, true);
			return true;
		}
		for (EElement validTarget : validTargets) {
			if (EH.isA(element, validTarget)) {
				targetCache.put(element, true);
				return true;
			}
		}
		targetCache.put(element, false);
		return false;		
	}
	
	static {
		Enums.getInstance().registerEnumClass(CommandParam.class);
	}
	
	
}
