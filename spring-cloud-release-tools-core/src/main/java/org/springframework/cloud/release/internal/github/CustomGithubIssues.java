/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.release.internal.github;

import org.springframework.cloud.release.internal.ReleaserProperties;
import org.springframework.cloud.release.internal.project.ProjectVersion;
import org.springframework.cloud.release.internal.project.Projects;

public interface CustomGithubIssues {

	/**
	 * Default no op implementation.
	 */
	CustomGithubIssues NO_OP = new CustomGithubIssues() {

		@Override
		public boolean isApplicable(ReleaserProperties properties, Projects projects,
				ProjectVersion version) {
			return true;
		}

		@Override
		public void fileIssueInSpringGuides(Projects projects, ProjectVersion version) {

		}

		@Override
		public void fileIssueInStartSpringIo(Projects projects, ProjectVersion version) {

		}
	};

	boolean isApplicable(ReleaserProperties properties, Projects projects,
			ProjectVersion version);

	void fileIssueInSpringGuides(Projects projects, ProjectVersion version);

	void fileIssueInStartSpringIo(Projects projects, ProjectVersion version);

}
