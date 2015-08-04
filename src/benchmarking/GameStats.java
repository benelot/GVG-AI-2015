package benchmarking;

public class GameStats {

	public GameStats(double detRatio, double winRatio, double avgScore,
			double avgTime) {
		this.detRatio = detRatio;
		this.winRatio = winRatio;
		this.avgScore = avgScore;
		this.avgTime = avgTime;
		this.sampleSize = 1;
	}

	public double detRatio;
	public double winRatio;
	public double avgScore;
	public double avgTime;
	public int sampleSize;

	public void print() {
		System.out.println("Deterministic Ratio:" + detRatio);
		System.out.println("Winratio: " + winRatio);
		System.out.println("Average Score: " + avgScore);
		System.out.println("Average Time: " + avgTime);
		System.out.println("Sample size: " + sampleSize);
		System.out.println("---------------------------------");
	}
}
