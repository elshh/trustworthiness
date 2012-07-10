package eu.aniketos.wp2.components.trustworthiness.impl.rules.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.KnowledgeBase;
//import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
//import org.drools.SystemEventListener;
//import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.io.Resource;

//note that builder configuration nor knowledge base configuration is supplied
//maybe easy to add as another constructor arguments? via util.properties?

//doesn't support complex resources types like decision table
/**
 * @author Hisain Elshaafi (TSSG)
 *
 */
public class KnowledgeBaseFactoryService implements
		KnowledgeBaseFactoryManagement {
	
	private KnowledgeBase knowledgeBase;

	

	/**
	 * builds the knowledge base and caches it
	 * 
	 * @param resourceMap
	 *            source resources (DRL, RF files ...)
	 * @throws IOException
	 *             in case of problems while reading resources
	 */
	public void create() throws IOException {
				
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
				
		KnowledgeBuilder builder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		

		Map<Resource, ResourceType> resourceMap = new HashMap<Resource, ResourceType>();
		/**
		 * TODO: DI + support multiple resources
		 */
		resourceMap.put(ResourceFactory.newClassPathResource("experience.drl"),
				ResourceType.DRL);
			/*resourceMap.put(ResourceFactory.newClassPathResource("role-based.drl"),
					ResourceType.DRL);
			resourceMap.put(ResourceFactory.newClassPathResource("referral.drl"),
					ResourceType.DRL);*/

		for (Entry<Resource, ResourceType> entry : resourceMap.entrySet()) {
			builder.add(ResourceFactory.newInputStreamResource(entry.getKey()
					.getInputStream()), entry.getValue());
		}

		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}

		/*KnowledgeBaseConfiguration config = KnowledgeBaseFactory
        .newKnowledgeBaseConfiguration();
		config.setOption( EventProcessingOption.STREAM );*/
  
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
	}

	/**
	 * returns cached knowledge base
	 */
	public KnowledgeBase getKnowledgeBase() {
		return this.knowledgeBase;
	}

	/**
	 * returns the KnowledgeBase class
	 */
	public Class<KnowledgeBase> getKnowledgeBaseType() {
		return KnowledgeBase.class;
	}

	/* (non-Javadoc)
	 * @see eu.aniketos.wp2.components.trustworthiness.impl.rules.model.KnowledgeBaseFactoryManagement#destroy()
	 */
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}
	
	

}
