package com.test.main;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import com.test.model.CardTypes;

public class Cards {

	public static final void main(String[] args) {

		try {
			  long s = System.currentTimeMillis();
			
			  KieServices kieServices = KieServices.Factory.get(); Resource dt =
			  ResourceFactory.newClassPathResource("rules/CardRules.xlsx"); KieFileSystem
			  kieFileSystem = kieServices.newKieFileSystem().write(dt); KieBuilder
			  kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
			  KieRepository kieRepository = kieServices.getRepository(); ReleaseId
			  krDefaultReleaseId = kieRepository.getDefaultReleaseId(); KieContainer
			  kieContainer = kieServices.newKieContainer(krDefaultReleaseId); KieSession
			  kSession = kieContainer.newKieSession();
			  
				CardTypes card = new CardTypes();
	
				card.setTypeOfCard("Prepaid");
				//card.setTypeOfCard("Credit");
				card.setCardUsage("Weekly");
				//card.setCardUsage("Monthly");
				//card.setCardUsage("Early");
				card.setTopupCCUsage(4200);
				//card.setTopupCCUsage(20500);
				//card.setTopupCCUsage(55000);
				
				FactHandle fact;
				fact = kSession.insert(card);
				kSession.fireAllRules();
				
				if(card.getFeesPercentage() != 0 && card.getTypeOfCard() == "Prepaid" ) {
					 long prepaidFee = (card.getTopupCCUsage() + card.getFeesPercentage());
					 
					 System.out.println("Dear Customer - Message- " + card.getTopupWarning() + "Your Total bill "+ prepaidFee);
				}
				else if(card.getFeesPercentage() != 0 && card.getTypeOfCard() == "Credit" ) {
					 long creditCharge = (card.getTopupCCUsage() * card.getFeesPercentage() / 100 );
					 
					 System.out.println("Dear Customer - Message- " + card.getTopupWarning() + "Your Total bill "+ creditCharge);
				}
	
				long e =  System.currentTimeMillis();	
				System.out.println("Time taken: " + (e-s));
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
