package org.sonar.plugins.stylecop.profiles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.rules.RuleQuery;
import org.sonar.plugins.stylecop.StyleCopPlugin;
import org.xml.sax.SAXException;

import com.google.common.base.Charsets;

public class StyleCopProfileExporterTest {
	@Test
	  public void testSimpleStyleCopRulesToExport() throws IOException, SAXException {
	    RulesProfile profile = RulesProfile.create("Sonar C# Way", "cs");
	    Rule elementMustBeginWithUpperCaseLetter = Rule.create(StyleCopPlugin.REPOSITORY_KEY, "ElementMustBeginWithUpperCaseLetter", "Element must begin with upper case letter")
		    .setConfigKey("Microsoft.StyleCop.CSharp.NamingRules#ElementMustBeginWithUpperCaseLetter");
		profile.activateRule(
	        elementMustBeginWithUpperCaseLetter, null);
	    Rule keywordMustBeSpacedCorrectly = Rule.create(StyleCopPlugin.REPOSITORY_KEY, "KeywordsMustBeSpacedCorrectly", "Keywords must be spaced correctly")
		    .setConfigKey("Microsoft.StyleCop.CSharp.SpacingRules#KeywordsMustBeSpacedCorrectly").setSeverity(RulePriority.MINOR);
		profile.activateRule(
	        keywordMustBeSpacedCorrectly, null);

		Rule notActivatedRule = Rule.create(StyleCopPlugin.REPOSITORY_KEY, "NotActivatedRule", "Not activated rule")
			    .setConfigKey("Microsoft.StyleCop.CSharp.SpacingRules#NotActivatedRule").setSeverity(RulePriority.MINOR);
		
	    StringWriter writer = new StringWriter();
	    
	    List<Rule> rules = new ArrayList<Rule>();
	    rules.add(elementMustBeginWithUpperCaseLetter);
	    rules.add(keywordMustBeSpacedCorrectly);
	    rules.add(notActivatedRule);

	    RuleFinder ruleFinder = mock(RuleFinder.class);
	    when(ruleFinder.findAll(Mockito.any(RuleQuery.class))).thenReturn(rules);
	    
	    StyleCopProfileExporter exporter = new StyleCopProfileExporter(ruleFinder);
	    
	    exporter.exportProfile(profile, writer);

	    Assert.assertEquals(clean(readFile("src/test/resources/StyleCopProfileExporterTest/exported.xml", Charsets.UTF_8)), clean(writer.toString()));
	  }
	
	private String clean(String string) {
		return string.replace("\r", "").replace("\n", "");
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}
