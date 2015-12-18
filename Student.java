package mainpackage;

//abstract base class

public abstract class Student {

	public abstract boolean isVaccinated(); // is the Student vaccinated?

	public abstract void advanceDay(); // allows the diseases the Student has to
										// progress

	public abstract boolean canAttend(); // would the Student be allowed to
											// attend school?, i.e. is the
											// disease visible

	public abstract void attemptToInfect(); // attempt to infect the Student;
											// can fail due to vaccination

	public abstract boolean canInfect(); // can the Student infect other
											// Students?

	public abstract boolean isSick(); // is the Student actually sick?

	public abstract boolean wasInfected(); // was the Student ever sick?

}