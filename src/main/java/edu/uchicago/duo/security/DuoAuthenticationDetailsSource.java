/**
 * Copyright 2014 University of Chicago
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Author: Daniel Yu <danielyu@uchicago.edu>
 */

package edu.uchicago.duo.security;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.util.StringUtils;

public class DuoAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, GrantedAuthoritiesContainer> {

	protected final Log log = LogFactory.getLog(getClass());

	@Override
	public GrantedAuthoritiesContainer buildDetails(HttpServletRequest request) {

		List gal = new ArrayList();
		try {
			GrantedAuthority ga = null;

			if (StringUtils.hasLength(request.getHeader("uid"))) {
				ga = new SimpleGrantedAuthority("ROLE_USER");
			} else {
				ga = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
			}

			log.debug("UID=" + request.getHeader("uid") + "|Granted:" + ga);

			gal.add(ga);
		} catch (Exception e) {
			throw new AuthenticationServiceException("Error..", e);
		}

		return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(request, gal);
	}
}
