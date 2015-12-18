package mainpackage;

public class HCalibrator {

	public static double runTenMillenia(double I, int unvStudents) {
		double counter = 0;
		for (int i = 0; i < 10000; i++) {
			HCalibrationTrial trial = new HCalibrationTrial(I, unvStudents);
			counter += trial.runYear();
		}
		return counter / 10000;
	}
	
	public static void testValue(double I) {
		double result = runTenMillenia(I, 40);
		System.out.println("result: " + result + ", expected: " + 0.16);
		double error = Math.abs(result - 0.16) / Math.min(result, 0.16) * 100;
		System.out.println("error: " + error + "%");
	}
	
	public static void main(String[] args) {
		testValue(0.006996);
	}

}
