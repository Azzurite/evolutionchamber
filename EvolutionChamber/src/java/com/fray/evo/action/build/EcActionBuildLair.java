package com.fray.evo.action.build;

import java.io.Serializable;

import com.fray.evo.EcBuildOrder;
import com.fray.evo.EcEvolver;
import com.fray.evo.util.Building;
import com.fray.evo.util.BuildingLibrary;
import com.fray.evo.util.GameLog;

public final class EcActionBuildLair extends EcActionBuildBuilding implements Serializable
{
	public EcActionBuildLair()
	{
		super(BuildingLibrary.Lair);
	}

	@Override
	protected void preExecute(EcBuildOrder s)
	{
		s.consumeHatch((Building)buildable.getConsumes(),this);
	}

	@Override
	protected void postExecute(EcBuildOrder s, GameLog e)
	{
            s.unconsumeHatch(this);
            s.RemoveBuilding(BuildingLibrary.Hatchery);
		s.AddBuilding((Building) buildable);
	}

	@Override
	public boolean isPossible(EcBuildOrder s)
	{
		if (!s.doesNonBusyReallyExist((Building)buildable.getConsumes()))
			return false;
		return super.isPossible(s);
	}


}
