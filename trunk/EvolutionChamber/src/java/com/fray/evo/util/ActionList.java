package com.fray.evo.util;

import java.util.Comparator;
import java.util.PriorityQueue;

public final class ActionList {
	private int numLeft;
	private PriorityQueue<TWrapper> pq = null;
	
	public ActionList()
	{
		numLeft = 0;
		pq = new PriorityQueue<TWrapper>( 11, new Comparitor() );
	}
	
	public void put( int i, Runnable r )
	{
		pq.add( new TWrapper( i, r ) );
		numLeft++;
	}
	
	public Runnable get( int i )
	{
		if( pq.peek() != null && pq.peek().time == i ) {
			numLeft--;
			return pq.poll().object;
		}
		return null;
	}

	public boolean hasFutureActions()
	{
		return numLeft > 0;
	}

	private class TWrapper {
		public int time;
		public Runnable object;
		
		public TWrapper( int i, Runnable r ) {
			this.time = i;
			this.object = r;
		}
	}

	public class Comparitor implements Comparator<TWrapper> {
		@Override
		public int compare(TWrapper arg0, TWrapper arg1) {
			return arg0.time - arg1.time;
		}
	}
}
