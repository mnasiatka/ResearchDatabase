import java.util.HashMap;

public class Participant {

	String Name;
	String Birthdate;
	String PhoneNumber;
	String Email;
	String Address;
	String DominantHand;
	String HearingLoss;
	String VisionImpairment;
	String FirstLanguage;
	String LearningDisability;
	String SpeechLanguageReadingDisorder;
	String NeurologicIllness;
	String StrokeAneurysmSevereHeartAttack;
	String PsychiatricMentalIllness;
	String IllicitDrugUse;
	String AlcoholAbuse;
	String Medications;
	String FutureStudies;
	String MRIEnvironmentBefore;
	String Claustrophobic;
	String ContactsGlasses;
	String MetalCannotRemove;
	String WeighLess250;

	HashMap<String, String> hm;

	String[] categories = new String[] { "Name", "Birthdate", "PhoneNumber", "Email", "Address",
			"DominantHand", "HearingLoss", "VisionImpairment", "FirstLanguage",
			"LearningDisability", "SpeechLanguageReadingDisorder", "NeurologicIllness",
			"StrokeAneurysmSevereHeartAttack", "PsychiatricMentalIllness", "IllicitDrugUse",
			"AlcoholAbuse", "Medications", "FutureStudies", "MRIEnvironmentBefore",
			"Claustrophobic", "ContactsGlasses", "MetalCannotRemove", "WeighLess250" };

	public Participant(String[] arr) {
		hm = new HashMap<String, String>();
		for (int i = 0; i < arr.length; i++) {
			hm.put(categories[i], arr[i]);
		}
	}

	public Participant(String Name, String Birthdate, String PhoneNumber, String Email,
			String Address, String DominantHand, String HearingLoss, String VisionImpairment,
			String FirstLanguage, String LearningDisability, String SpeechLanguageReadingDisorder,
			String NeurologicIllness, String StrokeAneurysmSevereHeartAttack,
			String PsychiatricMentalIllness, String IllicitDrugUse, String AlcoholAbuse,
			String Medications, String FutureStudies, String MRIEnvironmentBefore,
			String Claustrophobic, String ContactsGlasses, String MetalCannotRemove,
			String WeighLess250) {
		this.Name = Name;
		this.Birthdate = Birthdate;
		this.PhoneNumber = PhoneNumber;
		this.Email = Email;
		this.Address = Address;
		this.DominantHand = DominantHand;
		this.HearingLoss = HearingLoss;
		this.VisionImpairment = VisionImpairment;
		this.FirstLanguage = FirstLanguage;
		this.LearningDisability = LearningDisability;
		this.SpeechLanguageReadingDisorder = SpeechLanguageReadingDisorder;
		this.NeurologicIllness = NeurologicIllness;
		this.StrokeAneurysmSevereHeartAttack = StrokeAneurysmSevereHeartAttack;
		this.PsychiatricMentalIllness = PsychiatricMentalIllness;
		this.IllicitDrugUse = IllicitDrugUse;
		this.AlcoholAbuse = AlcoholAbuse;
		this.Medications = Medications;
		this.FutureStudies = FutureStudies;
		this.MRIEnvironmentBefore = MRIEnvironmentBefore;
		this.Claustrophobic = Claustrophobic;
		this.ContactsGlasses = ContactsGlasses;
		this.MetalCannotRemove = MetalCannotRemove;
		this.WeighLess250 = WeighLess250;
	}

}
