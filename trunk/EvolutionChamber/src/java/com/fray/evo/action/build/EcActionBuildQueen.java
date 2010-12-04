package com.fray.evo.action.build;

import static com.fray.evo.ui.swingx.EcSwingXMain.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fray.evo.EcBuildOrder;
import com.fray.evo.EcEvolver;
import com.fray.evo.EcState;
import com.fray.evo.action.EcAction;
import com.fray.evo.util.Unit;
import com.fray.evo.util.UnitLibrary;

public class EcActionBuildQueen extends EcActionBuildUnit implements Serializable
{
	public EcActionBuildQueen()
	{
		super(UnitLibrary.Queen);
	}

	@Override
	protected void preExecute(EcBuildOrder s)
	{
		s.consumeHatch(this);
	}

	@Override
	protected void postExecute(final EcBuildOrder s, final EcEvolver e)
	{
            s.unconsumeHatch(this);
		s.AddUnits((Unit) buildable, 1);
		if (s.larva.size() > s.hasQueen.size())
		{
			spawnLarva(s, e);
		}
		else
			s.addFutureAction(5, new Runnable()
			{
				@Override
				public void run()
				{
					if (s.larva.size() > s.hasQueen.size())
						spawnLarva(s, e);
					else
						s.addFutureAction(5, this);
				}
			});
	}

	private void spawnLarva(final EcBuildOrder s, final EcEvolver e)
	{
		int hatchWithoutQueen = 0;
		if (s.larva.size() > s.hasQueen.size())
		{
			hatchWithoutQueen = s.hasQueen.size();
			s.hasQueen.add(true);

			final int hatchIndex = hatchWithoutQueen;
			s.addFutureAction(40, new Runnable()
			{
				@Override
				public void run()
				{
					if (e.debug && s.getLarva() < s.bases() * 19)
						e.obtained(s,  " @"+messages.getString("Hatchery") + " #" + (hatchIndex+1) +" "
								+ messages.getString("Larva")
								+ " +"
								+ (Math.min(19, s.getLarva(hatchIndex) + 2) - s
										.getLarva(hatchIndex)));
					s.setLarva(hatchIndex, Math.min(19, s.getLarva(hatchIndex) + 2));
					s.addFutureAction(1, new Runnable()
					{
						public void run()
						{
							if (e.debug && s.getLarva() < s.bases() * 19)
								e.obtained(s,  " @"+messages.getString("Hatchery") + " #" + (hatchIndex+1) +" "
										+ messages.getString("Larva")
										+ " +"
										+ (Math.min(19, s.getLarva(hatchIndex) + 2) - s
												.getLarva(hatchIndex)));
							s.setLarva(hatchIndex, Math.min(19, s.getLarva(hatchIndex) + 2));
						}
					});
					s.addFutureAction(45, this);
					s.larvaProduction.set(hatchIndex, s.larvaProduction.get(hatchIndex)-1);
				}
			});
		}
	}

	@Override
	public boolean isInvalid(EcBuildOrder s)
	{
		if (s.getSpawningPools() == 0)
			return true;
		if (s.getHatcheries() + s.getLairs() + s.getHives() == s.busyMainBuildings)
			return true;
		return false;
	}

}
