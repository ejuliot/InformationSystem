package org.obeonetwork.dsl.migration.tests;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.junit.Assert;

abstract public class MigrationTests {
	public static final String PLUGIN_ID = "org.obeonetwork.dsl.migration.tests";

	protected abstract String getRootFolder();
	
	protected abstract String getModelFileExtension();
	
	protected void testMigration(String folder) {
		String sourceModelPath = getFullPathForBeforeModel(folder);
		String expectedModelPath = getFullPathForExpectedModel(folder);
		testMigration(sourceModelPath, expectedModelPath);
	}

	protected void testMigration(String sourceModelPath, String expectedModelPath) {
		URI sourceModelURI = URI.createPlatformPluginURI(PLUGIN_ID + "/" + sourceModelPath, true);
		
		ResourceSet set = new ResourceSetImpl();
		Resource sourceResource = set.getResource(sourceModelURI, true);
		
		Map<Object,Object> options = new HashMap<Object,Object>();
        //change as needed according to the type of resource
        //you are using
		options.put(XMIResource.OPTION_ENCODING, "UTF-8");
 
		StringWriter sw = new StringWriter();
		URIConverter.WriteableOutputStream uws = new URIConverter.WriteableOutputStream(sw, "UTF-8");
		
		try {
			sourceResource.save(uws, options);
		} catch (IOException e) {
			fail("Exception while saving : " + e.getMessage());
		}
		
		String migratedModel = uws.asWriter().toString();
		String expectedModel = getFileContents(expectedModelPath);
		
		Assert.assertEquals("Contents are different",
				normalizeString(expectedModel),
				normalizeString(migratedModel));
	}
	
	protected String normalizeString(String contents) {
		String result = contents;
		result = result.replaceAll("\\r\\n", "\n");
		result = result.replaceAll("\\r", "\n");
		
		// Convert links to environment.common
		result = result.replaceAll("\\.\\./\\.\\./\\.\\./\\.\\./org.obeonetwork.dsl.environment.common/model/obeo.environment", "platform:/plugin/org.obeonetwork.dsl.environment.common/model/obeo.environment");
		
		return result;
	}
	
	protected String getFullPathForBeforeModel(String folder) {
		return getFullPath(folder + "/before." + getModelFileExtension());
	}
	
	protected String getFullPathForExpectedModel(String folder) {
		return getFullPath(folder + "/expected." + getModelFileExtension());
	}
	
	protected String getFullPath(String path) {
		return getRootFolder() + "/" + path;
	}
	
	protected String getFileContents(String path) {
		String result = "";
		URL url;
		try {
			url = new URL("platform:/plugin/" + PLUGIN_ID + "/" + path);
			InputStream inputStream = url.openConnection().getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
				result += "\n";
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}