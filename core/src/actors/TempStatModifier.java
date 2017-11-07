package actors;

public class TempStatModifier {

		protected float value, timeRemaining;
		protected Stats stat;
		protected int statIndex;
		protected Actor parent;
		
		public TempStatModifier (Stats stat, float value, float timeRemaining) {
			this.stat = stat;
			this.value = value;
			this.timeRemaining = timeRemaining;
			statIndex = stat.ordinal();
		}
		
		protected void update(float deltaTime) {
			timeRemaining -= deltaTime;
		}
		
		public Stats getStat() { 
			return stat;
		}
		
		public int getStatIndex() {
			return stat.ordinal();
		}
		
		public float getValue() {
			return value;
		}
}
