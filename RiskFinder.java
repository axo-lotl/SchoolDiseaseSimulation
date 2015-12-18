package mainpackage;

public class RiskFinder {

	// This class models the risk of an unvaccinated child getting sick based on
	// the unvaccinated proportion u.

	private static final double S_PERTUSSIS = 0.0000405;
	private static final double I_PERTUSSIS = 0.0695;

	private static final double S_VARICELLA = 0.0001235;
	private static final double I_VARICELLA = 0.147;

	private static final double I_HEPATITIS_B = 0.006995;

	private static final double TRIALS = 3000;

	// method to calculate combined risk
	public static double getCombinedRisk(double[] risks) {
		double antirisk = 1;
		for (double d : risks) {
			antirisk *= (1 - d);
		}
		return 1 - antirisk;
	}

	// returns a 4-element double; in order, they are the risks for pertussis,
	// varicella, hepatitis b, and the total combined risk of getting infected
	public static void seeRisk(double u) {
		System.out.println("for u = " + u);
		
		double pertussisCounter = 0;
		double vPertussisCounter = 0;
		for (int i = 0; i < TRIALS; i++) {
			PCalibrationTrialNormal trial = new PCalibrationTrialNormal(
					S_PERTUSSIS, I_PERTUSSIS, (int) Math.round(500 * u));
			pertussisCounter += trial.getUnvaccinatedRisk();
			vPertussisCounter += trial.getVaccinatedRisk();
		}
		double pertussisRisk = pertussisCounter / TRIALS;
		double vPertussisRisk = vPertussisCounter / TRIALS;
		System.out.print("risk of pertussis: " + pertussisRisk + ", ");

		double varicellaCounter = 0;
		double vVaricellaCounter = 0;
		for (int i = 0; i < TRIALS; i++) {
			VCalibrationTrialNormal trial = new VCalibrationTrialNormal(
					S_VARICELLA, I_VARICELLA, (int) Math.round(500 * u));
			varicellaCounter += trial.getUnvaccinatedRisk();
			vVaricellaCounter += trial.getVaccinatedRisk();
		}
		double varicellaRisk = varicellaCounter / TRIALS;
		double vVaricellaRisk = vVaricellaCounter / TRIALS;
		System.out.print("risk of varicella: " + varicellaRisk + ", ");

		double hepatitisCounter = 0;
		double vHepatitisCounter = 0;
		for (int i = 0; i < TRIALS; i++) {
			HCalibrationTrial trial = new HCalibrationTrial(I_HEPATITIS_B,
					(int) Math.round(500 * u));
			hepatitisCounter += trial.getUnvaccinatedRisk();
			vHepatitisCounter += trial.getVaccinatedRisk();
		}
		double hepatitisRisk = hepatitisCounter / TRIALS;
		double vHepatitisRisk = vHepatitisCounter / TRIALS;
		System.out.println("risk of hepatitis: " + hepatitisRisk);
		
		double[] risks = {pertussisRisk, varicellaRisk, hepatitisRisk};
		System.out.print("combined risk: " + getCombinedRisk(risks) + ", ");
		
		double[] vRisks = {vPertussisRisk, vVaricellaRisk, vHepatitisRisk};
		System.out.println("combined risk (vaccinated): " + getCombinedRisk(vRisks));

	}
	
	public static void main(String[] args) {
		
		
	}

}
