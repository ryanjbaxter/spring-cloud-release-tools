package org.springframework.cloud.release.internal.spring;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.release.internal.ReleaserProperties;
import org.springframework.cloud.release.internal.ReleaserPropertiesAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@Import(ReleaserPropertiesIntegrationTests.Config.class)
public class ReleaserPropertiesIntegrationTests {

	@Autowired List<ReleaserPropertiesAware> propertiesAware;
	@Autowired ApplicationContext context;
	
	@Test public void should_update_properties() {
		ReleaserProperties properties = new ReleaserProperties();
		properties.getPom().setBranch("fooooo");
		
		new ReleaserPropertiesUpdater(this.context).updateProperties(properties,
				new File("."));

		BDDAssertions.then(this.propertiesAware).hasSize(2);
		this.propertiesAware.forEach(aware ->
				BDDAssertions.then(((ReleaserPropertiesHaving) aware)
						.properties.getPom().getBranch()).isEqualTo("fooooo"));
	}

	@Test public void should_update_properties_including_existing_releaser_config() {
		ReleaserProperties properties = new ReleaserProperties();
		properties.getPom().setBranch("barrrr");
		URL resource = ReleaserPropertiesIntegrationTests.class
				.getResource("/projects/project-with-config");

		new ReleaserPropertiesUpdater(this.context).updateProperties(properties,
				new File(resource.getFile()));

		BDDAssertions.then(this.propertiesAware).hasSize(2);
		this.propertiesAware.forEach(aware -> {
			ReleaserPropertiesHaving having = ((ReleaserPropertiesHaving) aware);
				BDDAssertions.then(having.properties.getPom().getBranch()).isEqualTo("barrrr");
				BDDAssertions.then(having.properties.getMaven().getBuildCommand()).isEqualTo("./scripts/noIntegration.sh");
	});
	}

	@Configuration
	static class Config {
		@Bean ReleaserPropertiesAware aware1() {
			return new ReleaserPropertiesHaving();
		}
		@Bean ReleaserPropertiesAware aware2() {
			return new ReleaserPropertiesHaving();
		}
	}

	static class ReleaserPropertiesHaving implements ReleaserPropertiesAware {

		ReleaserProperties properties;

		@Override public void setReleaserProperties(ReleaserProperties properties) {
			this.properties = properties;
		}

		ReleaserProperties getProps() {
			return this.properties;
		}
	}
}