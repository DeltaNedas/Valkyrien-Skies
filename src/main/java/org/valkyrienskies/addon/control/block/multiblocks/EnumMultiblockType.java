package org.valkyrienskies.addon.control.block.multiblocks;

public enum EnumMultiblockType {
	NONE(""),
	COMPRESSOR("multiblock_valkyrium_compressor"),
	ENGINE("multiblock_valkyrium_engine"),
	PROPELLER("multiblock_giant_propeller"),
	RUDDER("multiblock_rudder");

	private String name;

	EnumMultiblockType(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
}
