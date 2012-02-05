package net.lethargiclion.tradingpost;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a trade where a user is offering items in return for bids.
 * @author TerrorBite
 *
 */
public class SellTrade extends TradeBase {
	
	List<ItemBid> bids;
	TradeStatus status;
	int AcceptedBidId;
	
	public SellTrade(OfflinePlayer p, List<ItemStack> items) {
		this(p, items, TradingPost.getManager().getNextId(), new Date(),
				TradeStatus.open, new ArrayList<Integer>());
	}
	
	public SellTrade(OfflinePlayer p, List<ItemStack> items, int id,
			Date timestamp, TradeStatus status, List<Integer> bids) {
		this.owner = p;
		this.items = items;
		this.id = id;
		this.timestamp = timestamp;
		this.status = status;
		
		Iterator<Integer> i = bids.iterator();
		while(i.hasNext()) {
			this.bids.add(TradingPost.getManager().getBid(i.next()));
		}
	}

	public static SellTrade deserialize(Map<String, Object> serial) {
		OfflinePlayer owner = Bukkit.getOfflinePlayer((String)serial.get("owner"));
		
		//Deserialize list of items
		List<ItemStack> items = new ArrayList<ItemStack>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> itemstacks = (ArrayList<Map<String,Object>>)serial.get("items");
		Iterator<Map<String, Object>> i = itemstacks.iterator();
		while(i.hasNext()) {
			items.add(ItemStack.deserialize(i.next()));
		}
		
		return new SellTrade(owner, items);
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serial = new LinkedHashMap<String, Object>();
		serial.put("owner",	this.owner.getName());
		serial.put("id", this.id);
		serial.put("status", this.status.name());
		
		// Serialize items
		List<Map<String, Object>> itemstacks = new ArrayList<Map<String,Object>>();
		Iterator<ItemStack> i = items.iterator();
		while(i.hasNext()) {
			itemstacks.add(i.next().serialize());
		}
		serial.put("items", itemstacks);
		
		return serial;
	}

}
