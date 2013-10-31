package velir.intellij.cq5.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeRegistry;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import velir.intellij.cq5.config.JCRConfiguration;

public class JCRFacet extends Facet<JCRConfiguration> {
	private JCRConfiguration jcrConfiguration;

	public JCRFacet(@NotNull Module module, @NotNull String name, @NotNull JCRConfiguration configuration) {
		super(getFacetType(), module, name, configuration, null);
		jcrConfiguration = configuration;
	}

	public static JCRFacetType getFacetType () {
		return (JCRFacetType) FacetTypeRegistry.getInstance().findFacetType(JCRFacetType.JCR_TYPE_ID);
	}

	public JCRConfiguration getJcrConfiguration() {
		return jcrConfiguration;
	}

	public void initFacet() {
		super.initFacet();
		jcrConfiguration.processNewConnectionSettings();
	}
}
