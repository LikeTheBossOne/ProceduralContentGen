package main.enums;

import java.util.HashMap;
import java.util.Map;

public enum Tile {
	WallTL(0),
	WallT(1),
	WallTR(2),
	WallML(3),
	WallM(4),
	WallMR(5),
	WallBL(6),
	WallB(7),
	WallBR(8),
	WallSingleL(9),
	WallSingleLR(10),
	WallSingleR(11),
	
	ForestTL(12),
	ForestT(13),
	ForestTR(14),
	ForestML(15),
	ForestM(16),
	ForestMR(17),
	ForestBL(18),
	ForestB(19),
	ForestBR(20),
	
	WaterTL(21),
	WaterT(22),
	WaterTR(23),
	WaterML(24),
	WaterM(25),
	WaterMR(26),
	WaterBL(27),
	WaterB(28),
	WaterBR(29),
	
	StumpTL(30),
	StumpTR(31),
	StumpBL(32),
	StumpBR(33),
	
	SandTowerT(34),
	SandTowerB(35),
	
	Houses(36),
	
	Ground(37),
	
	Sand(38),
	SandShadow(39),
	
	unused(40),
	unused1(41),
	WallSingle(42),
	WallSingleT(43),
	WallSingleTB(44),
	WallSingleB(45),
	unused6(46),
	unused7(47),
	
	SIZE(48);
	
	private int index;
	private static Map<Integer, Tile> map = new HashMap<Integer, Tile>();
	
	Tile(int index) {
		this.index = index;
	}
	
	static {
		for (Tile tile : Tile.values()) {
			map.put(tile.index, tile);
		}
	}
	
	public static Tile valueOf(int index) {
		return map.get(index);
	}
	
	public int getIndex() {
		return index;
	}
}
