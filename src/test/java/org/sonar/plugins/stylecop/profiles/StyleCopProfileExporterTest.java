package org.sonar.plugins.stylecop.profiles;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RulePriority;
import org.sonar.plugins.stylecop.StyleCopPlugin;
import org.xml.sax.SAXException;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StyleCopProfileExporterTest {
	@Test
	  public void testSimpleStyleCopRulesToExport() throws IOException, SAXException {
	    RulesProfile profile = RulesProfile.create("Sonar C# Way", "cs");
	    profile.activateRule(
	        Rule.create(StyleCopPlugin.REPOSITORY_KEY, "ElementMustBeginWithUpperCaseLetter", "Element must begin with upper case letter")
	            .setConfigKey("Microsoft.StyleCop.CSharp.NamingRules#ElementMustBeginWithUpperCaseLetter"), null);
	    profile.activateRule(
	        Rule.create(StyleCopPlugin.REPOSITORY_KEY, "KeywordsMustBeSpacedCorrectly", "Keywords must be spaced correctly")
	            .setConfigKey("Microsoft.StyleCop.CSharp.SpacingRules#KeywordsMustBeSpacedCorrectly").setSeverity(RulePriority.MINOR), null);

	    StringWriter writer = new StringWriter();
	    RuleFinder ruleFinder = mock(RuleFinder.class);
	    new StyleCopProfileExporter(ruleFinder).exportProfile(profile, writer);

	    
	  }
}
