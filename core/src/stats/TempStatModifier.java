package stats;

import actors.Actor;

public abstract class TempStatModifier {
		public boolean remove = false;
		protected float value;
		protected Stats stat;
		protected int statIndex;
		protected Actor parent;
		
		public TempStatModifier (Stats stat, float value, Actor parent) {
			this.parent = parent;
			this.stat = stat;
			this.value = value;
			statIndex = stat.ordinal();
		}
		
		public abstract void update(float deltaTime);

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
