This code was written for the second Ph 011 hurdle of 2015-16.

STUDENT is just an abstract base class. PSTUDENT, VSTUDENT, and HSTUDENT (which represent students that simulate pertussis, varicella, and hepatitis b, respectively), extend from this base class.

The classes with "CalibrationTrial" in them are basically objects that represent the "environment" the students are in. "CalibrationTrialNormal" refers to a simulation of a normal school environment, while "CalibrationTrialSAR" refers to a simulation in a household-like environment with no vaccination and no student absences (for the purposes of evaluating the secondary attack rate).

The "Calibration" classes merely test values of I (number of external infection attempts per student per day) and S (number of student-to-student infection attempts per student per day) by using the "CalibrationTrial" classes. The best-fitting values of I and S are then used in RiskFinder.

RiskFinder is just a class that gives the total risk of infection as a function of the unvaccinated proportion, using the results from the "Calibration" classes.

More specific details can be found in the code itself.