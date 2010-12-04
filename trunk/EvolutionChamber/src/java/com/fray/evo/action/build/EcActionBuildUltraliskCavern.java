package com.fray.evo.action.build;

import java.io.Serializable;

import com.fray.evo.EcBuildOrder;
import com.fray.evo.util.BuildingLibrary;

public final class EcActionBuildUltraliskCavern extends EcActionBuildBuilding implements Serializable
{
	public EcActionBuildUltraliskCavern()
	{
		super(BuildingLibrary.UltraliskCavern);
	}
	@Override
	public boolean isInvalid(EcBuildOrder s)
	{
		if (s.getHives() == 0 )
			return true;
		if (s.getUltraliskCavern() == 1)
			return true;
		return super.isInvalid(s);
	}
}
