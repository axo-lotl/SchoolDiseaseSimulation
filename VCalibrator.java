package mainpackage;

public class VCalibrator {


	private static final double expectedNormal = 2.66;
	private static final double expectedSAR = 366.25;
	
	public static double runMilleniumNormal(double S, double I,
			int unvStudents) {
		double counter = 0;
		for (int i = 0; i < 1000; i++) {
			VCalibrationTrialNormal trial = new VCalibrationTrialNormal(S, I,
					unvStudents);
			counter += trial.runYear();
		}
		return counter / 1000;
	}
	
	public static double runMilleniumSAR(double S, double I) {
		double counter = 0;
		for (int i = 0; i < 1000; i++) {
			VCalibrationTrialSAR trial = new VCalibrationTrialSAR(S, I);
			counter += trial.runYear();
		}
		return counter / 1000;
	}
	
	public static void testValues(double S, double I) {
		double resultNormal = runMilleniumNormal(S, I, 25);
		double resultSAR = runMilleniumSAR(S, I);
		System.out.println("normal trial: " + resultNormal + ", expected: " + expectedNormal);
		System.out.println("SAR trial: " + resultSAR + ", expected: " + expectedSAR);
		System.out.println("error: "
				+ getErrorPercentage(S, I, resultNormal, resultSAR) + "%");
	}

	public static double getErrorPercentage(double S, double I,
			double resultNormal, double resultSAR) {
		double normalError = Math.abs(resultNormal - expectedNormal)
				/ Math.min(resultNormal, expectedNormal);
		double attackRateError = Math.abs(resultSAR - expectedSAR)
				/ Math.min(resultSAR, expectedSAR);
		return (normalError + attackRateError) * 100;
	}
	
	public static void main(String[] args) {
		testValues(0.0001235, 0.147);
	}



}
