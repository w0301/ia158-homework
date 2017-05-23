package cz.muni.fi.ia158.homework;

import java.util.Random;

/*
 * Class for storing the last turning direction an "intelligent" prediction of next turn.
 */
public class TurningHistory {	
	public enum TurningSide {
		LEFT, RIGHT;
		
		public TurningSide getOpposite(){
			if(this == TurningSide.LEFT)
				return TurningSide.RIGHT;
			return TurningSide.LEFT;
		}
		
		public int getInt(){
			return this == LEFT ? 1 : -1;
		}
		
		public static TurningSide getRandom(){
			return (RANDOM.nextBoolean() ? TurningSide.LEFT : TurningSide.RIGHT);
		}
	};
	
	private final static Random RANDOM = new Random();

	private TurningSide lastPrediction = TurningSide.RIGHT;
	
	public TurningSide predictSide() {
		double rand = RANDOM.nextDouble();
		lastPrediction = (rand <= 0.75 ? lastPrediction : lastPrediction.getOpposite());
		return lastPrediction;
	}
}
