package benchmarking;

public class GameStats {

	public GameStats(double winRatio, double avgScore,
			double avgTime) {
		this.winRatio = winRatio;
		this.avgScore = avgScore;
		this.avgTime = avgTime;
		this.sampleSize = 1;
	}

	public double winRatio;
	public double avgScore;
	public double avgTime;
	public int sampleSize;

	public void print() {
		System.out.println("STATISTICS::Winratio: " + winRatio + "; Average Score: " + avgScore);
		System.out.println("STATISTICS::Average Time: " + avgTime + "; Sample size: " + sampleSize);
	}
}
