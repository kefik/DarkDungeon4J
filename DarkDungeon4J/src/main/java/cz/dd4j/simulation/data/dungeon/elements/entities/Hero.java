package cz.dd4j.simulation.data.dungeon.elements.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.dd4j.domain.EEntity;
import cz.dd4j.domain.EItem;
import cz.dd4j.simulation.data.dungeon.elements.items.Item;

public class Hero extends Entity {
	
	public static class Inventory {
		
		/**
		 * Inventory of the player.
		 */
		private Map<Integer, Item> inventory = new HashMap<Integer, Item>();
		
		public Map<Integer, Item> getData() {
			return inventory;
		}
		
		public boolean has(int itemId) {		
			return inventory.containsKey(itemId);
		}
		
		public boolean has(Item item) {		
			return has(item.id);
		}

		public boolean has(EItem itemType) {
			for (Item item : inventory.values()) {
				if (item.type == itemType) return true;
			}
			return false;
		}
		
		public Item get(int itemId) {
			return inventory.get(itemId);
		}
		
		public Item get(EItem itemType) {
			for (Item item : inventory.values()) {
				if (item.type == itemType) return item;
			}
			return null;
		}
		
		public Item remove(int itemId) {
			return inventory.remove(itemId);
		}
		
		public Item remove(EItem itemType) {
			return inventory.remove(get(itemType));
		}
		
		public Item remove(Item item) {
			return inventory.remove(item.id);
		}
		
		public void add(Item item) {
			inventory.put(item.id, item);
		}

		public Collection<Item> values() {
			return inventory.values();
		}
		
	}

	/**
	 * Current item within hero's hands.
	 */
	public Item hand = null;	
	
	/**
	 * Inventory of the hero; map of items/
	 */
	public Inventory inventory = new Inventory();
	
	public Hero() {
		super(EEntity.HERO);
	}
	
	

	
}
