package cz.muni.fi.ia158.homework;


import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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
	
	private List<TurningSide> listHistory = new ArrayList<TurningSide>();
	private final static Random RANDOM = new Random();
	private final static int MAX_HISTORY = 3;
	
	public TurningSide predictSide(){
		if(listHistory.size() == 0){
			return TurningSide.getRandom();
		}
		else if(listHistory.size() == 1){
			return listHistory.get(0);
		}
		int lastTurnImpact = 400;
		int otherTurnsImpact = (1000 - lastTurnImpact) / (listHistory.size() - 1);
		int sum = 0;
		TurningSide lastTurn = listHistory.get(listHistory.size()-1);
		for(int i = 0; i < listHistory.size()-2; i++){
			if(listHistory.get(i) != lastTurn)
				sum += otherTurnsImpact;
		}
		return (sum>500) ? lastTurn.getOpposite() : lastTurn;
	}
	
	public void push(TurningSide side){
		while(listHistory.size() >= MAX_HISTORY){
			listHistory.remove(0);
		}
		listHistory.add(side);
	}
	

}
